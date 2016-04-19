package ru.khasang.cachoeira.commands.resource;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IResource;

public class RenameResourceCommand implements Command {
    private final IResource resource;
    private final String name;
    private String oldName;

    public RenameResourceCommand(IResource resource, String name) {
        this.resource = resource;
        this.name = name;
    }

    @Override
    public void execute() {
        oldName = resource.getName();
        resource.setName(name);
    }

    @Override
    public void undo() {
        resource.setName(oldName);
    }
}
