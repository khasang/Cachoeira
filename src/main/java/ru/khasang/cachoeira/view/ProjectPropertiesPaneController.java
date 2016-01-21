package ru.khasang.cachoeira.view;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

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

    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener nameFieldFocusListener;

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
//        nameField.textProperty().bindBidirectional(uiControl.getController().getProject().nameProperty());
        nameField.setText(uiControl.getController().getProject().getName());
        startDatePicker.valueProperty().bindBidirectional(uiControl.getController().getProject().startDateProperty());
        finishDatePicker.valueProperty().bindBidirectional(uiControl.getController().getProject().finishDateProperty());
        descriptionTextArea.textProperty().bindBidirectional(uiControl.getController().getProject().descriptionProperty());

        /* Поле наименование */
        nameField.setOnKeyPressed(keyEvent -> {
            // Изменения применяем только при нажатии на ENTER...
            if (keyEvent.getCode() == KeyCode.ENTER) {
                // Если поле не пустое
                if (!nameField.getText().trim().isEmpty()) {
                    uiControl.getController().getProject().setName(nameField.getText());
                    // Убираем фокусировку с поля наименования задачи
                    nameField.getParent().requestFocus();
                }
            }
            // Если нажали ESC,
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                // то возвращаем предыдущее название
                nameField.setText(uiControl.getController().getProject().getName());
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
                    uiControl.getController().getProject().setName(nameField.getText());
                } else {
                    // либо возвращаем предыдущее название
                    nameField.setText(uiControl.getController().getProject().getName());
                }
            }
        };
        nameField.focusedProperty().addListener(new WeakInvalidationListener(nameFieldFocusListener));

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
