package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProjectPropertiesPane extends VBox {
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

    public void createPane() {
        initLabelsAndFields();

        ColumnConstraints firstColumnConstrains = new ColumnConstraints();
        ColumnConstraints secondColumnConstrains = new ColumnConstraints();
        firstColumnConstrains.setPercentWidth(FIRST_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setPercentWidth(SECOND_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setHgrow(Priority.ALWAYS);

        GridPane gridPane = new GridPane();
        gridPane.getColumnConstraints().addAll(firstColumnConstrains, secondColumnConstrains);
        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, startDateLabel, startDatePicker);
        gridPane.addRow(2, finishDateLabel, finishDatePicker);
        gridPane.addRow(3, descriptionLabel, descriptionTextArea);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        startDatePicker.setPrefWidth(350);
        finishDatePicker.setPrefWidth(350);
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        TitledPane infoTitledPane = new TitledPane("Information", gridPane);
        this.getChildren().add(infoTitledPane);
    }

    private void initLabelsAndFields() {
        nameLabel = new Label("Name");
        startDateLabel = new Label("Start Date");
        finishDateLabel = new Label("Finish Date");
        descriptionLabel = new Label("Description");

        nameField = new TextField();
        startDatePicker = new DatePicker();
        finishDatePicker = new DatePicker();
        descriptionTextArea = new TextArea();
    }

    public TextField getNameField() {
        return nameField;
    }

    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    public DatePicker getFinishDatePicker() {
        return finishDatePicker;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }
}
