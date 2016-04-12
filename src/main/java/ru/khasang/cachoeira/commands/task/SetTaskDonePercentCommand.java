package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

public class SetTaskDonePercentCommand implements Command {
    private final ITask task;
    private final int donePercent;
    private int oldDonePercent;

    public SetTaskDonePercentCommand(ITask task, int donePercent) {
        this.task = task;
        this.donePercent = donePercent;
    }

    @Override
    public void execute() {
        oldDonePercent = task.getDonePercent();
        task.setDonePercent(donePercent);
    }

    @Override
    public void undo() {
        task.setDonePercent(oldDonePercent);
    }

    @Override
    public void redo() {

    }
}
