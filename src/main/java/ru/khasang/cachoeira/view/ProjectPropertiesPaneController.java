package ru.khasang.cachoeira.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.IController;

import java.time.LocalDate;

/**
 * Created by truesik on 24.11.2015.
 */
public class ProjectPropertiesPaneController {
    //Информация
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;
    @FXML
    private TextArea descriptionTextArea;

    private IController controller;

    public ProjectPropertiesPaneController() {
    }

    @FXML
    private void initialize() {
        /** Запрет на изменение полей с датами с помощью клавиатуры **/
        startDatePicker.setEditable(false);
        finishDatePicker.setEditable(false);
    }

    /** инциализировать initFields() только после setController() **/
    public void initFields() {
        /** Привязываем поля свойств к модели **/
        nameField.textProperty().bindBidirectional(controller.getProject().nameProperty());
        startDatePicker.valueProperty().bindBidirectional(controller.getProject().startDateProperty());
        finishDatePicker.valueProperty().bindBidirectional(controller.getProject().finishDateProperty());
        descriptionTextArea.textProperty().bindBidirectional(controller.getProject().descriptionProperty());

        /** Конечная дата всегда после начальной **/
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEqual(finishDatePicker.getValue()) || newValue.isAfter(finishDatePicker.getValue())) {
                finishDatePicker.setValue(newValue.plusDays(1));
            }
        });

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
                    }
                };
            }
        });
    }

    public void setController(IController controller) {
        this.controller = controller;
    }
}
