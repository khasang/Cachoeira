package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules.objects.TaskCheckBoxCell;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.ResourceAssignedTasks;

public class ResourceAssignedTasksModuleController implements ModuleController {
    private final ResourceAssignedTasks module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<IResource> resourceChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;

    public ResourceAssignedTasksModuleController(IModule module, MainWindowController controller) {
        this.module = (ResourceAssignedTasks) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        // set disable when selected resource is null
        module.disableProperty().bind(controller.selectedResourceProperty().isNull());
        // table view things
        module.setItems(controller.getProject().getTaskList());
        module.getTaskNameColumn().setCellValueFactory(cell -> cell.getValue().nameProperty());
        // init listeners
        resourceChangeListener = this::refreshCheckBoxColumn;
        taskListChangeListener = this::refreshCheckBoxColumn;
        // set listeners, when selected resource or project task list has changed refresh checkbox column
        controller.selectedResourceProperty().addListener(resourceChangeListener);
        controller.getProject().getTaskList().addListener(taskListChangeListener);
    }

    private void refreshCheckBoxColumn(ObservableValue<? extends IResource> observableValue,
                                       IResource oldResource,
                                       IResource newResource) {
        refreshCheckBoxColumn(newResource);
    }

    private void refreshCheckBoxColumn(ListChangeListener.Change<? extends ITask> change) {
        while (change.next()) {
            refreshCheckBoxColumn(controller.getSelectedResource());
        }
    }

    private void refreshCheckBoxColumn(IResource selectedResource) {
        if (selectedResource != null) {
            module.getTaskCheckboxColumn().setCellFactory(cellData -> new TaskCheckBoxCell(controller));
        }
    }
}
