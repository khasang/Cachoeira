package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;

public class RemoveChildTaskFromTaskCommand implements Command {
    private final ITask task;
    private final IDependentTask childTask;
    private int index;

    public RemoveChildTaskFromTaskCommand(ITask task, IDependentTask childTask) {
        this.task = task;
        this.childTask = childTask;
    }

    @Override
    public void execute() {
        index = task.getChildTasks().indexOf(childTask);
        task.getChildTasks().remove(index);
    }

    @Override
    public void undo() {
        task.getChildTasks().add(index, childTask);
    }

    @Override
    public void redo() {

    }
}
