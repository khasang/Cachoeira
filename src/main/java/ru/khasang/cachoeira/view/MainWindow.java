package ru.khasang.cachoeira.view;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;
import ru.khasang.cachoeira.model.Task;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by truesik on 28.09.2015.
 */
public class MainWindow implements IWindow {
    @FXML
    private TreeTableView<ITask> taskTreeTableView;     //таблица задач <Task>
    @FXML
    private TreeTableColumn<ITask, String> taskNameColumn;      //столбец с наименованием задачи <Task, String>
    @FXML
    private TreeTableColumn<ITask, Date> finishDateColumn;    //столбец с датой окончания задачи <Task, Date>
    @FXML
    private TreeTableColumn<ITask, Date> startDateColumn;     //столбец с датой начала задачи <Task, Date>
    @FXML
    private ScrollPane taskGanttScrollPane;      //здесь должен быть канвас, также возможна с помощью этого скролла получится синхронизировать вертикальные скроллы таблицы задач и ганта
    @FXML
    private TableView<IResource> resourceTableView;         //таблица ресурсов <Resource>
    @FXML
    private TableColumn<IResource, String> resourceNameColumn;      //стоблец с наименованием ресурса <Resource, String>
    @FXML
    private TableColumn<IResource, ResourceType> resourceTypeColumn;      //столбец с типом ресурса <Resource, String>
    @FXML
    private ScrollPane resourceGanttScrollPane;  //здесь должен быть канвас, также возможна с помощью этого скролла получится синхронизировать вертикальные скроллы таблицы ресурсов и ганта

    private Parent root = null;
    private Stage stage;
    private UIControl UIControl;
    private IController controller;
    private TreeItem<ITask> rootTask = new TreeItem<>(new Task());  //todo исправить new Task на контроллер
    private ObservableList<ITask> taskTableModel = FXCollections.observableArrayList();        //<Task> модель для задач
    private ObservableList<IResource> resourceTableModel = FXCollections.observableArrayList();    //<Resource> модель для ресурсов

    public MainWindow(IController controller, UIControl UIControl) {
        this.controller = controller;
        this.UIControl = UIControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));  //грузим макет окна
        fxmlLoader.setController(this);                                                     //говорим макету, что этот класс является его контроллером
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launch() {
        stage = new Stage();
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Cachoeira");
        stage.show();

        //при нажатии на крестик в тайтле
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
//                onClose();
//              if (произошли изменения в проекте) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cachoeira");
                alert.setHeaderText("Вы хотите сохранить изменения в Имя проекта?");

                ButtonType saveProjectButtonType = new ButtonType("Сохранить");
                ButtonType dontSaveProjectButtonType = new ButtonType("Не сохранять");
                ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(saveProjectButtonType, dontSaveProjectButtonType, cancelButtonType);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == saveProjectButtonType) {
                    //сохранение
                } else if (result.get() == dontSaveProjectButtonType) {
                    //закрываем программу без сохранения
                    System.exit(0);
                } else if (result.get() == cancelButtonType) {
                    event.consume(); //отмена закрытия окна
                }
                //}
            }
        });

        //нужно заполнить таблицы элементами
        taskTreeTableView.setRoot(rootTask); //вешаем корневой TreeItem в TreeTableView. Он в fxml стоит как невидимый (<TreeTableView fx:id="taskTreeTableView" showRoot="false">).
        rootTask.setExpanded(true); //делаем корневой элемент расширяемым, т.е. если у TreeItem'а экспэндед стоит тру, то элементы находящиеся в подчинении (children) будут видны, если фолз, то соответственно нет.
        refreshTaskTableModel(); //костыль
        refreshResourceTableModel();

        taskNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        startDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getStartDate()));
        finishDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getFinishDate()));

        resourceNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));
        resourceTypeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<ResourceType>(param.getValue().getType()));

        //контекстные меню в списках задач и ресурсов
        //контекстное меню на пустом месте таблицы
        ContextMenu taskTableMenu = new ContextMenu();
        MenuItem addNewTask = new MenuItem("Новая задача");
        addNewTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openNewTaskWindow();
            }
        });
        taskTableMenu.getItems().addAll(addNewTask);   //заполняем меню
        taskTreeTableView.setContextMenu(taskTableMenu);

        ContextMenu resourceTableMenu = new ContextMenu();
        MenuItem addNewResource = new MenuItem("Новый ресурс");
        addNewResource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openNewResourceWindow();
            }
        });
        resourceTableMenu.getItems().addAll(addNewResource);   //заполняем меню
        resourceTableView.setContextMenu(resourceTableMenu);

        taskTreeTableView.setRowFactory(new TaskContextMenuRowFactory(this)); //контекстное меню для каждого элемента таблицы задач
        resourceTableView.setRowFactory(new ResourceContextMenuRowFactory(this)); //контекстное меню для каждого элкмента таблицы ресурсов
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    public void refreshTaskTableModel() {
        taskTableModel.clear(); //очищаем модель перед наполнением
        rootTask.getChildren().clear(); //отчищаем корневой элемент в таблице
        taskTableModel.addAll(controller.getProject().getTaskList().stream().collect(Collectors.toList())); //заполняем модель
        //из модели пихаем корневой элемент таблицы
        taskTableModel.stream().forEach(new Consumer<ITask>() {
            @Override
            public void accept(ITask taskTableModel) {
                rootTask.getChildren().addAll(new TreeItem<>(taskTableModel));
            }
        });
    }

    public void refreshResourceTableModel() {
        resourceTableModel.clear();
        resourceTableView.getItems().clear();
        resourceTableModel.addAll(controller.getProject().getResourceList().stream().collect(Collectors.toList()));
        resourceTableView.setItems(resourceTableModel);
    }


    private void onClose() {
        //минимум JDK 8u40
        //if (произошли изменения в проекте) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cachoeira");
        alert.setHeaderText("Вы хотите сохранить изменения в Имя проекта?");

        ButtonType saveProjectButtonType = new ButtonType("Сохранить");
        ButtonType dontSaveProjectButtonType = new ButtonType("Не сохранять");
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveProjectButtonType, dontSaveProjectButtonType, cancelButtonType);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == saveProjectButtonType) {
            //сохранение
        } else if (result.get() == dontSaveProjectButtonType) {
            //закрываем программу без сохранения
            System.exit(0);
        }
        //}
    }

    //ивенты при нажании на пункты основного меню
    public void newProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка создания нового проекта
    }

    public void openProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка выбора файла проекта (см. доки по FileChooser'у)
    }

    public void saveProjectMenuItemHandle(ActionEvent actionEvent) {
        //сохранение проекта
    }

    public void exitMenuItemHandle(ActionEvent actionEvent) {
        //если произошли изменения в проекте: открытие диалогового окошка "Сохранить проект? Да Нет Отмена"
        onClose();
    }

    public void addNewResourceHandle(ActionEvent actionEvent) {
        //открытие окошка добавления нового ресурса с помощью кнопки +
        openNewResourceWindow();
    }

    public void addNewTaskHandle(ActionEvent actionEvent) {
        //открытие окошка добавления новой задачи с помощью кнопки +
        openNewTaskWindow();
    }

    private void openNewTaskWindow() {
        UIControl.launchNewTaskWindow(this);
    }

    public void openPropertiesTaskWindow() {
        UIControl.launchPropertiesTaskWindow(this);
    }

    private void openNewResourceWindow() {
        UIControl.launchResourceWindow(this);
    }

    public void openPropertiesResourceWindow() {
        UIControl.launchPropertiesResourceWindow(this);
    }

    public IController getController() {
        return controller;
    }
}
