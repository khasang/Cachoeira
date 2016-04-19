package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

public class RemoveResourceFromTaskCommand implements Command {
    private final ITask task;
    private final IResource resource;
    private int index;

    public RemoveResourceFromTaskCommand(ITask task, IResource resource) {
        this.task = task;
        this.resource = resource;
    }

    @Override
    public void execute() {
        index = task.getResourceList().indexOf(resource);
        task.getResourceList().remove(index);
    }

    @Override
    public void undo() {
        task.getResourceList().add(index, resource);
    }
}
