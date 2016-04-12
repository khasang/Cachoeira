package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

public class RenameTaskCommand implements Command {
    private ITask task;
    private String name;
    private String oldName;

    public RenameTaskCommand(ITask task, String name) {
        this.task = task;
        this.name = name;
    }

    @Override
    public void execute() {
        oldName = task.getName();
        task.setName(name);
    }

    @Override
    public void undo() {
        task.setName(oldName);
    }

    @Override
    public void redo() {

    }
}
