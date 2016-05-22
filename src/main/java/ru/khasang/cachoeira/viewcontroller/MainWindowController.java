package ru.khasang.cachoeira.viewcontroller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.commands.CommandExecutor;
import ru.khasang.cachoeira.commands.project.*;
import ru.khasang.cachoeira.data.DataService;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.properties.ISettingsManager;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.properties.SettingsManager;
import ru.khasang.cachoeira.viewcontroller.contextmenus.ColumnContextMenu;
import ru.khasang.cachoeira.viewcontroller.draganddrop.ResourceTableDragAndDrop;
import ru.khasang.cachoeira.viewcontroller.draganddrop.TaskTableDragAndDrop;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.buttonsbar.ButtonsBoxController;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.MenuBarController;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.buttonsbar.ResourceButtonsBoxController;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.buttonsbar.TaskButtonsBoxController;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules.*;
import ru.khasang.cachoeira.viewcontroller.rowfactories.ResourceTreeTableViewRowFactory;
import ru.khasang.cachoeira.viewcontroller.rowfactories.TaskTreeTableViewRowFactory;
import ru.khasang.cachoeira.view.IView;
import ru.khasang.cachoeira.view.mainwindow.MainWindowView;
import ru.khasang.cachoeira.view.mainwindow.diagram.ResourcePane;
import ru.khasang.cachoeira.view.mainwindow.diagram.TaskPane;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.ResourcesButtonBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.TasksButtonBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.AbstractGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.ResourceGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.TaskGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.ResourceTableView;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.TaskTableView;
import ru.khasang.cachoeira.view.mainwindow.menubar.AbstractMenuBar;
import ru.khasang.cachoeira.view.mainwindow.menubar.MainMenuBar;
import ru.khasang.cachoeira.view.mainwindow.properties.AbstractPropertiesSidePanel;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.*;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.ProjectPropertiesPane;
import ru.khasang.cachoeira.view.mainwindow.properties.PropertiesSidePanel;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.ResourcePropertiesPane;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.TaskPropertiesPane;

import java.io.File;

