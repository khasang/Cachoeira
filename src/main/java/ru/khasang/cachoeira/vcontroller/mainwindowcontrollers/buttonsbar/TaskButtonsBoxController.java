package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.buttonsbar;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import ru.khasang.cachoeira.commands.project.AddTaskToProjectCommand;
import ru.khasang.cachoeira.commands.project.RemoveTaskFromProjectCommand;
import ru.khasang.cachoeira.model.Task;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;

public class TaskButtonsBoxController extends ButtonsBoxController {
    public TaskButtonsBoxController(AbstractButtonsBox buttonsBox, MainWindowController controller) {
        super(buttonsBox, controller);
    }

    @Override
    protected void addHandler(ActionEvent event) {
        controller.getCommandExecutor().execute(new AddTaskToProjectCommand(controller.getProject(), new Task()));
    }

    @Override
    protected void removeHandler(ActionEvent event) {
        controller.getCommandExecutor().execute(new RemoveTaskFromProjectCommand(
                controller.getProject(),
                controller.getSelectedTask()));
    }

    @Override
    protected ObservableValue<? extends Boolean> bindToSelectedItem() {
        return controller.selectedTaskProperty().isNull();
    }
}
