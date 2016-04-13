package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;

public class AddResourceToProjectCommand implements Command {
    private final IProject project;
    private final IResource resource;

    public AddResourceToProjectCommand(IProject project, IResource resource) {
        this.project = project;
        this.resource = resource;
    }

    @Override
    public void execute() {
        project.getResourceList().add(resource);
    }

    @Override
    public void undo() {
        project.getResourceList().remove(resource);
    }
}
