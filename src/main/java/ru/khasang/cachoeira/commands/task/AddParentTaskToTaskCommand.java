package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;

public class AddParentTaskToTaskCommand implements Command {
    private final ITask task;
    private final IDependentTask parentTask;

    public AddParentTaskToTaskCommand(ITask task, IDependentTask parentTask) {
        this.task = task;
        this.parentTask = parentTask;
    }

    @Override
    public void execute() {
        task.getParentTasks().add(parentTask);
    }

    @Override
    public void undo() {
        task.getParentTasks().remove(parentTask);
    }
}
