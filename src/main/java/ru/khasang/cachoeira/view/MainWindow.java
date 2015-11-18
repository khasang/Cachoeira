package ru.khasang.cachoeira.view;

import javafx.collections.ListChangeListener;
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
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.view.rowfactories.ResourceTableViewRowFactory;
import ru.khasang.cachoeira.view.rowfactories.TaskTreeTableViewRowFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by truesik on 28.09.2015.
 */
public class MainWindow implements IWindow {
    @FXML
    private SplitPane taskSplitPane;
    @FXML
    private SplitPane resourceSplitPane;
    @FXML
    private TreeTableView<ITask> taskTreeTableView;     //таблица задач <Task>
    @FXML
    private TreeTableColumn<ITask, String> taskNameColumn;      //столбец с наименованием задачи <Task, String>
    @FXML
    private TreeTableColumn<ITask, Date> finishDateColumn;    //столбец с датой окончания задачи <Task, Date>
    @FXML
    private TreeTableColumn<ITask, Date> startDateColumn;     //столбец с датой начала задачи <Task, Date>
    @FXML
    private TreeTableColumn<ITask, String> durationColumn; //столбец Продолжительность
    @FXML
    private TreeTableColumn<ITask, Integer> donePercentColumn; //столбец процент выполения
    @FXML
    private TreeTableColumn<ITask, PriorityType> priorityColumn; //столбец Приоритет
    @FXML
    private TreeTableColumn<ITask, Double> costColumn; //столбец Стоимость
    @FXML
    private TableView<IResource> resourceTableView;         //таблица ресурсов <Resource>
    @FXML
    private TableColumn<IResource, String> resourceNameColumn;      //стоблец с наименованием ресурса <Resource, String>
    @FXML
    private TableColumn<IResource, ResourceType> resourceTypeColumn;      //столбец с типом ресурса <Resource, String>
    @FXML
    private TableColumn<IResource, String> resourceEmailColumn;
    @FXML
    private ScrollPane resourceGanttScrollPane;  //здесь должен быть канвас, также возможна с помощью этого скролла получится синхронизировать вертикальные скроллы таблицы ресурсов и ганта

    private GanttChart taskGanttChart;
    private GanttChart resourceGanttChart;
    private Parent root = null;
    private Stage stage;
    private UIControl UIControl;
    private IController controller;
    private TreeItem<ITask> rootTask = new TreeItem<>(new Task());  //todo исправить new Task на контроллер

    public MainWindow(IController controller, UIControl UIControl) {
        this.controller = controller;
        this.UIControl = UIControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));    //грузим макет окна
        fxmlLoader.setController(this);                                                             //говорим макету, что этот класс является его контроллером
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
        stage.show();
        stage.setTitle(controller.getProject().getName());

        taskGanttChart = new GanttChart(controller, UIControl, 70);
        taskSplitPane.getItems().add(taskGanttChart);
        taskSplitPane.setDividerPosition(0, 0.3);

        resourceGanttChart = new GanttChart(controller, UIControl, 70);
        resourceSplitPane.getItems().add(resourceGanttChart);
        resourceSplitPane.setDividerPosition(0, 0.3);

        //при нажатии на крестик в тайтле
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
//                onClose();
//              if (произошли изменения в проекте) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cachoeira");
                alert.setHeaderText("Вы хотите сохранить изменения в " + controller.getProject().getName() + "?");

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
        controller.getProject().getTaskList().addListener(new ListChangeListener<ITask>() {
            @Override
            public void onChanged(Change<? extends ITask> c) {
                refreshTaskTreeTableView();
                taskGanttChart.getObjectsLayer().refreshTaskDiagram();
                resourceGanttChart.getObjectsLayer().refreshResourceDiagram();
                while (c.next()) {
                    if (c.wasAdded()) {
                        System.out.println("Main Window Task Added!");
                    }
                    if (c.wasRemoved()) {
                        System.out.println("Main Window Task Removed");
                    }
                    if (c.wasReplaced()) {
                        System.out.println("Main Window Task Replaced");
                    }
                    if (c.wasUpdated()) {
                        System.out.println("Main Window Task Updated");
                    }
                }
            }
        });
        controller.getProject().getResourceList().addListener(new ListChangeListener<IResource>() {
            @Override
            public void onChanged(Change<? extends IResource> c) {
                resourceGanttChart.getObjectsLayer().refreshResourceDiagram();
                taskGanttChart.getObjectsLayer().refreshTaskDiagram();
                while (c.next()) {
                    if (c.wasAdded()) {
                        System.out.println("Main Window Resource Added!");
                    }
                    if (c.wasRemoved()) {
                        System.out.println("Main Window Resource Removed");
                    }
                    if (c.wasReplaced()) {
                        System.out.println("Main Window Resource Replaced");
                    }
                    if (c.wasUpdated()) {
                        System.out.println("Main Window Resource Updated");
                    }
                }
            }
        });

        resourceTableView.setItems(controller.getProject().getResourceList());

        taskNameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());              //столбец задач Наименование
        startDateColumn.setCellValueFactory(param -> param.getValue().getValue().startDateProperty());      //Дата начала
        finishDateColumn.setCellValueFactory(param -> param.getValue().getValue().finishDateProperty());    //Дата окончания
        durationColumn.setCellValueFactory(param -> param.getValue().getValue().durationProperty());
        donePercentColumn.setCellValueFactory(param -> param.getValue().getValue().donePercentProperty().asObject());
        priorityColumn.setCellValueFactory(param -> param.getValue().getValue().priorityTypeProperty());
        costColumn.setCellValueFactory(param -> param.getValue().getValue().costProperty().asObject());

        resourceNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());                     //столбец ресурсов Наименование
        resourceTypeColumn.setCellValueFactory(param -> param.getValue().resourceTypeProperty());                   //Тип
        resourceEmailColumn.setCellValueFactory(param -> param.getValue().emailProperty());                   //Почта

        //my ContextMenuColumn
        // contextMenuColumn for Task
        ContextMenuColumn contextMenuColumnTask = new ContextMenuColumn(taskTreeTableView);
        contextMenuColumnTask.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                contextMenuColumnTask.updateContextMenuColumnTTV(taskTreeTableView);
            }
        });
        for (int i = 0; i < taskTreeTableView.getColumns().size(); i++) {
            taskTreeTableView.getColumns().get(i).setContextMenu(contextMenuColumnTask);
        }
