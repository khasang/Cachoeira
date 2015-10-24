package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
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
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.TaskController;
import ru.khasang.cachoeira.controller.ViewController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Task;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

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
    private TableColumn<IResource, String> resourceTypeColumn;      //столбец с типом ресурса <Resource, String>
    @FXML
    private ScrollPane resourceGanttScrollPane;  //здесь должен быть канвас, также возможна с помощью этого скролла получится синхронизировать вертикальные скроллы таблицы ресурсов и ганта

    private Parent root = null;
    private Stage stage;
    private ViewController viewController;
    private TaskController taskController;
    private TreeItem<ITask> rootTask = new TreeItem<>(new Task());  //todo исправить new Task на контроллер
    private ObservableList<ITask> taskTableModel = FXCollections.observableArrayList();        //<Task> модель для задач
    //    ObservableList<IResource> resourceTableModel = FXCollections.observableArrayList();    //<Resource> модель для ресурсов

    public MainWindow(TaskController taskController, ViewController viewController) {
        this.taskController = taskController;
        this.viewController = viewController;

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
//        resourceTableView.setItems(resourceTableModel);

        taskTreeTableView.setRoot(rootTask); //вешаем корневой TreeItem в TreeTableView. Он в fxml стоит как невидимый (<TreeTableView fx:id="taskTreeTableView" showRoot="false">).
        rootTask.setExpanded(true); //делаем корневой элемент расширяемым, т.е. если у TreeItem'а экспэндед стоит тру, то элементы находящиеся в подчинении (children) будут видны, если фолз, то соответственно нет.
        refreshTableModel(); //костыль

        taskNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        startDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Date>(param.getValue().getValue().getStartDate()));
        finishDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Date>(param.getValue().getValue().getFinishDate()));

        //контекстные меню в списках задач и ресурсов todo вынести в отдельный класс
        //контекстное меню на пустом месте таблицы
        ContextMenu tableMenu = new ContextMenu();
        MenuItem addNewTask = new MenuItem("Новая задача");
        addNewTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openNewTaskWindow();
            }
        });
        tableMenu.getItems().addAll(addNewTask);   //заполняем меню
        taskTreeTableView.setContextMenu(tableMenu);

        taskTreeTableView.setRowFactory(new Callback<TreeTableView<ITask>, TreeTableRow<ITask>>() {
            @Override
            public TreeTableRow<ITask> call(TreeTableView<ITask> param) {
                TreeTableRow<ITask> row = new TreeTableRow<>();
                //контекстное меню на пустом месте таблицы
//                ContextMenu tableMenu = new ContextMenu();
//                MenuItem addNewTask = new MenuItem("Новая задача");
//                addNewTask.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        openNewTaskWindow();
//                    }
//                });
//                tableMenu.getItems().addAll(addNewTask);   //заполняем меню
//                taskTreeTableView.setContextMenu(tableMenu);
                //контекстное меню для элементов таблицы
                ContextMenu rowMenu = new ContextMenu();
                MenuItem setResource = new MenuItem("Назначить ресурс");
                MenuItem getProperties = new MenuItem("Свойства");
                MenuItem removeTask = new MenuItem("Удалить задачу");
                setResource.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //назначаем ресурс
                    }
                });
                getProperties.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        taskController.setSelectedTask(row.getTreeItem().getValue());
                        openPropertiesTaskWindow();
                    }
                });
                removeTask.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        taskController.removeTask(row.getTreeItem().getValue());
                        refreshTableModel();
                    }
                });
                rowMenu.getItems().addAll(setResource, getProperties, removeTask);  //заполняем меню

                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))   //если на не пустом месте кликаем,
                                .then(rowMenu)                                  //то выводим одно меню,
                                .otherwise((ContextMenu) null));                    //а если на пустом, то другое
                return row;
            }
        });

//        resourceTableView.setRowFactory(new Callback<TableView, TableRow>() {
//            @Override
//            public TableRow call(TableView param) {
//                TableRow row = new TableRow();
//                ContextMenu rowMenu = new ContextMenu();
//                MenuItem addNewResource = new MenuItem("Новый ресурс");
//                addNewResource.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        //open add resource dialog
//                    }
//                });
//                rowMenu.getItems().addAll(addNewResource);
//                row.contextMenuProperty().bind(Bindings.when(Bindings.isNull(row.itemProperty()))
//                        .then(rowMenu)
//                        .otherwise((ContextMenu) null));
//                return row;
//            }
//        });
    }

    private void openPropertiesTaskWindow() {
        viewController.launchPropertiesTaskWindow(this);
    }


    @Override
    public Stage getStage() {
        return stage;
    }

    public void refreshTableModel() {
        taskTableModel.clear(); //очищаем модель перед наполнением
        rootTask.getChildren().clear(); //отчищаем корневой элемент в таблице
        //заполняем модель
        for (ITask task : taskController.getTasks()) {
            taskTableModel.add(task);
        }
        //из модели пихаем корневой элемент таблицы
        taskTableModel.stream().forEach(new Consumer<ITask>() {
            @Override
            public void accept(ITask taskTableModel) {
                rootTask.getChildren().addAll(new TreeItem<>(taskTableModel));
            }
        });
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
        //отрытие окошка добавления нового ресурса
    }

    public void addNewTaskHandle(ActionEvent actionEvent) {
        openNewTaskWindow();
    }

    public void openNewTaskWindow() {
//        TaskWindow taskWindow = new TaskWindow(this); //todo переделать на контроллер
//        taskWindow.launch();
        viewController.launchNewTaskWindow(this);
    }
}
