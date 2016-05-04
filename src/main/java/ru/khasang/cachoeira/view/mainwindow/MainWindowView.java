package ru.khasang.cachoeira.view.mainwindow;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.GanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.ResourceGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.TaskGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;
import ru.khasang.cachoeira.view.mainwindow.diagram.ResourcePane;
import ru.khasang.cachoeira.view.mainwindow.diagram.TableAndGanttPane;
import ru.khasang.cachoeira.view.mainwindow.diagram.TaskPane;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.ResourceTableView;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.TaskTableView;
import ru.khasang.cachoeira.view.mainwindow.properties.ProjectPropertiesPane;
import ru.khasang.cachoeira.view.mainwindow.properties.ResourcePropertiesPane;
import ru.khasang.cachoeira.view.mainwindow.properties.TaskPropertiesPane;

import java.time.LocalDate;
import java.util.Arrays;

public class MainWindowView {
    private final MainWindowController controller;
    private final IProject project;

    private Stage stage;

    private AbstractTableView<ITask> taskTableView;
    private AbstractTableView<IResource> resourceTableView;

    private TreeTableColumn<ITask, String> taskNameColumn;
    private TreeTableColumn<ITask, LocalDate> taskStartDateColumn;
    private TreeTableColumn<ITask, LocalDate> taskFinishDateColumn;
    private TreeTableColumn<ITask, Integer> taskDurationColumn;
    private TreeTableColumn<ITask, Integer> taskDonePercentColumn;
    private TreeTableColumn<ITask, PriorityType> taskPriorityColumn;
    private TreeTableColumn<ITask, Double> taskCostColumn;

    private TreeTableColumn<IResource, String> resourceNameColumn;
    private TreeTableColumn<IResource, ResourceType> resourceTypeColumn;
    private TreeTableColumn<IResource, String> resourceEmailColumn;

    private GanttPlan taskGanttPlan;
    private GanttPlan resourceGanttPlan;

    private ProjectPropertiesPane projectPropertiesPane;
    private TaskPropertiesPane taskPropertiesPane;
    private ResourcePropertiesPane resourcePropertiesPane;

    public MainWindowView(MainWindowController controller, IProject project) {
        this.controller = controller;
        this.project = project;
    }

    public void createView() {
        BorderPane borderPane = new BorderPane();
//        borderPane.getStylesheets().add(getClass().getResource("/css/startwindow.css").toExternalForm());
        borderPane.setTop(createMenuBar());
        borderPane.setCenter(createGanttPlanLayout());
        borderPane.setRight(createPropertiesMenu());

        stage = new Stage();
        stage.setHeight(controller.getHeightOfWindow());
        stage.setWidth(controller.getWidthOfWindow());
        stage.setScene(new Scene(borderPane));
        stage.show();
        stage.setMaximized(controller.getIsMaximized());
        stage.titleProperty().bind(project.nameProperty());
    }

    private Node createPropertiesMenu() {
        projectPropertiesPane = new ProjectPropertiesPane();
        projectPropertiesPane.createPane();
        resourcePropertiesPane = new ResourcePropertiesPane();
        resourcePropertiesPane.createPane();
        TabPane tabPane = new TabPane(new Tab("Project", projectPropertiesPane), new Tab("Task"), new Tab("Resource", resourcePropertiesPane));
        tabPane.setPrefWidth(310);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }

    private Node createGanttPlanLayout() {
        TableAndGanttPane taskPane = new TaskPane(
                createTaskTable(controller.taskHorizontalScrollValueProperty(), controller.taskVerticalScrollValueProperty()),
                createTaskGattPlan());
        taskPane.createPane();
        TableAndGanttPane resourcePane = new ResourcePane(
                createResourceTable(controller.resourceHorizontalScrollValueProperty(), controller.resourceVerticalScrollValueProperty()),
                createResourceGanttPlan());
        resourcePane.createPane();

        Tab tasksTab = new Tab("Tasks", taskPane);
        tasksTab.setClosable(false);
        Tab resourcesTab = new Tab("Resources", resourcePane);
        resourcesTab.setClosable(false);
        return new TabPane(tasksTab, resourcesTab);
    }

    private Node createMenuBar() {
        MainMenuBar mainMenuBar = new MainMenuBar();
        mainMenuBar.createMenu();
        return mainMenuBar;
    }

