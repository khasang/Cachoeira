package ru.khasang.cachoeira.view;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;
import ru.khasang.cachoeira.model.Task;
import ru.khasang.cachoeira.view.contextmenus.ContextMenuColumn;
import ru.khasang.cachoeira.view.rowfactories.TaskTreeTableViewRowFactory;
import ru.khasang.cachoeira.view.tables.TaskTreeTableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
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

    private TaskGanttChart taskGanttChart;
    private UIControl uiControl;

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
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());              //столбец задач Наименование
        startDateColumn.setCellValueFactory(param -> param.getValue().getValue().startDateProperty());      //Дата начала
        finishDateColumn.setCellValueFactory(param -> param.getValue().getValue().finishDateProperty());    //Дата окончания
        durationColumn.setCellValueFactory(param -> param.getValue().getValue().durationProperty().asObject());
        donePercentColumn.setCellValueFactory(param -> param.getValue().getValue().donePercentProperty().asObject());
        priorityColumn.setCellValueFactory(param -> param.getValue().getValue().priorityTypeProperty());
        costColumn.setCellValueFactory(param -> param.getValue().getValue().costProperty().asObject());
        // Форматируем столбцы с датами
        startDateColumn.setCellFactory(new Callback<TreeTableColumn<ITask, LocalDate>, TreeTableCell<ITask, LocalDate>>() {
            @Override
            public TreeTableCell<ITask, LocalDate> call(TreeTableColumn<ITask, LocalDate> param) {
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
        });
        finishDateColumn.setCellFactory(new Callback<TreeTableColumn<ITask, LocalDate>, TreeTableCell<ITask, LocalDate>>() {
            @Override
            public TreeTableCell<ITask, LocalDate> call(TreeTableColumn<ITask, LocalDate> param) {
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
        });
        // Высота строк и выравнивание
        taskTreeTableView.setFixedCellSize(31);
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        durationColumn.setStyle("-fx-alignment: CENTER-LEFT");
        donePercentColumn.setStyle("-fx-alignment: CENTER-LEFT");
        priorityColumn.setStyle("-fx-alignment: CENTER-LEFT");
        costColumn.setStyle("-fx-alignment: CENTER-LEFT");
    }

    /**
     * Метод срабатывающий при нажатии на кнопку "Добавить задачу".
     */
    @FXML
    private void addNewTaskHandle(ActionEvent actionEvent) {
        uiControl.getController().handleAddTask(new Task());
    }

    /**
     * Метод срабатывающий при нажатии на кнопку "Удалить задачу".
     */
    @FXML
    private void removeTaskHandle(ActionEvent actionEvent) {
        uiControl.getController().handleRemoveTask(taskTreeTableView.getSelectionModel().getSelectedItem().getValue());
    }

    public void initTaskTable() {
        taskTreeTableView.bindScrollsToController(uiControl);
        taskTreeTableView.setRoot(new TreeItem<>(new Task()));
        taskTreeTableView.getRoot().setExpanded(true); //делаем корневой элемент расширяемым, т.е. если у TreeItem'а экспэндед стоит тру, то элементы находящиеся в подчинении (children) будут видны, если фолз, то соответственно нет.
        taskTreeTableView.setRowFactory(new TaskTreeTableViewRowFactory(this, uiControl.getController()));
        // Временное решение для синхронизации таблицы и диаграммы.
        // Добавил собственный горизонтальный скролл за вместо скролла таблицы (который скрыл, см. TaskTreeTableView),
        // чтобы он был всегда видимый, пока не придумаю более изящное решение.
        taskTreeTableViewHorizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
        taskTreeTableViewHorizontalScrollBar.visibleAmountProperty().bind(taskTreeTableView.widthProperty());
        taskTreeTableViewHorizontalScrollBar.valueProperty().bindBidirectional(uiControl.taskHorizontalScrollValueProperty());
        // Следим за выделенным элементом в таблице задач
        taskTreeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldSelectedTreeItem, newSelectedTreeItem) -> {
            if (newSelectedTreeItem != null) {
                uiControl.getController().setSelectedTask(newSelectedTreeItem.getValue());
            }
        });
        // Если таблица пуста, присваиваем controller.selectedTaskProperty() null
        taskTreeTableView.getRoot().getChildren().addListener((ListChangeListener<TreeItem<ITask>>) change -> {
            while (change.next()) {
                if (change.getList().isEmpty()) {
                    uiControl.getController().setSelectedTask(null);
                }
            }
        });
        // Следим за изменениями в модели задач
        uiControl.getController().getProject().getTaskList().addListener((ListChangeListener<ITask>) change -> {
            // При добавлении или удалении элемента из модели обновляем таблицу задач
            while (change.next()) {
                // Добавляем
                for (ITask task : change.getAddedSubList()) {
                    // Получаем индекс задачи
                    int indexOfTask = uiControl.getController().getProject().getTaskList().indexOf(task);
                    // Создаем элемент таблицы и присваиваем ему нашу задачу
                    TreeItem<ITask> taskTreeItem = new TreeItem<>(task);
                    // Добавляем элемент в таблицу на нужную строку (indexOfTask)
                    taskTreeTableView.getRoot().getChildren().add(indexOfTask, taskTreeItem); //обязательно нужен индекс элемента, иначе драгндроп не будет работать
                    // Выделяем добавленный элемент в таблице
                    taskTreeTableView.getSelectionModel().select(taskTreeItem);
                    // Добавляем задачу на диаграмму
                    taskGanttChart.getTaskPaneObjectsLayer().addTaskBar(task);
                }
                // Удаляем
                for (ITask task : change.getRemoved()) {
                    // Сначала удаляем из таблицы...
                    removeTaskTreeItem(task, taskTreeTableView.getRoot().getChildren());
                    // ...а теперь с диаграммы
                    taskGanttChart.getTaskPaneObjectsLayer().removeTaskBar(task);
                }
            }
        });
        LOGGER.debug("Таблица задач проинициализирована.");
    }

    /**
     * Спомощью этого метода удаляем элементы из таблицы.
     *
     * @param task             Спомощью этой задачи находим элемент к которому она присвоена.
     * @param taskTreeItemList Список таблицы из которого необходимо удалить нужный элемент.
     */
    public void removeTaskTreeItem(ITask task, ObservableList<TreeItem<ITask>> taskTreeItemList) {
        Iterator<TreeItem<ITask>> taskTreeItemListIterator = taskTreeItemList.iterator();
        while (taskTreeItemListIterator.hasNext()) {
            TreeItem<ITask> taskTreeItem = taskTreeItemListIterator.next();
            if (taskTreeItem.getValue().equals(task)) {
                taskTreeItemListIterator.remove();
            }
        }
    }

    public void initGanttChart() {
        taskGanttChart = new TaskGanttChart();
        taskGanttChart.initGanttDiagram(uiControl);
        taskSplitPane.getItems().add(taskGanttChart);
        taskSplitPane.setDividerPosition(0, 0.3);
        //Связываем разделитель таблицы и диаграммы на вкладке Задачи с разделителем на вкладке Ресурсы
        taskSplitPane.getDividers().get(0).positionProperty().bindBidirectional(uiControl.splitPaneDividerValueProperty());
        LOGGER.debug("Диаграмма Ганта проинициализирована.");
    }

    public void initContextMenus() {
        // Контекстное меню для выбора нужных столбцов
        ContextMenuColumn contextMenuColumnTask = new ContextMenuColumn(taskTreeTableView);
        contextMenuColumnTask.setOnShowing(event -> contextMenuColumnTask.updateContextMenuColumnTTV(taskTreeTableView));
        for (int i = 0; i < taskTreeTableView.getColumns().size(); i++) {
            taskTreeTableView.getColumns().get(i).setContextMenu(contextMenuColumnTask);
        }

        // Контекстное меню для таблицы
        ContextMenu taskTableMenu = new ContextMenu();
        MenuItem addNewTask = new MenuItem("Новая задача");
        addNewTask.setOnAction(event -> uiControl.getController().handleAddTask(new Task()));
        taskTableMenu.getItems().addAll(addNewTask);   //заполняем меню
        taskTreeTableView.setContextMenu(taskTableMenu);
    }

    public void initZoom() {
        zoomSlider.valueProperty().bindBidirectional(uiControl.zoomMultiplierProperty());
    }

    public TreeTableView<ITask> getTaskTreeTableView() {
        return taskTreeTableView;
    }

    public TaskGanttChart getTaskGanttChart() {
        return taskGanttChart;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}