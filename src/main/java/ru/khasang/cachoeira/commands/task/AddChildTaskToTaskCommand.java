package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;

public class AddChildTaskToTaskCommand implements Command {
    private final ITask task;
    private final IDependentTask childTask;

    public AddChildTaskToTaskCommand(ITask task, IDependentTask childTask) {
        this.task = task;
        this.childTask = childTask;
    }

    @Override
    public void execute() {
        task.getChildTasks().add(childTask);
    }

    @Override
    public void undo() {
        task.getChildTasks().remove(childTask);
    }

    @Override
    public void redo() {

    }
}
