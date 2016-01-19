package ru.khasang.cachoeira.view;

import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Класс-контроллер для ProjectPropertiesPane.fxml
 */
public class ProjectPropertiesPaneController {
    // Информация
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;
    @FXML
    private TextArea descriptionTextArea;

    public ProjectPropertiesPaneController() {
    }

    @FXML
    private void initialize() {
        // Запрет на изменение полей с датами с помощью клавиатуры
        startDatePicker.setEditable(false);
        finishDatePicker.setEditable(false);
    }

    public void initFields(UIControl uiControl) {
        // Привязываем поля свойств к модели
        nameField.textProperty().bindBidirectional(uiControl.getController().getProject().nameProperty());
        startDatePicker.valueProperty().bindBidirectional(uiControl.getController().getProject().startDateProperty());
        finishDatePicker.valueProperty().bindBidirectional(uiControl.getController().getProject().finishDateProperty());
        descriptionTextArea.textProperty().bindBidirectional(uiControl.getController().getProject().descriptionProperty());

        // Конечная дата всегда после начальной
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEqual(finishDatePicker.getValue()) || newValue.isAfter(finishDatePicker.getValue())) {
                finishDatePicker.setValue(newValue.plusDays(1));
            }
        });

        finishDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate finishDate, boolean empty) {
                super.updateItem(finishDate, empty);
                if (finishDate.isBefore(startDatePicker.getValue().plusDays(1))) {
                    setDisable(true);
                }
            }
        });
    }
}
