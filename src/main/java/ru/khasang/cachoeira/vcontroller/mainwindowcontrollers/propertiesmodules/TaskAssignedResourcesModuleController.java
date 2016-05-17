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
import ru.khasang.cachoeira.view.mainwindow.properties.modules.TaskAssignedResources;

public class TaskAssignedResourcesModuleController implements ModuleController {
    private final TaskAssignedResources module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<ITask>> taskChangeListener;

    public TaskAssignedResourcesModuleController(IModule module, MainWindowController controller) {
        this.module = (TaskAssignedResources) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        module.setItems(controller.getProject().getResourceList());
        module.getResourceNameColumn().setCellValueFactory(cell -> cell.getValue().nameProperty());

        taskChangeListener = this::selectedTaskObserver;
        controller.getTaskTableView().getSelectionModel().selectedItemProperty().addListener(
                new WeakChangeListener<>(taskChangeListener));
    }

    private void selectedTaskObserver(ObservableValue<? extends TreeItem<ITask>> observableValue,
                                      TreeItem<ITask> oldTaskItem,
                                      TreeItem<ITask> newTaskItem) {
        if (newTaskItem.getValue() != null) {
            module.getResourceCheckboxColumn().setCellFactory(column -> columnFactory(newTaskItem.getValue()));
        }
    }

    private TableCell<IResource, Boolean> columnFactory(ITask selectedTask) {
        return new TableCell<IResource, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                IResource currentRowResource = (IResource) getTableRow().getItem();
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
                                    new AddResourceToTaskCommand(selectedTask, currentRowResource));
                        } else {
                            CommandControl.getInstance().execute(
                                    new RemoveResourceFromTaskCommand(selectedTask, currentRowResource));
                        }
                    });

                    // Расставляем галочки на нужных строках
                    selectedTask.getResourceList()
                            .stream()
                            .filter(resource -> resource.equals(currentRowResource) && !checkBox.isSelected())
                            .forEach(resource -> checkBox.setSelected(true));
                }
            }
        };
    }
}
