package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

public class SetTaskDurationCommand implements Command {
    private final ITask task;
    private final int duration;
    private int oldDuration;

    public SetTaskDurationCommand(ITask task, int duration) {
        this.task = task;
        this.duration = duration;
    }

    @Override
    public void execute() {
        oldDuration = task.getDuration();
        task.setDuration(duration);
    }

    @Override
    public void undo() {
        task.setDuration(oldDuration);
    }

    @Override
    public void redo() {

    }
}
