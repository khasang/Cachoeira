package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TreeItem;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.ResourceAssignedTasks;

public class ResourceAssignedTasksModuleController implements ModuleController {
    private final ResourceAssignedTasks module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<IResource>> resourceChangeListener;

    public ResourceAssignedTasksModuleController(IModule module, MainWindowController controller) {
        this.module = (ResourceAssignedTasks) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        module.setItems(controller.getProject().getTaskList());
        module.getTaskNameColumn().setCellValueFactory(cell -> cell.getValue().nameProperty());

        resourceChangeListener = this::selectedResourceObserver;
        controller.getResourceTableView().getSelectionModel().selectedItemProperty().addListener(
                new WeakChangeListener<>(resourceChangeListener));
    }

    private void selectedResourceObserver(ObservableValue<? extends TreeItem<IResource>> observableValue,
                                          TreeItem<IResource> oldResourceItem,
                                          TreeItem<IResource> newResourceItem) {
        if (newResourceItem.getValue() != null) {
            module.getTaskCheckboxColumn().setCellFactory(column -> columnFactory(newResourceItem.getValue()));
        }
    }

    private TableCell<ITask, Boolean> columnFactory(IResource selectedResource) {
        return new TableCell<ITask, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                ITask currentRowTask = (ITask) getTableRow().getItem();
                if (empty) {
                    this.setText(null);
                    this.setGraphic(null);
                } else {
                    // Заполняем столбец чек-боксами
                    CheckBox checkBox = new CheckBox();
                    this.setGraphic(checkBox);
                    checkBox.setOnAction(event -> {
                        if (checkBox.isSelected()) {
                            CommandControl.getInstance().execute(
                                    new AddResourceToTaskCommand(currentRowTask, selectedResource));
                        } else {
                            CommandControl.getInstance().execute(
                                    new RemoveResourceFromTaskCommand(currentRowTask, selectedResource));
                        }
                    });

                    // Расставляем галочки на нужных строках
                    if (currentRowTask != null) {
                        currentRowTask.getResourceList()
                                .stream()
                                .filter(resource -> selectedResource.equals(resource) && !checkBox.isSelected())
                                .forEach(resource -> checkBox.setSelected(true));
                    }
                }
            }
        };
    }
}
