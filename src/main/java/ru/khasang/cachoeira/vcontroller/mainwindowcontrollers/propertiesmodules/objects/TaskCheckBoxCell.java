package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules.objects;

import javafx.scene.control.CheckBox;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

public class TaskCheckBoxCell extends AbstractCheckBoxCell<ITask, Boolean> {
    private IResource selectedResource;

    public TaskCheckBoxCell(IResource selectedResource) {
        this.selectedResource = selectedResource;
    }

    @Override
    protected CheckBox createCheckBox() {
        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> handleSelection(checkBox));
        return checkBox;
    }

    @Override
    protected void setSelectedCheckBox(CheckBox checkBox) {
        currentRow.getResourceList()
                .stream()
                .filter(resource -> selectedResource.equals(resource) && !checkBox.isSelected())
                .forEach(resource -> checkBox.setSelected(true));
    }

    private void handleSelection(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            CommandControl.getInstance().execute(
                    new AddResourceToTaskCommand(currentRow, selectedResource));
        } else {
            CommandControl.getInstance().execute(
                    new RemoveResourceFromTaskCommand(currentRow, selectedResource));
        }
    }
}
