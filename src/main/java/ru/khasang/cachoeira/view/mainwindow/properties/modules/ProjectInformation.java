package ru.khasang.cachoeira.view.mainwindow.properties.modules;

import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ProjectInformation extends GridPane {
    private static final double FIRST_COLUMN_PERCENT_WIDTH = 30;
    private static final double SECOND_COLUMN_PERCENT_WIDTH = 70;

    private Label nameLabel;
    private Label startDateLabel;
    private Label finishDateLabel;
    private Label descriptionLabel;

    private TextField nameField;
    private DatePicker startDatePicker;
    private DatePicker finishDatePicker;
    private TextArea descriptionTextArea;

    public ProjectInformation() {
        nameLabel = new Label("Name");
        startDateLabel = new Label("Start Date");
        finishDateLabel = new Label("Finish Date");
        descriptionLabel = new Label("Description");

        nameField = new TextField();
        startDatePicker = new DatePicker();
        finishDatePicker = new DatePicker();
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
        this.addRow(3, descriptionLabel, descriptionTextArea);
        this.setVgap(10);
        this.setHgap(10);

        startDatePicker.setPrefWidth(350);
        finishDatePicker.setPrefWidth(350);
        GridPane.setValignment(descriptionLabel, VPos.TOP);
    }
}
