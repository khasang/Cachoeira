package ru.khasang.cachoeira.view;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;
import ru.khasang.cachoeira.model.Task;
import ru.khasang.cachoeira.view.rowfactories.TaskTreeTableViewRowFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Created by truesik on 25.11.2015.
 */
public class TaskPaneController {
    @FXML
    private SplitPane taskSplitPane;
    @FXML
    private TreeTableView<ITask> taskTreeTableView;     //таблица задач <Task>
    @FXML
    private TreeTableColumn<ITask, String> taskNameColumn;      //столбец с наименованием задачи <Task, String>
    @FXML
    private TreeTableColumn<ITask, LocalDate> finishDateColumn;    //столбец с датой окончания задачи <Task, Date>
    @FXML
    private TreeTableColumn<ITask, LocalDate> startDateColumn;     //столбец с датой начала задачи <Task, Date>
    @FXML
    private TreeTableColumn<ITask, String> durationColumn; //столбец Продолжительность
    @FXML
    private TreeTableColumn<ITask, Integer> donePercentColumn; //столбец процент выполения
    @FXML
    private TreeTableColumn<ITask, PriorityType> priorityColumn; //столбец Приоритет
    @FXML
    private TreeTableColumn<ITask, Double> costColumn; //столбец Стоимость

    private GanttChart taskGanttChart;
    private UIControl UIControl;
    private IController controller;
    private TreeItem<ITask> rootTask = new TreeItem<>(new Task());  //todo исправить new Task на контроллер

    public TaskPaneController() {
        taskGanttChart = new GanttChart(controller, UIControl, 70);
        taskSplitPane.getItems().add(taskGanttChart);
        taskSplitPane.setDividerPosition(0, 0.3);

        taskTreeTableView.setRoot(rootTask); //вешаем корневой TreeItem в TreeTableView. Он в fxml стоит как невидимый (<TreeTableView fx:id="taskTreeTableView" showRoot="false">).
        rootTask.setExpanded(true); //делаем корневой элемент расширяемым, т.е. если у TreeItem'а экспэндед стоит тру, то элементы находящиеся в подчинении (children) будут видны, если фолз, то соответственно нет.
        controller.getProject().getTaskList().addListener(new ListChangeListener<ITask>() {
            @Override
            public void onChanged(Change<? extends ITask> c) {
                taskGanttChart.getObjectsLayer().refreshTaskDiagram();
                while (c.next()) {
                    if (c.wasAdded()) {
                        System.out.println("Main Window Task Added!");
                        refreshTaskTreeTableView();
                    }
                    if (c.wasRemoved()) {
                        System.out.println("Main Window Task Removed");
                        refreshTaskTreeTableView();
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

        taskNameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());              //столбец задач Наименование
        startDateColumn.setCellValueFactory(param -> param.getValue().getValue().startDateProperty());      //Дата начала
        startDateColumn.setCellFactory(column -> new TreeTableCell<ITask, LocalDate>() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) {
                    String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(item);
                    this.setText(dateFormatter);
                }
            }
        });
        finishDateColumn.setCellValueFactory(param -> param.getValue().getValue().finishDateProperty());    //Дата окончания
        finishDateColumn.setCellFactory(column -> new TreeTableCell<ITask, LocalDate>() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) {
                    String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(item);
                    this.setText(dateFormatter);
                }
            }
        });
        durationColumn.setCellValueFactory(param -> param.getValue().getValue().durationProperty());
        donePercentColumn.setCellValueFactory(param -> param.getValue().getValue().donePercentProperty().asObject());
        priorityColumn.setCellValueFactory(param -> param.getValue().getValue().priorityTypeProperty());
        costColumn.setCellValueFactory(param -> param.getValue().getValue().costProperty().asObject());

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

        //контекстные меню в списках задач и ресурсов
        //контекстное меню на пустом месте таблицы
        ContextMenu taskTableMenu = new ContextMenu();
        MenuItem addNewTask = new MenuItem("Новая задача");
        addNewTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                openNewTaskWindow();
            }
        });
        taskTableMenu.getItems().addAll(addNewTask);   //заполняем меню
        taskTreeTableView.setContextMenu(taskTableMenu);

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

    @FXML
    private void addNewTaskHandle(ActionEvent actionEvent) {
        //открытие окошка добавления новой задачи с помощью кнопки +
//        openNewTaskWindow();
    }

    public TreeTableView<ITask> getTaskTreeTableView() {
        return taskTreeTableView;
    }
}
