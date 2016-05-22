package ru.khasang.cachoeira.view.createnewprojectwindow.panes;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import ru.khasang.cachoeira.view.MaterialButton;

public class FieldsPane extends GridPane implements IFieldsPane {
    private static final double FIRST_COLUMN_PERCENT_WIDTH = 30;
    private static final double SECOND_COLUMN_PERCENT_WIDTH = 70;

    private Label projectNameLabel;
    private Label projectPathLabel;
    private Label startDateLabel;
    private Label finishDateLabel;
    private Label descriptionLabel;

    private TextField projectNameField;
    private TextField projectPathField;
    private DatePicker startDatePicker;
    private DatePicker finishDatePicker;
    private TextArea descriptionTextArea;

    private MaterialButton pathChooserButton;

    public FieldsPane() {
        projectNameLabel = new Label("Name");
        projectPathLabel = new Label("Path");
        startDateLabel = new Label("Start Date");
        finishDateLabel = new Label("Finish Date");
        descriptionLabel = new Label("Description");

        projectNameField = new TextField();
        projectPathField = new TextField();
        pathChooserButton = new MaterialButton("...");
        startDatePicker = new DatePicker();
        finishDatePicker = new DatePicker();
        descriptionTextArea = new TextArea();
    }

    @Override
    public void createPane() {
        this.setHgap(10);
        this.setVgap(10);

        ColumnConstraints firstColumnConstrains = new ColumnConstraints();
        ColumnConstraints secondColumnConstrains = new ColumnConstraints();
        firstColumnConstrains.setPercentWidth(FIRST_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setPercentWidth(SECOND_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().addAll(firstColumnConstrains, secondColumnConstrains);

        this.addRow(0, projectNameLabel, projectNameField);
        this.addRow(1, projectPathLabel, new HBox(projectPathField, pathChooserButton));
        this.addRow(2, startDateLabel, startDatePicker);
        this.addRow(3, finishDateLabel, finishDatePicker);
        this.addRow(4, descriptionLabel, descriptionTextArea);

        HBox.setHgrow(projectPathField, Priority.ALWAYS);
        GridPane.setMargin(descriptionTextArea, new Insets(0, 0, 10, 0));
        GridPane.setValignment(descriptionLabel, VPos.TOP);

        startDatePicker.setPrefWidth(350);
        finishDatePicker.setPrefWidth(350);
        descriptionTextArea.setPrefHeight(200);
    }

    @Override
    public TextField getProjectNameField() {
        return projectNameField;
    }

    @Override
    public TextField getProjectPathField() {
        return projectPathField;
    }

    @Override
    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    @Override
    public DatePicker getFinishDatePicker() {
        return finishDatePicker;
    }

    @Override
    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    @Override
    public Button getPathChooserButton() {
        return pathChooserButton;
    }
}
