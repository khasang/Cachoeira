package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules.objects;

import javafx.scene.control.CheckBox;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

public class TaskCheckBoxCell extends AbstractCheckBoxCell<ITask, Boolean> {
    private MainWindowController controller;

    public TaskCheckBoxCell(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    protected CheckBox createCheckBox() {
        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> handleSelection(checkBox));
        return checkBox;
    }

    @Override
    protected void setSelectedCheckBox(CheckBox checkBox) {
        if (currentRow != null) {
            currentRow.getResourceList()
                    .stream()
                    .filter(resource -> controller.getSelectedResource().equals(resource) && !checkBox.isSelected())
                    .forEach(resource -> checkBox.setSelected(true));
        }
    }

    private void handleSelection(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            controller.getCommandExecutor().execute(
                    new AddResourceToTaskCommand(currentRow, controller.getSelectedResource()));
        } else {
            controller.getCommandExecutor().execute(
                    new RemoveResourceFromTaskCommand(currentRow, controller.getSelectedResource()));
        }
    }
}
