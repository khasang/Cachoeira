package ru.khasang.cachoeira.viewcontroller.mainwindow;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.project.AddTaskToProjectCommand;
import ru.khasang.cachoeira.commands.project.RemoveTaskFromProjectCommand;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;
import ru.khasang.cachoeira.model.Task;
import ru.khasang.cachoeira.viewcontroller.UIControl;
import ru.khasang.cachoeira.viewcontroller.mainwindow.contextmenus.ColumnContextMenu;
import ru.khasang.cachoeira.viewcontroller.mainwindow.ganttplan.GanttPlan;
import ru.khasang.cachoeira.viewcontroller.mainwindow.ganttplan.TaskGanttPlan;
import ru.khasang.cachoeira.viewcontroller.mainwindow.rowfactories.TaskTreeTableViewRowFactory;
import ru.khasang.cachoeira.viewcontroller.mainwindow.tables.TaskTreeTableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Класс в котором описывается все что находится на вкладке Задачи.
 */
public class TaskPaneController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPaneController.class.getName());

    @FXML
    private SplitPane taskSplitPane;
    @FXML
    private TaskTreeTableView<ITask> taskTreeTableView;     //таблица задач <Task>
    @FXML
    private TreeTableColumn<ITask, String> nameColumn;      //столбец с наименованием задачи <Task, String>
    @FXML
    private TreeTableColumn<ITask, LocalDate> startDateColumn;     //столбец с датой начала задачи <Task, Date>
    @FXML
    private TreeTableColumn<ITask, LocalDate> finishDateColumn;    //столбец с датой окончания задачи <Task, Date>
    @FXML
    private TreeTableColumn<ITask, Integer> durationColumn; //столбец Продолжительность
    @FXML
    private TreeTableColumn<ITask, Integer> donePercentColumn; //столбец процент выполнения
    @FXML
    private TreeTableColumn<ITask, PriorityType> priorityColumn; //столбец Приоритет
    @FXML
    private TreeTableColumn<ITask, Double> costColumn; //столбец Стоимость
    @FXML
    private ScrollBar taskTreeTableViewHorizontalScrollBar;
    @FXML
    private Button addNewTaskButton;
    @FXML
    private Button removeTaskButton;
    @FXML
    private Slider zoomSlider;

    private GanttPlan ganttPlan;
    private UIControl uiControl;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<ITask>> selectedTreeItemChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<TreeItem<ITask>> treeItemsChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;

    public TaskPaneController() {
    }

    /**
     * Метод initialize исполняется после загрузки fxml файла
     */
    @FXML
    private void initialize() {
        // Если элемент в таблице не выбран, то кнопка не активна
        removeTaskButton.disableProperty().bind(taskTreeTableView.getSelectionModel().selectedItemProperty().isNull());
        // Вешаем иконки на кнопки
        addNewTaskButton.setGraphic(new ImageView(getClass().getResource("/img/ic_add.png").toExternalForm()));
        removeTaskButton.setGraphic(new ImageView(getClass().getResource("/img/ic_remove.png").toExternalForm()));
        // Уменьшаем толщину разделителя
        taskSplitPane.getStylesheets().add(this.getClass().getResource("/css/split-pane.css").toExternalForm());
        // Привязываем столбцы к полям в модели
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().nameProperty());              //столбец задач Наименование
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().startDateProperty());      //Дата начала
        finishDateColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().finishDateProperty());    //Дата окончания
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().durationProperty().asObject());
        donePercentColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().donePercentProperty().asObject());
        priorityColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().priorityTypeProperty());
        costColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().costProperty().asObject());
        // Форматируем столбцы с датами
        startDateColumn.setCellFactory(column -> getTreeTableCell());
        finishDateColumn.setCellFactory(column -> getTreeTableCell());
        // Высота строк и выравнивание
        taskTreeTableView.setFixedCellSize(31);
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        durationColumn.setStyle("-fx-alignment: CENTER-LEFT");
        donePercentColumn.setStyle("-fx-alignment: CENTER-LEFT");
        priorityColumn.setStyle("-fx-alignment: CENTER-LEFT");
        costColumn.setStyle("-fx-alignment: CENTER-LEFT");
    }

    private TreeTableCell<ITask, LocalDate> getTreeTableCell() {
        return new TreeTableCell<ITask, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(item);
                    setText(dateFormatter);
                }
            }
        };
    }

    /**
     * Метод срабатывающий при нажатии на кнопку "Добавить задачу".
     */
    @FXML
    private void addNewTaskHandle(ActionEvent actionEvent) {
//        uiControl.getController().handleAddTask(new Task());
        CommandControl.getInstance().execute(new AddTaskToProjectCommand(uiControl.getController().getProject(), new Task()));
    }

    /**
     * Метод срабатывающий при нажатии на кнопку "Удалить задачу".
     */
    @FXML
    private void removeTaskHandle(ActionEvent actionEvent) {
//        uiControl.getController().handleRemoveTask(taskTreeTableView.getSelectionModel().getSelectedItem().getValue());
        CommandControl.getInstance().execute(new RemoveTaskFromProjectCommand(uiControl.getController().getProject(), uiControl.getController().getSelectedTask()));
    }

    public void initTaskTable(UIControl uiControl) {
        taskTreeTableView.bindScrollsToController(uiControl);
        taskTreeTableView.setRoot(new TreeItem<>(new Task()));
        // Делаем корневой элемент расширяемым, т.е. если у TreeItem'а экспэндед стоит тру, то элементы находящиеся в подчинении (children) будут видны, если фолз, то соответственно нет.
        taskTreeTableView.getRoot().setExpanded(true);
        taskTreeTableView.setRowFactory(new TaskTreeTableViewRowFactory(this, uiControl.getController()));

        // Временное решение для синхронизации таблицы и диаграммы.
        // Добавил собственный горизонтальный скролл за вместо скролла таблицы (который скрыл, см. AbstractTableView),
        // чтобы он был всегда видимый, пока не придумаю более изящное решение.
        taskTreeTableViewHorizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
        taskTreeTableViewHorizontalScrollBar.visibleAmountProperty().bind(taskTreeTableView.widthProperty());
        taskTreeTableViewHorizontalScrollBar.valueProperty().bindBidirectional(uiControl.taskHorizontalScrollValueProperty());
        LOGGER.debug("Таблица задач проинициализирована.");
    }

    public void refreshTableView(UIControl uiControl) {
        // При загрузке проверяем наличие задач
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            TreeItem<ITask> treeItem = new TreeItem<>(task);
            taskTreeTableView.getRoot().getChildren().add(treeItem);
            taskTreeTableView.getSelectionModel().select(treeItem);
//            uiControl.getController().setSelectedTask(treeItem.getValue());
        }
    }

    public void setListeners(UIControl uiControl) {
        selectedTreeItemChangeListener = (observable, oldSelectedTreeItem, newSelectedTreeItem) -> {
            if (newSelectedTreeItem != null) {
                uiControl.getController().setSelectedTask(newSelectedTreeItem.getValue());
            }
        };
        treeItemsChangeListener = change -> {
            while (change.next()) {
                if (change.getList().isEmpty()) {
                    uiControl.getController().setSelectedTask(null);
                }
            }
        };
        taskListChangeListener = change -> {
            // При добавлении или удалении элемента из модели обновляем таблицу задач
            while (change.next()) {
                // Добавляем
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(task -> {
                        // Получаем индекс задачи
                        int indexOfTask = uiControl.getController().getProject().getTaskList().indexOf(task);
                        // Создаем элемент таблицы и присваиваем ему нашу задачу
                        TreeItem<ITask> taskTreeItem = new TreeItem<>(task);
                        // Добавляем элемент в таблицу на нужную строку (indexOfTask)
                        taskTreeTableView.getRoot().getChildren().add(indexOfTask, taskTreeItem); //обязательно нужен индекс элемента, иначе драгндроп не будет работать
                        // Выделяем добавленный элемент в таблице
                        taskTreeTableView.getSelectionModel().select(taskTreeItem);
                        // Добавляем задачу на диаграмму
                        ganttPlan.getObjectsLayer().addTaskBar(task, null);
                    });
                }
                // Удаляем
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(task -> {
                        // Сначала удаляем из таблицы...
                        removeTaskTreeItem(task, taskTreeTableView.getRoot().getChildren());
                        // ...а теперь с диаграммы
                        ganttPlan.getObjectsLayer().removeTaskBar(task);
                        ganttPlan.getSelectedObjectLayer().removeBackgroundTaskBar(task);
                    });
                }
            }
        };

        // Следим за выделенным элементом в таблице задач
        taskTreeTableView.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<>(selectedTreeItemChangeListener));
        // Если таблица пуста, присваиваем controller.selectedTaskProperty() null
        taskTreeTableView.getRoot().getChildren().addListener(new WeakListChangeListener<>(treeItemsChangeListener));
        // Следим за изменениями в модели задач
        uiControl.getController().getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));
    }

    /**
     * Спомощью этого метода удаляем элементы из таблицы.
     *
     * @param task             Спомощью этой задачи находим элемент к которому она присвоена.
     * @param taskTreeItemList Список таблицы из которого необходимо удалить нужный элемент.
     */
    public void removeTaskTreeItem(ITask task, ObservableList<TreeItem<ITask>> taskTreeItemList) {
        taskTreeItemList.removeIf(taskTreeItem -> taskTreeItem.getValue().equals(task));
    }

    public void initGanttChart(UIControl uiControl) {
        ganttPlan = new TaskGanttPlan();
        ganttPlan.initGanttDiagram(uiControl);
        taskSplitPane.getItems().add(ganttPlan);
        taskSplitPane.setDividerPosition(0, 0.3);
        // Связываем разделитель таблицы и диаграммы на вкладке Задачи с разделителем на вкладке Ресурсы
        taskSplitPane.getDividers().get(0).positionProperty().bindBidirectional(uiControl.splitPaneDividerValueProperty());
        LOGGER.debug("Диаграмма Ганта проинициализирована.");
    }

    public void initContextMenus(UIControl uiControl) {
        // Контекстное меню для выбора нужных столбцов
        ColumnContextMenu columnContextMenuTask = new ColumnContextMenu(taskTreeTableView);
        columnContextMenuTask.setOnShowing(event -> columnContextMenuTask.updateContextMenuColumnTTV(taskTreeTableView));
        taskTreeTableView.getColumns().forEach(column -> column.setContextMenu(columnContextMenuTask));

        // Контекстное меню для таблицы
        ContextMenu taskTableMenu = new ContextMenu();
        MenuItem addNewTask = new MenuItem("Новая задача");
        addNewTask.setOnAction(event -> CommandControl.getInstance().execute(new AddTaskToProjectCommand(uiControl.getController().getProject(), new Task())));
        taskTableMenu.getItems().addAll(addNewTask);   //заполняем меню
        taskTreeTableView.setContextMenu(taskTableMenu);
    }

    public void initZoom(UIControl uiControl) {
        zoomSlider.valueProperty().bindBidirectional(uiControl.zoomMultiplierProperty());
    }

    public TreeTableView<ITask> getTaskTreeTableView() {
        return taskTreeTableView;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }

    public GanttPlan getGanttPlan() {
        return ganttPlan;
    }
}