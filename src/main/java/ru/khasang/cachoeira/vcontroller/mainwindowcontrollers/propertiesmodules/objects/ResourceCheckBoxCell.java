package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules.objects;

import javafx.scene.control.CheckBox;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

public class ResourceCheckBoxCell extends AbstractCheckBoxCell<IResource, Boolean> {
    protected ITask selectedTask;

    public ResourceCheckBoxCell(ITask selectedTask) {
        this.selectedTask = selectedTask;
    }

    @Override
    protected CheckBox createCheckBox() {
        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> handleSelection(checkBox));
        return checkBox;
    }

    @Override
    protected void setSelectedCheckBox(CheckBox checkBox) {
        selectedTask.getResourceList()
                .stream()
                .filter(resource -> resource.equals(currentRow) && !checkBox.isSelected())
                .forEach(resource -> checkBox.setSelected(true));
    }

    private void handleSelection(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            CommandControl.getInstance().execute(
                    new AddResourceToTaskCommand(selectedTask, currentRow));
        } else {
            CommandControl.getInstance().execute(
                    new RemoveResourceFromTaskCommand(selectedTask, currentRow));
        }
    }
}