//
//         contextMenuColumn for Resource
        ContextMenuColumn contextMenuColumnResource = new ContextMenuColumn(resourceTableView);
        contextMenuColumnResource.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                contextMenuColumnResource.updateContextMenuColumnTV(resourceTableView);
            }
        });
        for (int i = 0; i < resourceTableView.getColumns().size(); i++) {
            resourceTableView.getColumns().get(i).setContextMenu(contextMenuColumnResource);
        }

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

        resourceTableView.setRowFactory(new ResourceTableViewRowFactory(this, controller)); //вешаем драг и дроп, и контекстное меню
        taskTreeTableView.setRowFactory(new TaskTreeTableViewRowFactory(this, controller));
    }

    private void refreshTaskTreeTableView() {
        rootTask.getChildren().clear();
        controller.getProject().getTaskList().stream().forEach(new Consumer<ITask>() {
            @Override
            public void accept(ITask iTask) {
                rootTask.getChildren().add(new TreeItem<>(iTask));
            }
        });
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    private void onClose() {
        //минимум JDK 8u40
        //if (произошли изменения в проекте) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cachoeira");
        alert.setHeaderText("Вы хотите сохранить изменения в " + controller.getProject().getName() + "?");

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
    @FXML
    private void newProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка создания нового проекта
    }

    @FXML
    private void openProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка выбора файла проекта (см. доки по FileChooser'у)
    }

    @FXML
    private void saveProjectMenuItemHandle(ActionEvent actionEvent) {
        //сохранение проекта
    }

    @FXML
    private void exitMenuItemHandle(ActionEvent actionEvent) {
        //если произошли изменения в проекте: открытие диалогового окошка "Сохранить проект? Да Нет Отмена"
        onClose();
    }

    @FXML
    private void addNewResourceHandle(ActionEvent actionEvent) {
        //открытие окошка добавления нового ресурса с помощью кнопки +
        openNewResourceWindow();
    }

    @FXML
    private void addNewTaskHandle(ActionEvent actionEvent) {
        //открытие окошка добавления новой задачи с помощью кнопки +
        openNewTaskWindow();
    }

    private void openNewTaskWindow() {
        UIControl.launchNewTaskWindow();
    }

    public void openPropertiesTaskWindow() {
        UIControl.launchPropertiesTaskWindow();
    }

    private void openNewResourceWindow() {
        UIControl.launchResourceWindow();
    }

    public void openPropertiesResourceWindow() {
        UIControl.launchPropertiesResourceWindow();
    }

    public IController getController() {
        return controller;
    }

    public TreeTableView<ITask> getTaskTreeTableView() {
        return taskTreeTableView;
    }

    public TableView<IResource> getResourceTableView() {
        return resourceTableView;
    }

    public GanttChart getResourceGanttChart() {
        return resourceGanttChart;
    }

    public GanttChart getTaskGanttChart() {
        return taskGanttChart;
    }
}
