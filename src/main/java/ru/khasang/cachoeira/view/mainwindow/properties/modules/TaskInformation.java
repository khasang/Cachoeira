package ru.khasang.cachoeira.view.mainwindow.properties.modules;

import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import ru.khasang.cachoeira.model.PriorityType;

public class TaskInformation extends GridPane {
    private static final double FIRST_COLUMN_PERCENT_WIDTH = 30;
    private static final double SECOND_COLUMN_PERCENT_WIDTH = 70;

    private Label nameLabel;
    private Label startDateLabel;
    private Label finishDateLabel;
    private Label donePercentLabel;
    private Label priorityTypeLabel;
    private Label costLabel;
    private Label descriptionLabel;

    private TextField nameField;
    private DatePicker startDatePicker;
    private DatePicker finishDatePicker;
    private Slider donePercentSlider;
    private ComboBox<PriorityType> priorityTypeComboBox;
    private TextField costField;
    private TextArea descriptionTextArea;

    public TaskInformation() {
        nameLabel = new Label("Name");
        startDateLabel = new Label("Start Date");
        finishDateLabel = new Label("Finish Date");
        donePercentLabel = new Label("Percent");
        priorityTypeLabel = new Label("Priority Type");
        costLabel = new Label("Cost");
        descriptionLabel = new Label("Description");

        nameField = new TextField();
        startDatePicker = new DatePicker();
        finishDatePicker = new DatePicker();
        donePercentSlider = new Slider();
        priorityTypeComboBox = new ComboBox<>();
        costField = new TextField();
        descriptionTextArea = new TextArea();
    }

    public void createPane() {
        ColumnConstraints firstColumnConstrains = new ColumnConstraints();
        ColumnConstraints secondColumnConstrains = new ColumnConstraints();
        firstColumnConstrains.setPercentWidth(FIRST_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setPercentWidth(SECOND_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setHgrow(Priority.ALWAYS);

        this.getColumnConstraints().addAll(firstColumnConstrains, secondColumnConstrains);
        this.addRow(0, nameLabel, nameField);
        this.addRow(1, startDateLabel, startDatePicker);
        this.addRow(2, finishDateLabel, finishDatePicker);
        this.addRow(3, donePercentLabel, donePercentSlider);
        this.addRow(4, priorityTypeLabel, priorityTypeComboBox);
        this.addRow(5, costLabel, costField);
        this.addRow(6, descriptionLabel, descriptionTextArea);
        this.setVgap(10);
        this.setHgap(10);

        startDatePicker.setPrefWidth(350);
        finishDatePicker.setPrefWidth(350);
        priorityTypeComboBox.setPrefWidth(350);
        GridPane.setValignment(descriptionTextArea, VPos.TOP);
    }
}
