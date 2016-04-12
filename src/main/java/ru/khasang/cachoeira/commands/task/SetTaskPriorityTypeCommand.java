package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;

public class SetTaskPriorityTypeCommand implements Command {
    private final ITask task;
    private final PriorityType priorityType;
    private PriorityType oldPriorityType;

    public SetTaskPriorityTypeCommand(ITask task, PriorityType priorityType) {
        this.task = task;
        this.priorityType = priorityType;
    }

    @Override
    public void execute() {
        oldPriorityType = task.getPriorityType();
        task.setPriorityType(priorityType);
    }

    @Override
    public void undo() {
        task.setPriorityType(oldPriorityType);
    }

    @Override
    public void redo() {

    }
}