public class MainWindowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowController.class.getName());

    private File projectFile;
    private final IProject project;
    private final CommandExecutor commandExecutor;
    private final IView view;

    private ObjectProperty<ITask> selectedTask = new SimpleObjectProperty<>(this, "selectedTask", null);
    private ObjectProperty<IResource> selectedResource = new SimpleObjectProperty<>(this, "selectedResource", null);

    private IntegerProperty zoomMultiplier = new SimpleIntegerProperty(this, "zoomMultiplier", 70);
    private DoubleProperty ganttHorizontalScrollValue = new SimpleDoubleProperty(this, "ganttHorizontalScrollValue", 0);
    private DoubleProperty taskVerticalScrollValue = new SimpleDoubleProperty(this, "taskVerticalScrollValue", 0);
    private DoubleProperty resourceVerticalScrollValue = new SimpleDoubleProperty(this, "resourceVerticalScrollValue", 0);
    private DoubleProperty taskHorizontalScrollValue = new SimpleDoubleProperty(this, "taskHorizontalScrollValue", 0);
    private DoubleProperty resourceHorizontalScrollValue = new SimpleDoubleProperty(this, "resourceHorizontalScrollValue", 0);
    private DoubleProperty splitPaneDividerValue = new SimpleDoubleProperty(this, "splitPaneDividerValue", 0.15);

    private DoubleProperty widthOfWindow = new SimpleDoubleProperty(this, "widthOfWindow", 1280);
    private DoubleProperty heightOfWindow = new SimpleDoubleProperty(this, "heightOfWindow", 720);
    private BooleanProperty isMaximized = new SimpleBooleanProperty(this, "isMaximized", false);

    private AbstractMenuBar menuBar;

    private SplitPane taskSplitPane;
    private SplitPane resourceSplitPane;

    private AbstractTableView<ITask> taskTableView;
    private AbstractTableView<IResource> resourceTableView;

    private AbstractGanttPlan taskGanttPlan;
    private AbstractGanttPlan resourceGanttPlan;

    private ProjectInformation projectInformation;
    private TaskInformation taskInformation;
    private TaskAssignedResources taskAssignedResources;
    private TaskDependencies taskDependencies;
    private ResourceInformation resourceInformation;
    private ResourceAssignedTasks resourceAssignedTasks;

    private AbstractButtonsBox tasksButtonsBox;
    private AbstractButtonsBox resourcesButtonBox;

    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IResource> resourceListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener projectDateListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<ITask>> taskChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<IResource>> resourceChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<TreeItem<ITask>> taskTreeItemsChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<TreeItem<IResource>> resourceTreeItemsChangeListener;

    public MainWindowController(File projectFile, IProject project, CommandExecutor commandExecutor) {
        this.projectFile = projectFile;
        this.project = project;
        this.commandExecutor = commandExecutor;
        view = createMainWindow();
        projectDateListener = this::projectDateObserver;
        project.startDateProperty().addListener(new WeakInvalidationListener(projectDateListener));
        project.finishDateProperty().addListener(new WeakInvalidationListener(projectDateListener));
    }

    private void projectDateObserver(Observable observable) {
        taskGanttPlan.getObjectsLayer().refreshPlan();
        resourceGanttPlan.getObjectsLayer().refreshPlan();
    }

    public void launch() {
        view.createView();

        MenuBarController menuBarController = new MenuBarController(menuBar, this);
        menuBarController.attachMenuBarEvents();

        ButtonsBoxController taskButtonsBoxController = new TaskButtonsBoxController(tasksButtonsBox, this);
        taskButtonsBoxController.attachButtonsEvents();
        ButtonsBoxController resourceButtonsBoxController = new ResourceButtonsBoxController(resourcesButtonBox, this);
        resourceButtonsBoxController.attachButtonsEvents();

        ModuleController projectInformationModuleController = new ProjectInformationModuleController(projectInformation, this);
        projectInformationModuleController.initModule();

        ModuleController taskInformationModuleController = new TaskInformationModuleController(taskInformation, this);
        taskInformationModuleController.initModule();

        TaskAssignedResourcesModuleController taskAssignedResourcesModuleController = new TaskAssignedResourcesModuleController(taskAssignedResources, this);
        taskAssignedResourcesModuleController.initModule();

        TaskDependenciesModuleController taskDependenciesModuleController = new TaskDependenciesModuleController(taskDependencies, this);
        taskDependenciesModuleController.initModule();

        ModuleController resourceInformationModuleController = new ResourceInformationModuleController(resourceInformation, this);
        resourceInformationModuleController.initModule();

        ResourceAssignedTasksModuleController resourceAssignedTasksModuleController = new ResourceAssignedTasksModuleController(resourceAssignedTasks, this);
        resourceAssignedTasksModuleController.initModule();

        this.attachGanttPlanEvents();
        this.attachContextMenus();
        view.getStage().setOnCloseRequest(event -> {
            this.saveProperties();
            this.saveProject(event);
        });
        LOGGER.debug("Launched.");
    }

    private void saveProperties() {
        ISettingsManager settingsManager = SettingsManager.getInstance();
        settingsManager.writeUIValues(
                splitPaneDividerValue.get(),
                zoomMultiplier.getValue(),
                view.getStage().getWidth(),
                view.getStage().getHeight(),
                view.getStage().isMaximized());
        settingsManager.writeRecentProjects(RecentProjectsController.getInstance().getRecentProjects());
    }

    private void saveProject(WindowEvent event) {
        //минимум JDK 8u40
        if (commandExecutor.isChanged()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cachoeira");
            alert.setHeaderText("Вы хотите сохранить изменения в " + project.getName() + "?");

            ButtonType saveProjectButtonType = new ButtonType("Сохранить");
            ButtonType doNotSaveProjectButtonType = new ButtonType("Не сохранять");
            ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveProjectButtonType, doNotSaveProjectButtonType, cancelButtonType);

            alert.showAndWait().ifPresent(response -> {
                if (response == saveProjectButtonType) {
                    DataService.getInstance().saveProject(projectFile, project);
                    view.getStage().close();
                } else if (response == doNotSaveProjectButtonType) {
                    view.getStage().close();
                } else {
                    if (event != null) {
                        event.consume();
                    }
                }
            });
        } else {
            view.getStage().close();
        }
    }

    private void attachGanttPlanEvents() {
        taskSplitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneDividerValue);
        resourceSplitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneDividerValue);
    }

    private void attachContextMenus() {
        // Контекстное меню для выбора нужных столбцов
        ColumnContextMenu columnContextMenuTask = new ColumnContextMenu(taskTableView);
        columnContextMenuTask.setOnShowing(event -> columnContextMenuTask.updateContextMenuColumnTTV(taskTableView));
        taskTableView.getColumns().forEach(column -> column.setContextMenu(columnContextMenuTask));

        // Контекстное меню для таблицы
        ContextMenu taskTableMenu = new ContextMenu();
        MenuItem addNewTask = new MenuItem("Новая задача");
        addNewTask.setOnAction(event -> commandExecutor.execute(new AddTaskToProjectCommand(project, new Task())));
        taskTableMenu.getItems().addAll(addNewTask);   //заполняем меню
        taskTableView.setContextMenu(taskTableMenu);

        ColumnContextMenu columnContextMenuResource = new ColumnContextMenu(resourceTableView);
        columnContextMenuResource.setOnShowing(event -> columnContextMenuResource.updateContextMenuColumnTTV(resourceTableView));
        resourceTableView.getColumns().forEach(column -> column.setContextMenu(columnContextMenuResource));

        // Контекстное меню для таблицы
        ContextMenu resourceTableMenu = new ContextMenu();
        MenuItem addNewResource = new MenuItem("Новый ресурс");
        addNewResource.setOnAction(event -> commandExecutor.execute(new AddResourceToProjectCommand(project, new Resource())));
        resourceTableMenu.getItems().addAll(addNewResource);   //заполняем меню
        resourceTableView.setContextMenu(resourceTableMenu);
    }

    private MainWindowView createMainWindow() {
        // Выгружаем настройки программы из файлов
        if (SettingsManager.getInstance().getUIValueByKey("ZoomValue") != null) {
            zoomMultiplier.setValue(Double.valueOf(SettingsManager.getInstance().getUIValueByKey("ZoomValue")).intValue());
        }
        if (SettingsManager.getInstance().getUIValueByKey("DiagramDividerValue") != null) {
            splitPaneDividerValue.setValue(Double.valueOf(SettingsManager.getInstance().getUIValueByKey("DiagramDividerValue")));
        }
        if (SettingsManager.getInstance().getUIValueByKey("WidthOfWindow") != null) {
            widthOfWindow.setValue(Double.valueOf(SettingsManager.getInstance().getUIValueByKey("WidthOfWindow")));
        }
        if (SettingsManager.getInstance().getUIValueByKey("HeightOfWindow") != null) {
            heightOfWindow.setValue(Double.valueOf(SettingsManager.getInstance().getUIValueByKey("HeightOfWindow")));
        }
        isMaximized.setValue(isMaximized());

        taskGanttPlan = new TaskGanttPlan(this);
        resourceGanttPlan = new ResourceGanttPlan(this);
        menuBar = new MainMenuBar();

        AbstractPropertiesSidePanel sidePanel = new PropertiesSidePanel(new ProjectPropertiesPane(), new TaskPropertiesPane(), new ResourcePropertiesPane());

        projectInformation = (ProjectInformation) createPropertiesModule(new ProjectInformation());
        sidePanel.getProjectPropertiesPane().addNewPropertiesTitledModule(projectInformation, "Information");

        taskInformation = (TaskInformation) createPropertiesModule(new TaskInformation());
        sidePanel.getTaskPropertiesPane().addNewPropertiesTitledModule(taskInformation, "Information");
        taskAssignedResources = (TaskAssignedResources) createPropertiesModule(new TaskAssignedResources());
        sidePanel.getTaskPropertiesPane().addNewPropertiesTitledModule(taskAssignedResources, "Assigned Resources");
        taskDependencies = (TaskDependencies) createPropertiesModule(new TaskDependencies());
        sidePanel.getTaskPropertiesPane().addNewPropertiesTitledModule(taskDependencies, "Dependencies");

        resourceInformation = (ResourceInformation) createPropertiesModule(new ResourceInformation());
        sidePanel.getResourcePropertiesPane().addNewPropertiesTitledModule(resourceInformation, "Information");
        resourceAssignedTasks = (ResourceAssignedTasks) createPropertiesModule(new ResourceAssignedTasks());
        sidePanel.getResourcePropertiesPane().addNewPropertiesTitledModule(resourceAssignedTasks, "Assigned Tasks");

        tasksButtonsBox = new TasksButtonBox();
        resourcesButtonBox = new ResourcesButtonBox();

        taskSplitPane = new SplitPane();
        resourceSplitPane = new SplitPane();

        return new MainWindowView(
                this,
                project,
                menuBar,
                sidePanel,
                new TaskPane(
                        this,
                        createTaskTable(
                                taskHorizontalScrollValueProperty(),
                                taskVerticalScrollValueProperty()),
                        taskGanttPlan,
                        tasksButtonsBox,
                        taskSplitPane),
                new ResourcePane(
                        this,
                        createResourceTable(
                                resourceHorizontalScrollValueProperty(),
                                resourceVerticalScrollValueProperty()),
                        resourceGanttPlan,
                        resourcesButtonBox,
                        resourceSplitPane));
    }

    private boolean isMaximized() {
        return SettingsManager.getInstance().getUIValueByKey("MaximizedWindow") != null &&
                SettingsManager.getInstance().getUIValueByKey("MaximizedWindow").equals("1");
    }

    private IModule createPropertiesModule(IModule module) {
        module.createPane();
        return module;
    }

    private AbstractTableView<ITask> createTaskTable(DoubleProperty horizontalScrollValue,
                                                     DoubleProperty verticalScrollValue) {
        taskTableView = new TaskTableView<>(horizontalScrollValue, verticalScrollValue);

        taskTableView.setRoot(new TreeItem<>(new Task()));
        taskTableView.setRowFactory(new TaskTreeTableViewRowFactory(this, new TaskTableDragAndDrop(this)));
        taskTableView.setRowFactory(new TaskTreeTableViewRowFactory(this, new TaskTableDragAndDrop(this)));
        taskListChangeListener = this::taskChangeListenerHandler;
        project.getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));

        taskChangeListener = this::refreshSelectedTask;
        taskTableView.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<>(taskChangeListener));

        taskTreeItemsChangeListener = this::setSelectedTaskNull;
        taskTableView.getRoot().getChildren().addListener(new WeakListChangeListener<>(taskTreeItemsChangeListener));
        return taskTableView;
    }

    private void setSelectedTaskNull(ListChangeListener.Change<? extends TreeItem<ITask>> change) {
        while (change.next()) {
            if (change.getList().isEmpty()) {
                selectedTask.setValue(null);
            }
        }
    }

    private void refreshSelectedTask(ObservableValue<? extends TreeItem<ITask>> observableValue,
                                     TreeItem<ITask> oldTaskTreeItem,
                                     TreeItem<ITask> newTaskTreeItem) {
        if (newTaskTreeItem != null) {
            selectedTask.setValue(newTaskTreeItem.getValue());
        }
    }

    private AbstractTableView<IResource> createResourceTable(DoubleProperty horizontalScrollValue,
                                                             DoubleProperty verticalScrollValue) {
        resourceTableView = new ResourceTableView<>(horizontalScrollValue, verticalScrollValue);

        resourceTableView.setRoot(new TreeItem<>(new Resource()));
        resourceTableView.setRowFactory(new ResourceTreeTableViewRowFactory(this, new ResourceTableDragAndDrop(this)));
        resourceListChangeListener = this::resourceChangeListenerHandler;
        project.getResourceList().addListener(new WeakListChangeListener<>(resourceListChangeListener));

        resourceChangeListener = this::refreshSelectedResource;
        resourceTableView.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<>(resourceChangeListener));

        resourceTreeItemsChangeListener = this::setSelectedResourceNull;
        resourceTableView.getRoot().getChildren().addListener(new WeakListChangeListener<>(resourceTreeItemsChangeListener));
        return resourceTableView;
    }

    private void setSelectedResourceNull(ListChangeListener.Change<? extends TreeItem<IResource>> change) {
        while (change.next()) {
            if (change.getList().isEmpty()) {
                selectedResource.setValue(null);
            }
        }
    }

    private void refreshSelectedResource(ObservableValue<? extends TreeItem<IResource>> observableValue,
                                         TreeItem<IResource> oldResourceTreeItem,
                                         TreeItem<IResource> newResourceTreeItem) {
        if (newResourceTreeItem != null) {
            selectedResource.setValue(newResourceTreeItem.getValue());
        }
    }

    private void resourceChangeListenerHandler(ListChangeListener.Change<? extends IResource> change) {
        // При добавлении или удалении элемента из модели обновляем таблицу задач
        while (change.next()) {
            // Добавляем
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(resource -> {
                    // Получаем индекс задачи
                    int indexOfTask = project.getResourceList().indexOf(resource);
                    // Создаем элемент таблицы и присваиваем ему нашу задачу
                    TreeItem<IResource> resourceTreeItem = new TreeItem<>(resource);
                    // Добавляем элемент в таблицу на нужную строку (indexOfTask)
                    resourceTableView.getRoot().getChildren().add(indexOfTask, resourceTreeItem); //обязательно нужен индекс элемента, иначе драгндроп не будет работать
                    // Выделяем добавленный элемент в таблице
                    resourceTableView.getSelectionModel().select(resourceTreeItem);
                });
            }
            // Удаляем
            if (change.wasRemoved()) {
                change.getRemoved().forEach(resource -> {
                    // Сначала удаляем из таблицы...
                    removeTreeItem(resource, resourceTableView.getRoot().getChildren());
                });
            }
        }
    }

    private void taskChangeListenerHandler(ListChangeListener.Change<? extends ITask> change) {
        // При добавлении или удалении элемента из модели обновляем таблицу задач
        while (change.next()) {
            // Добавляем
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(task -> {
                    // Получаем индекс задачи
                    int indexOfTask = project.getTaskList().indexOf(task);
                    // Создаем элемент таблицы и присваиваем ему нашу задачу
                    TreeItem<ITask> taskTreeItem = new TreeItem<>(task);
                    // Добавляем элемент в таблицу на нужную строку (indexOfTask)
                    taskTableView.getRoot().getChildren().add(indexOfTask, taskTreeItem); //обязательно нужен индекс элемента, иначе драгндроп не будет работать
                    // Выделяем добавленный элемент в таблице
                    taskTableView.getSelectionModel().select(taskTreeItem);
                    // Добавляем задачу на диаграмму
                    taskGanttPlan.getObjectsLayer().addTaskBar(task, null);
                });
            }
            // Удаляем
            if (change.wasRemoved()) {
                change.getRemoved().forEach(task -> {
                    // Сначала удаляем из таблицы...
                    removeTreeItem(task, taskTableView.getRoot().getChildren());
                    // ...а теперь с диаграммы
                    taskGanttPlan.getObjectsLayer().removeTaskBar(task);
                    taskGanttPlan.getSelectedObjectLayer().removeBackgroundTaskBar(task);
                });
            }
        }
    }

    public <T> void removeTreeItem(T object, ObservableList<TreeItem<T>> itemList) {
        itemList.removeIf(item -> item.getValue().equals(object));
    }

    public void refreshInformation() {
        // При загрузке проверяем наличие задач
        for (ITask task : project.getTaskList()) {
            TreeItem<ITask> treeItem = new TreeItem<>(task);
            taskTableView.getRoot().getChildren().add(treeItem);
            taskTableView.getSelectionModel().select(treeItem);
        }

        for (IResource resource : project.getResourceList()) {
            TreeItem<IResource> treeItem = new TreeItem<>(resource);
            resourceTableView.getRoot().getChildren().add(treeItem);
            resourceTableView.getSelectionModel().select(treeItem);
        }

        taskGanttPlan.getObjectsLayer().refreshPlan();
        taskGanttPlan.getRelationsLayer().refreshRelationsDiagram(this);

        resourceGanttPlan.getObjectsLayer().refreshPlan();
    }

    public IProject getProject() {
        return project;
    }

    public IView getView() {
        return view;
    }

    public int getZoomMultiplier() {
        return zoomMultiplier.get();
    }

    public IntegerProperty zoomMultiplierProperty() {
        return zoomMultiplier;
    }

    public double getGanttHorizontalScrollValue() {
        return ganttHorizontalScrollValue.get();
    }

    public DoubleProperty ganttHorizontalScrollValueProperty() {
        return ganttHorizontalScrollValue;
    }

    public double getTaskVerticalScrollValue() {
        return taskVerticalScrollValue.get();
    }

    public DoubleProperty taskVerticalScrollValueProperty() {
        return taskVerticalScrollValue;
    }

    public double getResourceVerticalScrollValue() {
        return resourceVerticalScrollValue.get();
    }

    public DoubleProperty resourceVerticalScrollValueProperty() {
        return resourceVerticalScrollValue;
    }

    public double getTaskHorizontalScrollValue() {
        return taskHorizontalScrollValue.get();
    }

    public DoubleProperty taskHorizontalScrollValueProperty() {
        return taskHorizontalScrollValue;
    }

    public double getResourceHorizontalScrollValue() {
        return resourceHorizontalScrollValue.get();
    }

    public DoubleProperty resourceHorizontalScrollValueProperty() {
        return resourceHorizontalScrollValue;
    }

    public double getSplitPaneDividerValue() {
        return splitPaneDividerValue.get();
    }

    public DoubleProperty splitPaneDividerValueProperty() {
        return splitPaneDividerValue;
    }

    public double getWidthOfWindow() {
        return widthOfWindow.get();
    }

    public DoubleProperty widthOfWindowProperty() {
        return widthOfWindow;
    }

    public double getHeightOfWindow() {
        return heightOfWindow.get();
    }

    public DoubleProperty heightOfWindowProperty() {
        return heightOfWindow;
    }

    public boolean getIsMaximized() {
        return isMaximized.get();
    }

    public BooleanProperty isMaximizedProperty() {
        return isMaximized;
    }

    public AbstractTableView<ITask> getTaskTableView() {
        return taskTableView;
    }

    public AbstractTableView<IResource> getResourceTableView() {
        return resourceTableView;
    }

    public AbstractGanttPlan getTaskGanttPlan() {
        return taskGanttPlan;
    }

    public AbstractGanttPlan getResourceGanttPlan() {
        return resourceGanttPlan;
    }

    public File getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }

    public ITask getSelectedTask() {
        return selectedTask.get();
    }

    public ObjectProperty<ITask> selectedTaskProperty() {
        return selectedTask;
    }

    public IResource getSelectedResource() {
        return selectedResource.get();
    }

    public ObjectProperty<IResource> selectedResourceProperty() {
        return selectedResource;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
