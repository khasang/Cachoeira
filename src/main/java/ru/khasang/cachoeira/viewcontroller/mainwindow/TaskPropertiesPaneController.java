package ru.khasang.cachoeira.viewcontroller.mainwindow;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.task.*;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.viewcontroller.UIControl;
import ru.khasang.cachoeira.viewcontroller.mainwindow.contextmenus.ParentTaskContextMenu;

import java.time.LocalDate;

/**
 * Класс-контроллер для TaskPropertiesPane.fxml
 */
public class TaskPropertiesPaneController {
    // Информация
    @FXML
    private VBox propertiesPane;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;
    @FXML
    private Slider donePercentSlider;
    @FXML
    private ComboBox<PriorityType> priorityTypeComboBox;
    @FXML
    private TextField costField;
    @FXML
    private TextArea descriptionTextArea;
    // Привязанные ресурсы
    @FXML
    private TableView<IResource> resourceTableView;
    @FXML
    private TableColumn<IResource, String> resourceNameColumn;
    @FXML
    private TableColumn<IResource, Boolean> resourceCheckboxColumn;
    // Привязанные задачи
    @FXML
    private TableView<IDependentTask> parentTaskTableView;
    @FXML
    private TableColumn<IDependentTask, String> parentTaskNameColumn;
    @FXML
    private TableColumn<IDependentTask, TaskDependencyType> parentTaskDependencyTypeColumn;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<ITask> selectedTaskListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener nameFieldFocusListener;


    public TaskPropertiesPaneController() {
    }

    /**
     * Метод initialize исполняется после загрузки fxml файла
     */
    @FXML
    private void initialize() {
        // Запрет на изменение полей с датами с помощью клавиатуры
        startDatePicker.setEditable(false);
        finishDatePicker.setEditable(false);
    }

    /**
     * Метод onlyNumber позволяет вводить в поле costField только цифры и точку.
     *
     * @param event Перехваченное нажатие на клавишу.
     */
    @FXML
    private void onlyNumber(KeyEvent event) {
        if ((isInteger(event.getText()) || event.getText().equals(".") && (countChar(costField.getText(), '.') < 1)) || (event.getCode() == KeyCode.BACK_SPACE) || (event.getCode() == KeyCode.DELETE)) {
            costField.setEditable(true);
            if ((costField.getText().length() > 0) && (costField.getText().lastIndexOf(".") != -1)) {
                if ((costField.getText().length() > costField.getText().lastIndexOf(".") + 2) && (event.getCode() != KeyCode.BACK_SPACE) && (event.getCode() != KeyCode.DELETE)) {
                    costField.setEditable(false);
                }
            }
        } else {
            costField.setEditable(false);
        }
    }

    /**
     * Считает количество дублей заданного символа в строке.
     *
     * @param text Строка.
     * @param c    Символ.
     * @return Возвращает количество (int).
     */
    private int countChar(String text, char c) {
        int count = 0;
        for (char element : text.toCharArray()) {
            if (element == c) count++;
        }
        return count;
    }

    /**
     * Проверяет является ли строка цифрами.
     *
     * @param string Строка для проверки.
     * @return Возвращает true, либо false.
     */
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

