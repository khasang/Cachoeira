package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;

public class RemoveParentTaskFromTaskCommand implements Command {
    private final ITask task;
    private final IDependentTask parentTask;
    private int index;

    public RemoveParentTaskFromTaskCommand(ITask task, IDependentTask parentTask) {
        this.task = task;
        this.parentTask = parentTask;
    }

    @Override
    public void execute() {
        index = task.getParentTasks().indexOf(parentTask);
        task.getParentTasks().remove(index);
    }

    @Override
    public void undo() {
        task.getParentTasks().add(index, parentTask);
    }
}
