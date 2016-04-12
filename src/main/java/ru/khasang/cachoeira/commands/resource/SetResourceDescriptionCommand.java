package ru.khasang.cachoeira.commands.resource;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IResource;

public class SetResourceDescriptionCommand implements Command {
    private final IResource resource;
    private final String description;
    private String oldDescription;

    public SetResourceDescriptionCommand(IResource resource, String description) {
        this.resource = resource;
        this.description = description;
    }

    @Override
    public void execute() {
        oldDescription = resource.getDescription();
        resource.setDescription(description);
    }

    @Override
    public void undo() {
        resource.setDescription(oldDescription);
    }

    @Override
    public void redo() {

    }
}
