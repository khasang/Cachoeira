package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules.objects;

import javafx.scene.control.CheckBox;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.vcontroller.MainWindowController;

public class ResourceCheckBoxCell extends AbstractCheckBoxCell<IResource, Boolean> {
    private MainWindowController controller;

    public ResourceCheckBoxCell(MainWindowController controller) {
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
            controller.getSelectedTask().getResourceList()
                    .stream()
                    .filter(resource -> resource.equals(currentRow) && !checkBox.isSelected())
                    .forEach(resource -> checkBox.setSelected(true));
        }
    }

    private void handleSelection(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            controller.getCommandExecutor().execute(
                    new AddResourceToTaskCommand(controller.getSelectedTask(), currentRow));
        } else {
            controller.getCommandExecutor().execute(
                    new RemoveResourceFromTaskCommand(controller.getSelectedTask(), currentRow));
        }
    }
}
