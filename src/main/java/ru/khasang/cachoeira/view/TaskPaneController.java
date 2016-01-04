package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
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
import java.time.temporal.ChronoUnit;
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
    private TreeTableColumn<ITask, String> durationColumn; //столбец Продолжительность
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
     * метод initialize исполняется после загрузки fxml файла
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
        durationColumn.setCellValueFactory(param -> Bindings.concat(ChronoUnit.DAYS.between(
                param.getValue().getValue().startDateProperty().getValue(),
                param.getValue().getValue().finishDateProperty().getValue()))); // TODO: 02.12.2015 не обновляет в рилтайме, нужно править
        donePercentColumn.setCellValueFactory(param -> param.getValue().getValue().donePercentProperty().asObject());
        priorityColumn.setCellValueFactory(param -> param.getValue().getValue().priorityTypeProperty());
        costColumn.setCellValueFactory(param -> param.getValue().getValue().costProperty().asObject());
        // Высота строк и выравнивание
        taskTreeTableView.setFixedCellSize(31);
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        durationColumn.setStyle("-fx-alignment: CENTER-LEFT");
        donePercentColumn.setStyle("-fx-alignment: CENTER-LEFT");
        priorityColumn.setStyle("-fx-alignment: CENTER-LEFT");
        costColumn.setStyle("-fx-alignment: CENTER-LEFT");
    }

    @FXML
    private void addNewTaskHandle(ActionEvent actionEvent) {
        uiControl.getController().handleAddTask(new Task());
    }

    @FXML
    private void removeTaskHandle(ActionEvent actionEvent) {
        uiControl.getController().handleRemoveTask(taskTreeTableView.getSelectionModel().getSelectedItem().getValue());
    }

    public void initTaskTable() {
        taskTreeTableView.bindScrollsToController(uiControl);
        taskTreeTableView.setRoot(new TreeItem<>(new Task()));
        taskTreeTableView.getRoot().setExpanded(true); //делаем корневой элемент расширяемым, т.е. если у TreeItem'а экспэндед стоит тру, то элементы находящиеся в подчинении (children) будут видны, если фолз, то соответственно нет.
        taskTreeTableView.setRowFactory(new TaskTreeTableViewRowFactory(this, uiControl.getController()));
        // Делаем поля таблицы редактируемыми
        setTableEditable();
        // Временное решение для синхронизации таблицы и диаграммы.
        // Добавил собственный горизонтальный скролл за вместо скролла таблицы (который скрыл, см. TaskTreeTableView),
        // чтобы он был всегда видимый, пока не придумаю более изящное решение.
        taskTreeTableViewHorizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
        taskTreeTableViewHorizontalScrollBar.visibleAmountProperty().bind(taskTreeTableView.widthProperty());
        taskTreeTableViewHorizontalScrollBar.valueProperty().bindBidirectional(uiControl.taskHorizontalScrollValueProperty());
        // Следим за выделенным элементом в таблице задач
        taskTreeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                uiControl.getController().selectedTaskProperty().setValue(newValue.getValue());
            }
        });
        // Если таблица пуста, присваиваем controller.selectedTaskProperty() null
        taskTreeTableView.getRoot().getChildren().addListener((ListChangeListener<TreeItem<ITask>>) change -> {
            while (change.next()) {
                if (change.getList().isEmpty()) {
                    uiControl.getController().selectedTaskProperty().setValue(null);
                }
            }
        });
        // Следим за изменениями в модели задач
        uiControl.getController().getProject().getTaskList().addListener((ListChangeListener<ITask>) change -> {
            // При добавлении или удалении элемента из модели обновляем таблицу задач
            while (change.next()) {
                // Добавляем
                for (ITask task : change.getAddedSubList()) {
                    int indexOfTask = uiControl.getController().getProject().getTaskList().indexOf(task);
                    TreeItem<ITask> taskTreeItem = new TreeItem<>(task);
                    taskTreeTableView.getRoot().getChildren().add(indexOfTask, taskTreeItem); //обязательно нужен индекс элемента, иначе драгндроп не будет работать
                    taskTreeTableView.getSelectionModel().select(taskTreeItem);
                    taskGanttChart.getTaskPaneObjectsLayer().addTaskBar(task); // Добавляем на диаграмму
                }
                // Удаляем
                for (ITask task : change.getRemoved()) {
                    for (TreeItem<ITask> taskTreeItem : taskTreeTableView.getRoot().getChildren()) {
                        if (taskTreeItem.getValue().equals(task)) {
                            taskTreeTableView.getRoot().getChildren().remove(taskTreeItem);
                            break; //Если убрать - будет ConcurrentModificationException
                        }
                    }
                    taskGanttChart.getTaskPaneObjectsLayer().removeTaskBar(task); // Удаляем с диаграммы
                }
            }
        });
        LOGGER.debug("Таблица задач проинициализирована.");
    }

    private void setTableEditable() {
        taskTreeTableView.setEditable(true);
        nameColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        startDateColumn.setCellFactory(column -> new TreeTableCell<ITask, LocalDate>() {
            DatePicker startDatePicker;

            @Override
            public void startEdit() {
                super.startEdit();
                if (startDatePicker == null) {
                    createDatePicker();
                }
                startDatePicker.setValue(getItem());
                setText(null);
                setGraphic(startDatePicker);
                startDatePicker.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
                setAlignment(Pos.CENTER);
            }

            private void createDatePicker() {
                this.startDatePicker = new DatePicker();
                startDatePicker.setEditable(false);
                startDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(DatePicker param) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(uiControl.getController().getProject().getStartDate())) {
                                    setDisable(true);
                                }
                                if (item.isEqual(uiControl.getController().getProject().getFinishDate()) || item.isAfter(uiControl.getController().getProject().getFinishDate())) {
                                    setDisable(true);
                                }
                            }
                        };
                    }
                });
                startDatePicker.setOnAction(event -> {
                    commitEdit(startDatePicker.getValue());
                    event.consume();
                });
            }

            private String getString() {
                return getItem() == null ? "" : DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(getItem());
            }
        });
        finishDateColumn.setCellFactory(column -> new TreeTableCell<ITask, LocalDate>() {
            DatePicker finishDatePicker;

            @Override
            public void startEdit() {
                super.startEdit();
                if (finishDatePicker == null) {
                    createDatePicker();
                }
                finishDatePicker.setValue(getItem());
                setText(null);
                setGraphic(finishDatePicker);
                finishDatePicker.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
                setAlignment(Pos.CENTER);
            }

            private void createDatePicker() {
                this.finishDatePicker = new DatePicker();
                finishDatePicker.setEditable(false);
                finishDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(DatePicker param) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(getTreeTableRow().getTreeItem().getValue().getStartDate().plusDays(1))) {
                                    setDisable(true);
                                }
                                if (item.isEqual(uiControl.getController().getProject().getFinishDate().plusDays(1)) || item.isAfter(uiControl.getController().getProject().getFinishDate().plusDays(1))) {
                                    setDisable(true);
                                }
                            }
                        };
                    }
                });
                finishDatePicker.setOnAction(event -> {
                    commitEdit(finishDatePicker.getValue());
                    event.consume();
                });
            }

            private String getString() {
                return getItem() == null ? "" : DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(getItem());
            }
        });
        donePercentColumn.setCellFactory(column -> new TreeTableCell<ITask, Integer>() {
            Slider slider;

            @Override
            public void startEdit() {
                super.startEdit();
                if (slider == null) {
                    createSlider();
                }
                slider.setValue(getItem());
                setText(null);
                setGraphic(slider);
                slider.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(String.valueOf(getItem()));
                setGraphic(null);
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }

            private void createSlider() {
                slider = new Slider();
                slider.setShowTickMarks(true);
                slider.setOnMouseReleased(event -> {
                    if (!event.isPrimaryButtonDown()) {
                        commitEdit(slider.valueProperty().intValue());
                        event.consume();
                    }
                });
            }

            private String getString() {
                return getItem() == null ? "0" : getItem().toString();
            }
        });
        priorityColumn.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(PriorityType.values()));
        costColumn.setCellFactory(column -> new TreeTableCell<ITask, Double>() {
            TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();
                if (textField == null) {
                    createTextField();
                }
                textField.setText(getString());
                setText(null);
                setGraphic(textField);
                textField.selectAll();
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getString());
                setGraphic(null);
            }

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setText(null);
                        setGraphic(textField);
                    } else {
                        setText(getString());
                        setGraphic(null);
                    }
                }
            }

            private void createTextField() {
                textField = new TextField();
                textField.setOnKeyPressed(event -> {
                    if ((isInteger(event.getText()) || event.getText().equals(".") && (countChar(textField.getText(), '.') < 1)) || (event.getCode() == KeyCode.BACK_SPACE)) {
                        textField.setEditable(true);
                        if ((textField.getText().length() > 0) && (textField.getText().lastIndexOf(".") != -1)) {
                            if ((textField.getText().length() > textField.getText().lastIndexOf(".") + 2) && (event.getCode() != KeyCode.BACK_SPACE)) {
                                textField.setEditable(false);
                            }
                        }
                    } else {
                        textField.setEditable(false);
                    }
                });
                textField.setOnAction(event -> {
                    commitEdit(new DoubleStringConverter().fromString(textField.getText()));
                    event.consume();
                });

            }

            private String getString() {
                return getItem() == null ? "0.0" : getItem().toString();
            }

            private int countChar(String text, char s) {
                int count = 0;
                for (char element : text.toCharArray()) {
                    if (element == s) count++;
                }
                return count;
            }

            private boolean isInteger(String string) {
                if (string == null) {
                    return false;
                }
                int length = string.length();
                if (length == 0) {
                    return false;
                }
                int i = 0;
                if (string.charAt(0) == '-') {
                    if (length == 1) {
                        return false;
                    }
                    i = 1;
                }
                for (; i < length; i++) {
                    char c = string.charAt(i);
                    if (c < '0' || c > '9') {
                        return false;
                    }
                }
                return true;
            }
        });
        LOGGER.debug("Поля таблицы сделаны редактируемыми.");
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

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}