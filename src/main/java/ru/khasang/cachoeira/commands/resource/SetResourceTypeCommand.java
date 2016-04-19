package ru.khasang.cachoeira.commands.resource;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ResourceType;

public class SetResourceTypeCommand implements Command {
    private final IResource resource;
    private final ResourceType resourceType;
    private ResourceType oldResourceType;

    public SetResourceTypeCommand(IResource resource, ResourceType resourceType) {
        this.resource = resource;
        this.resourceType = resourceType;
    }

    @Override
    public void execute() {
        oldResourceType = resource.getType();
        resource.setType(resourceType);
    }

    @Override
    public void undo() {
        resource.setType(oldResourceType);
    }
}
