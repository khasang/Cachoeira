package ru.khasang.cachoeira.view;

import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    public void initFields() {
        nameField.textProperty().bindBidirectional(controller.getProject().nameProperty());
        startDatePicker.valueProperty().bindBidirectional(controller.getProject().startDateProperty());
        finishDatePicker.valueProperty().bindBidirectional(controller.getProject().finishDateProperty());
        descriptionTextArea.textProperty().bindBidirectional(controller.getProject().descriptionProperty());
    }

    public IController getController() {
        return controller;
    }

    public void setController(IController controller) {
        this.controller = controller;
    }
}
