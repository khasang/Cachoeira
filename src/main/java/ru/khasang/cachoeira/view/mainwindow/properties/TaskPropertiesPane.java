package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.model.*;

import java.util.Arrays;

public class TaskPropertiesPane extends VBox {
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

    private TableView<IResource> assignedResourceTable;
    private TableColumn<IResource, String> resourceNameColumn;
    private TableColumn<IResource, Boolean> resourceCheckboxColumn;

    private TableView<IDependentTask> parentTaskTableView;
    private TableColumn<IDependentTask, String> parentTaskNameColumn;
    private TableColumn<IDependentTask, TaskDependencyType> parentTaskDependencyTypeColumn;

    public void createPane() {
        this.getChildren().add(createInformationPane());
        this.getChildren().add(createAssignedResourcesPane());
        this.getChildren().add(createDependenciesTasksPane());
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
        gridPane.addRow(1, startDateLabel, startDatePicker);
        gridPane.addRow(2, finishDateLabel, finishDatePicker);
        gridPane.addRow(3, donePercentLabel, donePercentSlider);
        gridPane.addRow(4, priorityTypeLabel, priorityTypeComboBox);
        gridPane.addRow(5, costLabel, costField);
        gridPane.addRow(6, descriptionLabel, descriptionTextArea);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        startDatePicker.setPrefWidth(350);
        finishDatePicker.setPrefWidth(350);
        priorityTypeComboBox.setPrefWidth(350);
        GridPane.setValignment(descriptionTextArea, VPos.TOP);
        return new TitledPane("Information", gridPane);
    }

    private void initLabelsAndFields() {
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

    private TitledPane createAssignedResourcesPane() {
        assignedResourceTable = new TableView<>();
        resourceNameColumn = new TableColumn<>("Name");
        resourceCheckboxColumn = new TableColumn<>();

        resourceNameColumn.setPrefWidth(251);
        resourceCheckboxColumn.setPrefWidth(47);

        assignedResourceTable.getColumns().addAll(Arrays.asList(resourceNameColumn, resourceCheckboxColumn));
        assignedResourceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return new TitledPane("Assigned Resources", assignedResourceTable);
    }

    private TitledPane createDependenciesTasksPane() {
        parentTaskTableView = new TableView<>();
        parentTaskNameColumn = new TableColumn<>("Name");
        parentTaskDependencyTypeColumn = new TableColumn<>("Dependency Type");

        parentTaskTableView.getColumns().addAll(Arrays.asList(parentTaskNameColumn, parentTaskDependencyTypeColumn));
        parentTaskTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return new TitledPane("Dependencies", parentTaskTableView);
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

    public Slider getDonePercentSlider() {
        return donePercentSlider;
    }

    public ComboBox<PriorityType> getPriorityTypeComboBox() {
        return priorityTypeComboBox;
    }

    public TextField getCostField() {
        return costField;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public TableView<IResource> getAssignedResourceTable() {
        return assignedResourceTable;
    }

    public TableColumn<IResource, String> getResourceNameColumn() {
        return resourceNameColumn;
    }

    public TableColumn<IResource, Boolean> getResourceCheckboxColumn() {
        return resourceCheckboxColumn;
    }

    public TableView<IDependentTask> getParentTaskTableView() {
        return parentTaskTableView;
    }

    public TableColumn<IDependentTask, String> getParentTaskNameColumn() {
        return parentTaskNameColumn;
    }

    public TableColumn<IDependentTask, TaskDependencyType> getParentTaskDependencyTypeColumn() {
        return parentTaskDependencyTypeColumn;
    }
}
