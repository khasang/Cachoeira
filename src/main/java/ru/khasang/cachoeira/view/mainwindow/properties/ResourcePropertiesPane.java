package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;

import java.util.Arrays;

public class ResourcePropertiesPane extends VBox {
    private static final double FIRST_COLUMN_PERCENT_WIDTH = 30;
    private static final double SECOND_COLUMN_PERCENT_WIDTH = 70;

    private Label nameLabel;
    private Label emailLabel;
    private Label resourceTypeLabel;
    private Label descriptionLabel;

    private TextField nameField;
    private TextField emailField;
    private ComboBox<ResourceType> resourceTypeComboBox;
    private TextArea descriptionTextArea;

    private TableView<ITask> assignedTaskTable;
    private TableColumn<ITask, String> taskNameColumn;
    private TableColumn<ITask, Boolean> taskCheckboxColumn;

    public void createPane() {
        this.getChildren().add(createInformationPane());
        this.getChildren().add(createAssignedTasksPane());
    }

    private TitledPane createAssignedTasksPane() {
        assignedTaskTable = new TableView<>();
        taskNameColumn = new TableColumn<>("Name");
        taskCheckboxColumn = new TableColumn<>();

        taskNameColumn.setPrefWidth(251);
        taskCheckboxColumn.setPrefWidth(47);

        assignedTaskTable.getColumns().addAll(Arrays.asList(taskNameColumn, taskCheckboxColumn));
        assignedTaskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return new TitledPane("Assigned Tasks", assignedTaskTable);
    }

    private TitledPane createInformationPane() {
        initLabelsAndFields();

        ColumnConstraints firstColumnConstrains = new ColumnConstraints();
        ColumnConstraints secondColumnConstrains = new ColumnConstraints();
        firstColumnConstrains.setPercentWidth(FIRST_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setPercentWidth(SECOND_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setHgrow(Priority.ALWAYS);

        GridPane gridPane = new GridPane();
        gridPane.getColumnConstraints().addAll(firstColumnConstrains, secondColumnConstrains);
        gridPane.addRow(0, nameLabel, nameField);
        gridPane.addRow(1, emailLabel, emailField);
        gridPane.addRow(2, resourceTypeLabel, resourceTypeComboBox);
        gridPane.addRow(3, descriptionLabel, descriptionTextArea);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        emailField.setPrefWidth(350);
        resourceTypeComboBox.setPrefWidth(350);
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        return new TitledPane("Information", gridPane);
    }

    private void initLabelsAndFields() {
        nameLabel = new Label("Name");
        emailLabel = new Label("E-Mail");
        resourceTypeLabel = new Label("Resource Type");
        descriptionLabel = new Label("Description");

        nameField = new TextField();
        emailField = new TextField();
        resourceTypeComboBox = new ComboBox<>();
        descriptionTextArea = new TextArea();
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public ComboBox<ResourceType> getResourceTypeComboBox() {
        return resourceTypeComboBox;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public TableView<ITask> getAssignedTaskTable() {
        return assignedTaskTable;
    }

    public TableColumn<ITask, String> getTaskNameColumn() {
        return taskNameColumn;
    }

    public TableColumn<ITask, Boolean> getTaskCheckboxColumn() {
        return taskCheckboxColumn;
    }
}
