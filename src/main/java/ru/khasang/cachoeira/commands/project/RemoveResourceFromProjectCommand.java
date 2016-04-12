package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;

public class RemoveResourceFromProjectCommand implements Command {
    private final IProject project;
    private final IResource resource;
    private int index;

    public RemoveResourceFromProjectCommand(IProject project, IResource resource) {
        this.project = project;
        this.resource = resource;
    }

    @Override
    public void execute() {
        index = project.getResourceList().indexOf(resource);
        project.getResourceList().remove(index);
    }

    @Override
    public void undo() {
        project.getResourceList().add(index, resource);
    }

    @Override
    public void redo() {

    }
}
