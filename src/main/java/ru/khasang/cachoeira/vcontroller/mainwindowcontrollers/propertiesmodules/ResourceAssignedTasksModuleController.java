package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules.objects.TaskCheckBoxCell;
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
        module.setItems(controller.getProject().getTaskList());
        module.getTaskNameColumn().setCellValueFactory(cell -> cell.getValue().nameProperty());

        resourceChangeListener = this::refreshCheckBoxColumn;
        taskListChangeListener = this::refreshCheckBoxColumn;

        controller.selectedResourceProperty().addListener(resourceChangeListener);
        controller.getProject().getTaskList().addListener(taskListChangeListener);
    }

    private void refreshCheckBoxColumn(ObservableValue<? extends IResource> observableValue,
                                       IResource oldResourceItem,
                                       IResource newResourceItem) {
        refreshCheckBoxColumn(newResourceItem);
    }

    private void refreshCheckBoxColumn(ListChangeListener.Change<? extends ITask> change) {
        while (change.next()) {
            refreshCheckBoxColumn(controller.getSelectedResource());
        }
    }

    private void refreshCheckBoxColumn(IResource selectedResource) {
        if (selectedResource != null) {
            module.getTaskCheckboxColumn().setCellFactory(cellData -> new TaskCheckBoxCell(selectedResource));
        }
    }
}
