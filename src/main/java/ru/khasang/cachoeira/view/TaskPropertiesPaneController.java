package ru.khasang.cachoeira.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;

/**
 * Created by truesik on 24.11.2015.
 */
public class TaskPropertiesPaneController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label finishDateLabel;
    @FXML
    private Label donePercentLabel;
    @FXML
    private Label priorityTypeLabel;
    @FXML
    private Label costLabel;

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
    private TableView resourceTableView;
    @FXML
    private TableColumn resourceNameColumn;
    @FXML
    private TableColumn resourceCheckboxColumn;

    private IController controller;
    private ObjectProperty<ITask> task;

    public TaskPropertiesPaneController() {
    }

    @FXML
    private void initialize() {
        nameLabel.setLabelFor(nameField);
        startDateLabel.setLabelFor(startDatePicker);
        finishDateLabel.setLabelFor(finishDatePicker);
        donePercentLabel.setLabelFor(donePercentSlider);
        priorityTypeLabel.setLabelFor(priorityTypeComboBox);
        costLabel.setLabelFor(costField);
    }

    @FXML
    public void onlyNumber(KeyEvent event) {
        if ((isInteger(event.getText()) || event.getText().equals(".") && (countChar(costField.getText(), ".") < 1)) || (event.getCode() == KeyCode.BACK_SPACE)) {
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

    private int countChar(String text, String s) {
        int count = 0;
        for (char element : text.toCharArray()) {
            if (element == '.') count++;
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

    public void setTask(ITask task) {
        this.task.set(task);
    }

    public void initFields() {
        controller.selectedTaskProperty().addListener(new ChangeListener<ITask>() {
            @Override
            public void changed(ObservableValue<? extends ITask> observable, ITask oldValue, ITask newValue) {
                propertiesPane.setDisable(false);
                nameField.textProperty().bindBidirectional(newValue.nameProperty());
                startDatePicker.valueProperty().bindBidirectional(newValue.startDateProperty());
                finishDatePicker.valueProperty().bindBidirectional(newValue.finishDateProperty());
                donePercentSlider.valueProperty().bindBidirectional(newValue.donePercentProperty());
                priorityTypeComboBox.valueProperty().bindBidirectional(newValue.priorityTypeProperty());
                costField.textProperty().bindBidirectional(newValue.costProperty(), new NumberStringConverter());
            }
        });

        if (task != null) {
            nameField.textProperty().bindBidirectional(task.getValue().nameProperty());
            startDatePicker.valueProperty().bindBidirectional(task.getValue().startDateProperty());
            finishDatePicker.valueProperty().bindBidirectional(task.getValue().finishDateProperty());
            donePercentSlider.valueProperty().bindBidirectional(task.getValue().donePercentProperty());
            priorityTypeComboBox.valueProperty().bindBidirectional(task.getValue().priorityTypeProperty());
            costField.textProperty().bindBidirectional(task.getValue().costProperty(), new NumberStringConverter());
        } else {
            propertiesPane.setDisable(true);
        }
    }
}
