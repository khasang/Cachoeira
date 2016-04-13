package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

public class SetTaskDescriptionCommand implements Command {
    private final ITask task;
    private final String description;
    private String oldDescription;

    public SetTaskDescriptionCommand(ITask task, String description) {
        this.task = task;
        this.description = description;
    }

    @Override
    public void execute() {
        oldDescription = task.getDescription();
        task.setDescription(description);
    }

    @Override
    public void undo() {
        task.setDescription(oldDescription);
    }
}
