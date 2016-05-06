package ru.khasang.cachoeira.viewcontroller.mainwindow;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.project.RenameProjectCommand;
import ru.khasang.cachoeira.commands.project.SetProjectDescriptionCommand;
import ru.khasang.cachoeira.commands.project.SetProjectFinishDateCommand;
import ru.khasang.cachoeira.commands.project.SetProjectStartDateCommand;
import ru.khasang.cachoeira.viewcontroller.UIControl;

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
        // Устанавливаем начальные значения
        nameField.setText(uiControl.getController().getProject().getName());
        startDatePicker.setValue(uiControl.getController().getProject().getStartDate());
        finishDatePicker.setValue(uiControl.getController().getProject().getFinishDate());
        descriptionTextArea.setText(uiControl.getController().getProject().getDescription());
        // При изменении значения в модели меняем значение в поле
        uiControl.getController().getProject().nameProperty().addListener(observable -> {
            nameField.setText(uiControl.getController().getProject().getName());
        });
        uiControl.getController().getProject().startDateProperty().addListener(observable -> {
            startDatePicker.setValue(uiControl.getController().getProject().getStartDate());
        });
        uiControl.getController().getProject().finishDateProperty().addListener(observable -> {
            finishDatePicker.setValue(uiControl.getController().getProject().getFinishDate());
        });
        uiControl.getController().getProject().descriptionProperty().addListener(observable -> {
            descriptionTextArea.setText(uiControl.getController().getProject().getDescription());
        });

        /* Поле наименование */
        nameField.setOnKeyPressed(keyEvent -> {
            // Изменения применяем только при нажатии на ENTER...
            if (keyEvent.getCode() == KeyCode.ENTER) {
                // Если поле не пустое
                if (!nameField.getText().trim().isEmpty()) {
                    CommandControl.getInstance().execute(new RenameProjectCommand(uiControl.getController().getProject(), nameField.getText()));
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
                nameField.setText(uiControl.getController().getProject().getName());
            }
        };
        nameField.focusedProperty().addListener(new WeakInvalidationListener(nameFieldFocusListener));

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            CommandControl.getInstance().execute(new SetProjectStartDateCommand(uiControl.getController().getProject(), newValue));
        });

        finishDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            CommandControl.getInstance().execute(new SetProjectFinishDateCommand(uiControl.getController().getProject(), newValue));
        });

        descriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            CommandControl.getInstance().execute(new SetProjectDescriptionCommand(uiControl.getController().getProject(), newValue));
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
