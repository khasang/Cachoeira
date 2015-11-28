package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;

import java.time.LocalDate;

/**
 * Created by truesik on 24.11.2015.
 */
public class TaskPropertiesPaneController {
    //Информация
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

    //Привязанные ресурсы
    @FXML
    private TextField costField;
    @FXML
    private TableView<IResource> resourceTableView;
    @FXML
    private TableColumn<IResource, String> resourceNameColumn;
    @FXML
    private TableColumn<IResource, Boolean> resourceCheckboxColumn;

    private IController controller;

    public TaskPropertiesPaneController() {
    }

    /**
     * метод initialize исполняется после загрузки fxml файла
     */
    @FXML
    private void initialize() {
        /** Запрет на изменение полей с датами с помощью клавиатуры **/
        startDatePicker.setEditable(false);
        finishDatePicker.setEditable(false);
    }

    @FXML
    public void onlyNumber(KeyEvent event) {
        if ((isInteger(event.getText()) || event.getText().equals(".") && (countChar(costField.getText(), '.') < 1)) || (event.getCode() == KeyCode.BACK_SPACE)) {
            costField.setEditable(true);
            if ((costField.getText().length() > 0) && (costField.getText().lastIndexOf(".") != -1)) {
                if ((costField.getText().length() > costField.getText().lastIndexOf(".") + 2) && (event.getCode() != KeyCode.BACK_SPACE)) {
                    costField.setEditable(false);
                }
            }
        } else {
            costField.setEditable(false);
        }
    }

    private int countChar(String text, char s) {
        int count = 0;
        for (char element : text.toCharArray()) {
            if (element == s) count++;
        }
        return count;
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void initFields() {
        /** Заполняем комбобокс **/
        ObservableList<PriorityType> taskPriorityTypes = FXCollections.observableArrayList(PriorityType.values());
        priorityTypeComboBox.setItems(taskPriorityTypes);

        /** Делаем панель не активной, если задача не выбрана**/
        propertiesPane.disableProperty().bind(controller.selectedTaskProperty().isNull());

        controller.selectedTaskProperty().addListener((observable, oldValue, newValue) -> {
            /** Прежде чем привязать поля свойств новой задачи необходимо отвязать поля предыдущей задачи (если такая была) **/
            if (oldValue != null) {
                nameField.textProperty().unbindBidirectional(oldValue.nameProperty());
                startDatePicker.valueProperty().unbindBidirectional(oldValue.startDateProperty());
                finishDatePicker.valueProperty().unbindBidirectional(oldValue.finishDateProperty());
                donePercentSlider.valueProperty().unbindBidirectional(oldValue.donePercentProperty());
                priorityTypeComboBox.valueProperty().unbindBidirectional(oldValue.priorityTypeProperty());
                costField.textProperty().unbindBidirectional(oldValue.costProperty());
            }

            /** Привязываем поля свойств к модели **/
            if (newValue != null) {
                nameField.textProperty().bindBidirectional(newValue.nameProperty());
                startDatePicker.valueProperty().bindBidirectional(newValue.startDateProperty());
                finishDatePicker.valueProperty().bindBidirectional(newValue.finishDateProperty());
                donePercentSlider.valueProperty().bindBidirectional(newValue.donePercentProperty());
                priorityTypeComboBox.valueProperty().bindBidirectional(newValue.priorityTypeProperty());
                costField.textProperty().bindBidirectional(newValue.costProperty(), new NumberStringConverter());
            }

            /** Конечная дата всегда после начальной **/
            startDatePicker.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                if (newValue1.isAfter(finishDatePicker.getValue()) || newValue1.isEqual(finishDatePicker.getValue())) {
                    finishDatePicker.setValue(newValue1.plusDays(1));
                }
            });
        });

        startDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(controller.getProject().getStartDate())) {
                            setDisable(true);
                        }
                        if (item.isEqual(controller.getProject().getFinishDate()) || item.isAfter(controller.getProject().getFinishDate())) {
                            setDisable(true);
                        }
                    }
                };
            }
        });
        //отключает возможность в Дате окончания выбрать дату предыдущую Начальной даты
        finishDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(startDatePicker.getValue().plusDays(1))) {
                            setDisable(true);
                        }
                        if (item.isEqual(controller.getProject().getFinishDate()) || item.isAfter(controller.getProject().getFinishDate())) {
                            setDisable(true);
                        }
                    }
                };
            }
        });
    }

    public void initResourceTable() {
        resourceTableView.setItems(controller.getProject().getResourceList());
        resourceNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
        controller.selectedTaskProperty().addListener(new ChangeListener<ITask>() {
            @Override
            public void changed(ObservableValue<? extends ITask> observable, ITask oldValue, ITask newValue) {
                resourceCheckboxColumn.setCellFactory(new Callback<TableColumn<IResource, Boolean>, TableCell<IResource, Boolean>>() {
                    @Override
                    public TableCell<IResource, Boolean> call(TableColumn<IResource, Boolean> param) {
                        return new TableCell<IResource, Boolean>() {
                            @Override
                            public void updateItem(Boolean item, boolean empty) {
                                super.updateItem(item, empty);
                                setAlignment(Pos.CENTER);
                                TableRow<IResource> currentRow = getTableRow();
                                if (empty) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    /** Заполняем столбец чек-боксами **/
                                    CheckBox checkBox = new CheckBox();
                                    setGraphic(checkBox);
                                    checkBox.setOnAction(event -> {
                                        if (checkBox.isSelected()) {
                                            controller.selectedTaskProperty().getValue().addResource(currentRow.getItem());
                                        } else {
                                            controller.selectedTaskProperty().getValue().removeResource(currentRow.getItem());
                                        }
                                    });

                                    /** Расставляем галочки на нужных строках **/
                                    for (IResource resource : controller.selectedTaskProperty().getValue().getResourceList()) {
                                        if (resource.equals(currentRow.getItem())) {
                                            checkBox.selectedProperty().setValue(Boolean.TRUE);
                                            break;
                                        } else {
                                            checkBox.selectedProperty().setValue(Boolean.FALSE);
                                        }
                                    }
                                }
                            }
                        };
                    }
                });
            }
        });
    }
}
