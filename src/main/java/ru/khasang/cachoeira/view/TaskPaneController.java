package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;
import ru.khasang.cachoeira.model.Task;
import ru.khasang.cachoeira.view.rowfactories.TaskTreeTableViewRowFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

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
    @FXML
    private Button addNewTaskButton;
    @FXML
    private Button removeTaskButton;

    private TaskGanttChart taskGanttChart;
    private IController controller;
    private UIControl uiControl;
    private TreeItem<ITask> rootTask = new TreeItem<>(new Task());

    public TaskPaneController() {
    }

    /**
     * метод initialize исполняется после загрузки fxml файла
     */
    @FXML
    private void initialize() {
        /** Если элемент в таблице не выбран, то кнопка не активна */
        removeTaskButton.disableProperty().bind(taskTreeTableView.getSelectionModel().selectedItemProperty().isNull());
        /** Вешаем иконки на кнопки */
        addNewTaskButton.setGraphic(new ImageView(getClass().getResource("/img/ic_add.png").toExternalForm()));
        removeTaskButton.setGraphic(new ImageView(getClass().getResource("/img/ic_remove.png").toExternalForm()));
        /** Привязываем столбцы к полям в модели**/
        taskNameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());              //столбец задач Наименование
        startDateColumn.setCellValueFactory(param -> param.getValue().getValue().startDateProperty());      //Дата начала
        finishDateColumn.setCellValueFactory(param -> param.getValue().getValue().finishDateProperty());    //Дата окончания
        durationColumn.setCellValueFactory(param -> Bindings.concat(ChronoUnit.DAYS.between(
                param.getValue().getValue().startDateProperty().getValue(),
                param.getValue().getValue().finishDateProperty().getValue()))); // TODO: 02.12.2015 не обновляет в рилтайме, нужно править
        donePercentColumn.setCellValueFactory(param -> param.getValue().getValue().donePercentProperty().asObject());
        priorityColumn.setCellValueFactory(param -> param.getValue().getValue().priorityTypeProperty());
        costColumn.setCellValueFactory(param -> param.getValue().getValue().costProperty().asObject());
        /** Меняем формат даты в столбцах **/
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
    }

    @FXML
    private void addNewTaskHandle(ActionEvent actionEvent) {
        controller.handleAddTask(new Task());
    }

    @FXML
    private void removeTaskHandle(ActionEvent actionEvent) {
        controller.handleRemoveTask(taskTreeTableView.getSelectionModel().getSelectedItem().getValue());
    }

    public void initTaskTable() {
        taskTreeTableView.setRoot(rootTask); //вешаем корневой TreeItem в TreeTableView. Он в fxml стоит как невидимый (<TreeTableView fx:id="taskTreeTableView" showRoot="false">).
        taskTreeTableView.getRoot().setExpanded(true); //делаем корневой элемент расширяемым, т.е. если у TreeItem'а экспэндед стоит тру, то элементы находящиеся в подчинении (children) будут видны, если фолз, то соответственно нет.
        taskTreeTableView.setRowFactory(new TaskTreeTableViewRowFactory(this, controller));
        /** Следим за выделенным элементом в таблице задач **/
        taskTreeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controller.selectedTaskProperty().setValue(newValue.getValue());
            }
        });
        /** Если таблица пуста, присваиваем controller.selectedTaskProperty() null **/
        taskTreeTableView.getRoot().getChildren().addListener((ListChangeListener<TreeItem<ITask>>) c -> {
            while (c.next()) {
                if (c.getList().isEmpty()) {
                    controller.selectedTaskProperty().setValue(null);
                }
            }
        });
        /** Следим за изменениями в модели задач **/
        controller.getProject().getTaskList().addListener((ListChangeListener<ITask>) c -> {
            /** При добавлении или удалении элемента их модели обновлям таблицу задач**/
            while (c.next()) {
                /** Добавляем **/
                for (ITask task : c.getAddedSubList()) {
                    int indexOfTask = controller.getProject().getTaskList().indexOf(task);
                    TreeItem<ITask> taskTreeItem = new TreeItem<>(task);
                    taskTreeTableView.getRoot().getChildren().add(indexOfTask, taskTreeItem); //обязательно нужен инжекс элемента, иначе драгндроп не будет работать
                    taskTreeTableView.getSelectionModel().select(taskTreeItem);
                    taskGanttChart.getTaskPaneObjectsLayer().addTaskBar(task); // Добавляем на диаграмму
                }
                /** Удаляем **/
                for (ITask task : c.getRemoved()) {
                    for (TreeItem<ITask> taskTreeItem : taskTreeTableView.getRoot().getChildren()) {
                        if (taskTreeItem.getValue().equals(task)) {
                            taskTreeTableView.getRoot().getChildren().remove(taskTreeItem);
                            break; //Если убрать - будет ConcurrentModificationException
                        }
                    }
                    taskGanttChart.getTaskPaneObjectsLayer().removeTaskBar(task); // Удаляем с диаграммы
                }
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
        });
    }

    public void initGanttChart() {
        taskGanttChart = new TaskGanttChart(controller, uiControl, 70);
        taskSplitPane.getItems().add(taskGanttChart);
        taskSplitPane.setDividerPosition(0, 0.3);
    }

    public void initContextMenus() {
        /** Контекстное меню для выбора нужных столбцов **/
        ContextMenuColumn contextMenuColumnTask = new ContextMenuColumn(taskTreeTableView);
        contextMenuColumnTask.setOnShowing(event -> contextMenuColumnTask.updateContextMenuColumnTTV(taskTreeTableView));
        for (int i = 0; i < taskTreeTableView.getColumns().size(); i++) {
            taskTreeTableView.getColumns().get(i).setContextMenu(contextMenuColumnTask);
        }

        /** Контекстное меню для таблицы **/
        ContextMenu taskTableMenu = new ContextMenu();
        MenuItem addNewTask = new MenuItem("Новая задача");
        addNewTask.setOnAction(event -> controller.handleAddTask(new Task()));
        taskTableMenu.getItems().addAll(addNewTask);   //заполняем меню
        taskTreeTableView.setContextMenu(taskTableMenu);
    }

    public TreeTableView<ITask> getTaskTreeTableView() {
        return taskTreeTableView;
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
    public TaskGanttChart getTaskGanttChart() {
        return taskGanttChart;
    }
}
