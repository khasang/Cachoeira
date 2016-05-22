package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules.objects.ResourceCheckBoxCell;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.TaskAssignedResources;

public class TaskAssignedResourcesModuleController implements ModuleController {
    private final TaskAssignedResources module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<ITask> taskChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;

    public TaskAssignedResourcesModuleController(IModule module, MainWindowController controller) {
        this.module = (TaskAssignedResources) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        // set disable when selected task is null
        module.disableProperty().bind(controller.selectedTaskProperty().isNull());
        // table view things
        module.setItems(controller.getProject().getResourceList());
        module.getResourceNameColumn().setCellValueFactory(cell -> cell.getValue().nameProperty());
        // init listeners
        taskChangeListener = this::refreshCheckBoxColumn;
        taskListChangeListener = this::refreshCheckBoxColumn;
        // set listeners, when selected resource or project task list has changed refresh checkbox column
        controller.selectedTaskProperty().addListener(taskChangeListener);
        controller.getProject().getTaskList().addListener(taskListChangeListener);
    }

    private void refreshCheckBoxColumn(ObservableValue<? extends ITask> observableValue,
                                       ITask oldTask,
                                       ITask newTask) {
        refreshCheckBoxColumn(newTask);
    }

    private void refreshCheckBoxColumn(ListChangeListener.Change<? extends ITask> change) {
        while (change.next()) {
            refreshCheckBoxColumn(controller.getSelectedTask());
        }
    }

    private void refreshCheckBoxColumn(ITask selectedTask) {
        if (selectedTask != null) {
            module.getResourceCheckboxColumn().setCellFactory(cellData -> new ResourceCheckBoxCell(controller));
        }
    }
}
