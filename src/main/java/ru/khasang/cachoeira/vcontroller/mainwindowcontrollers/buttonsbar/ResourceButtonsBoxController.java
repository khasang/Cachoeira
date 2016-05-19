package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.buttonsbar;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import ru.khasang.cachoeira.commands.project.AddResourceToProjectCommand;
import ru.khasang.cachoeira.commands.project.RemoveResourceFromProjectCommand;
import ru.khasang.cachoeira.model.Resource;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;

public class ResourceButtonsBoxController extends ButtonsBoxController {
    public ResourceButtonsBoxController(AbstractButtonsBox buttonsBox, MainWindowController controller) {
        super(buttonsBox, controller);
    }

    @Override
    protected void addHandler(ActionEvent event) {
        controller.getCommandExecutor().execute(new AddResourceToProjectCommand(controller.getProject(), new Resource()));
    }

    @Override
    protected void removeHandler(ActionEvent event) {
        controller.getCommandExecutor().execute(new RemoveResourceFromProjectCommand(
                controller.getProject(),
                controller.getSelectedResource()));
    }

    @Override
    protected ObservableValue<? extends Boolean> bindToSelectedItem() {
        return controller.selectedResourceProperty().isNull();
    }
}