    private AbstractTableView<ITask> createTaskTable(DoubleProperty horizontalScrollValue, DoubleProperty verticalScrollValue) {
        taskNameColumn = new TreeTableColumn<>("Name");
        taskStartDateColumn = new TreeTableColumn<>("Start Date");
        taskFinishDateColumn = new TreeTableColumn<>("Finish Date");
        taskDurationColumn = new TreeTableColumn<>("Duration");
        taskDonePercentColumn = new TreeTableColumn<>("Progress");
        taskPriorityColumn = new TreeTableColumn<>("Priority");
        taskCostColumn = new TreeTableColumn<>("Cost");

        taskDurationColumn.setVisible(false);
        taskPriorityColumn.setVisible(false);
        taskDonePercentColumn.setVisible(false);
        taskCostColumn.setVisible(false);

        taskNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskDurationColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskDonePercentColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskPriorityColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskCostColumn.setStyle("-fx-alignment: CENTER-LEFT");

        taskTableView = new TaskTableView<>(horizontalScrollValue, verticalScrollValue);
        taskTableView.bindScrollsToController();
        taskTableView.getColumns().addAll(Arrays.asList(taskNameColumn, taskStartDateColumn, taskFinishDateColumn, taskDurationColumn, taskDonePercentColumn, taskPriorityColumn, taskCostColumn));
        taskTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        taskTableView.setRoot(new TreeItem<>(new Task()));
        taskTableView.getRoot().setExpanded(true);
        taskTableView.setShowRoot(false);
        return taskTableView;
    }

    private AbstractTableView<IResource> createResourceTable(DoubleProperty horizontalScrollValue, DoubleProperty verticalScrollValue) {
        resourceNameColumn = new TreeTableColumn<>("Name");
        resourceTypeColumn = new TreeTableColumn<>("Type");
        resourceEmailColumn = new TreeTableColumn<>("E-Mail");

        resourceEmailColumn.setVisible(false);

        resourceNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceTypeColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceEmailColumn.setStyle("-fx-alignment: CENTER-LEFT");

        resourceTableView = new ResourceTableView<>(horizontalScrollValue, verticalScrollValue);
        resourceTableView.bindScrollsToController();
        resourceTableView.getColumns().addAll(Arrays.asList(resourceNameColumn, resourceTypeColumn, resourceEmailColumn));
        resourceTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        resourceTableView.setRoot(new TreeItem<>(new Resource()));
        resourceTableView.getRoot().setExpanded(true);
        resourceTableView.setShowRoot(false);
        return resourceTableView;
    }

    private GanttPlan createTaskGattPlan() {
        taskGanttPlan = new TaskGanttPlan(controller);
        taskGanttPlan.initGanttDiagram();
        return taskGanttPlan;
    }

    private GanttPlan createResourceGanttPlan() {
        resourceGanttPlan = new ResourceGanttPlan(controller);
        resourceGanttPlan.initGanttDiagram();
        return resourceGanttPlan;
    }

    public Stage getStage() {
        return stage;
    }

    public AbstractTableView<ITask> getTaskTableView() {
        return taskTableView;
    }

    public AbstractTableView<IResource> getResourceTableView() {
        return resourceTableView;
    }

    public TreeTableColumn<ITask, String> getTaskNameColumn() {
        return taskNameColumn;
    }

    public TreeTableColumn<ITask, LocalDate> getTaskStartDateColumn() {
        return taskStartDateColumn;
    }

    public TreeTableColumn<ITask, LocalDate> getTaskFinishDateColumn() {
        return taskFinishDateColumn;
    }

    public TreeTableColumn<ITask, Integer> getTaskDurationColumn() {
        return taskDurationColumn;
    }

    public TreeTableColumn<ITask, Integer> getTaskDonePercentColumn() {
        return taskDonePercentColumn;
    }

    public TreeTableColumn<ITask, PriorityType> getTaskPriorityColumn() {
        return taskPriorityColumn;
    }

    public TreeTableColumn<ITask, Double> getTaskCostColumn() {
        return taskCostColumn;
    }

    public TreeTableColumn<IResource, String> getResourceNameColumn() {
        return resourceNameColumn;
    }

    public TreeTableColumn<IResource, ResourceType> getResourceTypeColumn() {
        return resourceTypeColumn;
    }

    public TreeTableColumn<IResource, String> getResourceEmailColumn() {
        return resourceEmailColumn;
    }

    public GanttPlan getTaskGanttPlan() {
        return taskGanttPlan;
    }

    public GanttPlan getResourceGanttPlan() {
        return resourceGanttPlan;
    }
}
