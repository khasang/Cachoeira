package ru.khasang.cachoeira.view;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;

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
        ObservableList<PriorityType> taskPriorityTypes = FXCollections.observableArrayList(PriorityType.values());
        priorityTypeComboBox.setItems(taskPriorityTypes);
        // Делаем панель не активной, если задача не выбрана
        propertiesPane.disableProperty().bind(uiControl.getController().selectedTaskProperty().isNull());

        /* Поле наименование */
        nameField.setOnKeyPressed(keyEvent -> {
            // Изменения применяем только при нажатии на ENTER...
            if (keyEvent.getCode() == KeyCode.ENTER) {
                // Если поле не пустое
                if (!nameField.getText().trim().isEmpty()) {
                    uiControl.getController().getSelectedTask().setName(nameField.getText());
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
                // Если поле не пустое, то
                if (!nameField.getText().trim().isEmpty()) {
                    // применяем изменения
                    uiControl.getController().getSelectedTask().setName(nameField.getText());
                } else {
                    // либо возвращаем предыдущее название
                    nameField.setText(uiControl.getController().getSelectedTask().getName());
                }
            }
        };
        nameField.focusedProperty().addListener(new WeakInvalidationListener(nameFieldFocusListener));

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
    }

    public void initAssignmentResourceTable(UIControl uiControl) {
        resourceTableView.setItems(uiControl.getController().getProject().getResourceList());
        resourceNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
    }

    public void setListeners(UIControl uiControl) {
        taskListListener = change -> initCheckBoxColumn(uiControl.getController().getSelectedTask());
        selectedTaskListener = (observable, oldSelectedTask, newSelectedTask) -> {
            // Прежде чем привязать поля свойств новой задачи необходимо отвязать поля предыдущей задачи (если такая была)
            if (oldSelectedTask != null) {
//                nameField.textProperty().unbindBidirectional(oldSelectedTask.nameProperty());
                startDatePicker.valueProperty().unbindBidirectional(oldSelectedTask.startDateProperty());
                finishDatePicker.valueProperty().unbindBidirectional(oldSelectedTask.finishDateProperty());
                donePercentSlider.valueProperty().unbindBidirectional(oldSelectedTask.donePercentProperty());
                priorityTypeComboBox.valueProperty().unbindBidirectional(oldSelectedTask.priorityTypeProperty());
                costField.textProperty().unbindBidirectional(oldSelectedTask.costProperty());
                descriptionTextArea.textProperty().unbindBidirectional(oldSelectedTask.descriptionProperty());
            }

            // Привязываем поля свойств к модели
            if (newSelectedTask != null) {
//                nameField.textProperty().bindBidirectional(newSelectedTask.nameProperty());
                nameField.setText(newSelectedTask.getName());
                startDatePicker.valueProperty().bindBidirectional(newSelectedTask.startDateProperty());
                finishDatePicker.valueProperty().bindBidirectional(newSelectedTask.finishDateProperty());
                donePercentSlider.valueProperty().bindBidirectional(newSelectedTask.donePercentProperty());
                priorityTypeComboBox.valueProperty().bindBidirectional(newSelectedTask.priorityTypeProperty());
                costField.textProperty().bindBidirectional(newSelectedTask.costProperty(), new NumberStringConverter());
                descriptionTextArea.textProperty().bindBidirectional(newSelectedTask.descriptionProperty());
            }
            // Если выбрали другую задачу перерисовываем таблицу привязанных ресурсов
            initCheckBoxColumn(uiControl.getController().getSelectedTask());
        };

        uiControl.getController().selectedTaskProperty().addListener(new WeakChangeListener<>(selectedTaskListener));
        // Если список ресурсов в какой либо задаче обновляется, то обновляем список ресурсов в панели свойств задач
        uiControl.getController().getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListListener));
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
                                selectedTask.addResource(currentRowResource);
                            } else {
                                selectedTask.removeResource(currentRowResource);
                            }
                        });

                        // Расставляем галочки на нужных строках
                        for (IResource resource : selectedTask.getResourceList()) {
                            if (resource.equals(currentRowResource)) {
                                checkBox.setSelected(true);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }
}
