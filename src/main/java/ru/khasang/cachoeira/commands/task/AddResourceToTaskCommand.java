package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

public class AddResourceToTaskCommand implements Command {
    private final ITask task;
    private final IResource resource;

    public AddResourceToTaskCommand(ITask task, IResource resource) {
        this.task = task;
        this.resource = resource;
    }

    @Override
    public void execute() {
        task.getResourceList().add(resource);
    }

    @Override
    public void undo() {
        task.getResourceList().remove(resource);
    }

    @Override
    public void redo() {

    }
}
