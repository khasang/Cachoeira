package ru.khasang.cachoeira.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateNewProjectWindowView extends Application {
    private static final double HEIGHT_WINDOW = 424;
    private static final double WIDTH_WINDOW = 487;
    private static final double FIRST_COLUMN_PERCENT_WIDTH = 30;
    private static final double SECOND_COLUMN_PERCENT_WIDTH = 70;

    private Stage stage;

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
    private MaterialButton createNewProjectButton;
    private MaterialButton cancelButton;

    public CreateNewProjectWindowView() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CreateNewProjectWindowView createNewProjectWindowView = new CreateNewProjectWindowView();
        createNewProjectWindowView.createView();
    }

    public void createView() {
        VBox window = new VBox(createFieldsGrid(), createButtonsBox());
        window.setPadding(new Insets(10));
        window.getStylesheets().add(getClass().getResource("/css/startwindow.css").toExternalForm());

        stage = new Stage(StageStyle.UTILITY);
        stage.setHeight(HEIGHT_WINDOW);
        stage.setWidth(WIDTH_WINDOW);
        stage.setScene(new Scene(window));
        stage.setResizable(false);
        stage.setTitle("Creating new project...");
//        stage.initOwner();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    private Node createFieldsGrid() {
        initLabels();
        initFields();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints firstColumnConstrains = new ColumnConstraints();
        ColumnConstraints secondColumnConstrains = new ColumnConstraints();
        firstColumnConstrains.setPercentWidth(FIRST_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setPercentWidth(SECOND_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(firstColumnConstrains, secondColumnConstrains);

        gridPane.addRow(0, projectNameLabel, projectNameField);
        gridPane.addRow(1, projectPathLabel, new HBox(projectPathField, pathChooserButton));
        gridPane.addRow(2, startDateLabel, startDatePicker);
        gridPane.addRow(3, finishDateLabel, finishDatePicker);
        gridPane.addRow(4, descriptionLabel, descriptionTextArea);

        HBox.setHgrow(projectPathField, Priority.ALWAYS);
        GridPane.setMargin(descriptionTextArea, new Insets(0, 0, 10, 0));
        GridPane.setValignment(descriptionLabel, VPos.TOP);

        startDatePicker.setPrefWidth(350);
        finishDatePicker.setPrefWidth(350);
        descriptionTextArea.setPrefHeight(200);
        return gridPane;
    }

    private Node createButtonsBox() {
        createNewProjectButton = new MaterialButton("Create");
        createNewProjectButton.setDefaultButton(true);
        cancelButton = new MaterialButton("Cancel");
        cancelButton.setCancelButton(true);
        HBox hBox = new HBox(20, createNewProjectButton, cancelButton);
        HBox.setHgrow(hBox, Priority.NEVER);
        hBox.setAlignment(Pos.TOP_RIGHT);
        return hBox;
    }

    private void initFields() {
        projectNameField = new TextField();
        projectPathField = new TextField();
        pathChooserButton = new MaterialButton("...");
        startDatePicker = new DatePicker();
        finishDatePicker = new DatePicker();
        descriptionTextArea = new TextArea();
    }

    private void initLabels() {
        projectNameLabel = new Label("Name");
        projectPathLabel = new Label("Path");
        startDateLabel = new Label("Start Date");
        finishDateLabel = new Label("Finish Date");
        descriptionLabel = new Label("Description");
    }

    public Stage getStage() {
        return stage;
    }

    public TextField getProjectNameField() {
        return projectNameField;
    }

    public TextField getProjectPathField() {
        return projectPathField;
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

    public Button getPathChooserButton() {
        return pathChooserButton;
    }

    public Button getCreateNewProjectButton() {
        return createNewProjectButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public static void main(String[] args) {
        launch();
    }
}