    public void initFields(UIControl uiControl) {
        // Заполняем комбобокс
        priorityTypeComboBox.setItems(FXCollections.observableArrayList(PriorityType.values()));
        // Делаем панель не активной, если задача не выбрана
        propertiesPane.disableProperty().bind(uiControl.getController().selectedTaskProperty().isNull());

        /* Поле наименование */
        nameField.setOnKeyPressed(keyEvent -> {
            // Изменения применяем только при нажатии на ENTER...
            if (keyEvent.getCode() == KeyCode.ENTER) {
                // Если поле не пустое
                if (!nameField.getText().trim().isEmpty()) {
                    CommandControl.getInstance().execute(new RenameTaskCommand(uiControl.getController().getSelectedTask(), nameField.getText()));
                    // Убираем фокусировку с поля наименования задачи
                    nameField.getParent().requestFocus();
                }
            }
            // Если нажали ESC,
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                // то возвращаем предыдущее название
                nameField.setText(uiControl.getController().getSelectedTask().getName());
                // Убираем фокусировку с поля наименования задачи
                nameField.getParent().requestFocus();
            }
        });
        // ... или при потере фокуса.
        nameFieldFocusListener = observable -> {
            if (!nameField.isFocused()) {
                nameField.setText(uiControl.getController().getSelectedTask().getName());
            }
        };
        nameField.focusedProperty().addListener(new WeakInvalidationListener(nameFieldFocusListener));
        /* Поле дата начала*/
        startDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate startDate, boolean empty) {
                super.updateItem(startDate, empty);
                if (startDate.isBefore(uiControl.getController().getProject().getStartDate())) {
                    setDisable(true);
                }
                if (startDate.isEqual(uiControl.getController().getProject().getFinishDate()) || startDate.isAfter(uiControl.getController().getProject().getFinishDate())) {
                    setDisable(true);
                }
            }
        });
        /* Поле дата окончания */
        // Отключает возможность в Дате окончания выбрать дату предыдущую Начальной даты
        finishDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate finishDate, boolean empty) {
                super.updateItem(finishDate, empty);
                if (finishDate.isBefore(startDatePicker.getValue().plusDays(1))) {
                    setDisable(true);
                }
                if (finishDate.isEqual(uiControl.getController().getProject().getFinishDate().plusDays(1)) || finishDate.isAfter(uiControl.getController().getProject().getFinishDate().plusDays(1))) {
                    setDisable(true);
                }
            }
        });
        /* Слайдер изменения прогресса */
        donePercentSlider.setSnapToTicks(true);
        donePercentSlider.setOnMouseReleased(event -> CommandControl.getInstance().execute(
                new SetTaskDonePercentCommand(uiControl.getController().getSelectedTask(), (int) donePercentSlider.getValue())));
    }

    public void initAssignmentResourceTable(UIControl uiControl) {
        resourceTableView.setItems(uiControl.getController().getProject().getResourceList());
        resourceNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
    }

    public void setListeners(UIControl uiControl) {
        taskListListener = change -> initCheckBoxColumn(uiControl.getController().getSelectedTask());
        selectedTaskListener = (observable, oldSelectedTask, newSelectedTask) -> {
            // Привязываем поля свойств к модели
            if (newSelectedTask != null) {
                nameField.setText(newSelectedTask.getName());
                startDatePicker.setValue(newSelectedTask.getStartDate());
                finishDatePicker.setValue(newSelectedTask.getFinishDate());
                donePercentSlider.setValue(newSelectedTask.getDonePercent());
                priorityTypeComboBox.setValue(newSelectedTask.getPriorityType());
                costField.setText(String.valueOf(newSelectedTask.getCost()));
                descriptionTextArea.setText(newSelectedTask.getDescription());

                uiControl.getController().getSelectedTask().nameProperty().addListener(observable1 -> {
                    nameField.setText(uiControl.getController().getSelectedTask().getName());
                });
                uiControl.getController().getSelectedTask().startDateProperty().addListener(observable1 -> {
                    startDatePicker.setValue(uiControl.getController().getSelectedTask().getStartDate());
                });
                uiControl.getController().getSelectedTask().finishDateProperty().addListener(observable1 -> {
                    finishDatePicker.setValue(uiControl.getController().getSelectedTask().getFinishDate());
                });
                uiControl.getController().getSelectedTask().donePercentProperty().addListener(observable1 -> {
                    donePercentSlider.setValue(uiControl.getController().getSelectedTask().getDonePercent());
                });
                uiControl.getController().getSelectedTask().priorityTypeProperty().addListener(observable1 -> {
                    priorityTypeComboBox.setValue(uiControl.getController().getSelectedTask().getPriorityType());
                });
                uiControl.getController().getSelectedTask().costProperty().addListener(observable1 -> {
                    costField.setText(String.valueOf(uiControl.getController().getSelectedTask().getCost()));
                });
                uiControl.getController().getSelectedTask().descriptionProperty().addListener(observable1 -> {
                    descriptionTextArea.setText(uiControl.getController().getSelectedTask().getDescription());
                });

                startDatePicker.valueProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetTaskStartDateCommand(uiControl.getController().getSelectedTask(), newValue));
                });
                finishDatePicker.valueProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetTaskFinishDateCommand(uiControl.getController().getSelectedTask(), newValue));
                });
                priorityTypeComboBox.valueProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetTaskPriorityTypeCommand(uiControl.getController().getSelectedTask(), newValue));
                });
                costField.textProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetTaskCostCommand(uiControl.getController().getSelectedTask(), Double.parseDouble(newValue)));
                });
                descriptionTextArea.textProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetTaskDescriptionCommand(uiControl.getController().getSelectedTask(), newValue));
                });
            }
            // Если выбрали другую задачу перерисовываем таблицу привязанных ресурсов
            initCheckBoxColumn(uiControl.getController().getSelectedTask());
            initParentTaskTableView(uiControl.getController().getSelectedTask(), uiControl);
        };

        uiControl.getController().selectedTaskProperty().addListener(new WeakChangeListener<>(selectedTaskListener));
        // Если список ресурсов в какой либо задаче обновляется, то обновляем список ресурсов в панели свойств задач
        uiControl.getController().getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListListener));
    }

    public void initParentTaskTableView(ITask selectedTask, UIControl uiControl) {
        if (selectedTask != null) {
            parentTaskTableView.setItems(selectedTask.getParentTasks());
            parentTaskNameColumn.setCellValueFactory(cellData -> cellData.getValue().getTask().nameProperty());
            parentTaskDependencyTypeColumn.setCellValueFactory(cellData -> cellData.getValue().dependenceTypeProperty());
            // Добавляем возможность изменять тип связи в таблице
            parentTaskTableView.setEditable(true);
            // TODO: 18.04.2016 Сделать изменение строки через команды
            parentTaskDependencyTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(TaskDependencyType.values()));
            // Контекстное меню
            ParentTaskContextMenu parentTaskContextMenu = new ParentTaskContextMenu();
            parentTaskContextMenu.initMenus(uiControl.getController(), uiControl.getController().getSelectedTask());
            parentTaskTableView.setContextMenu(parentTaskContextMenu);
        }
    }

    private void initCheckBoxColumn(ITask selectedTask) {
        if (selectedTask != null) {
            resourceCheckboxColumn.setCellFactory(column -> new TableCell<IResource, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    setAlignment(Pos.CENTER);
                    IResource currentRowResource = (IResource) getTableRow().getItem();
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Заполняем столбец чек-боксами
                        CheckBox checkBox = new CheckBox();
                        setGraphic(checkBox);
                        checkBox.setOnAction(event -> {
                            if (checkBox.isSelected()) {
                                CommandControl.getInstance().execute(new AddResourceToTaskCommand(selectedTask, currentRowResource));
                            } else {
                                CommandControl.getInstance().execute(new RemoveResourceFromTaskCommand(selectedTask, currentRowResource));
                            }
                        });

                        // Расставляем галочки на нужных строках
                        selectedTask.getResourceList()
                                .stream()
                                .filter(resource -> resource.equals(currentRowResource) && !checkBox.isSelected())
                                .forEach(resource -> checkBox.setSelected(true));
                    }
                }
            });
        }
    }
}
