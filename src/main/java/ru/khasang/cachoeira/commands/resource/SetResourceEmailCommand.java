package ru.khasang.cachoeira.commands.resource;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IResource;

public class SetResourceEmailCommand implements Command {
    private final IResource resource;
    private final String email;
    private String oldEmail;

    public SetResourceEmailCommand(IResource resource, String email) {
        this.resource = resource;
        this.email = email;
    }

    @Override
    public void execute() {
        oldEmail = resource.getEmail();
        resource.setEmail(email);
    }

    @Override
    public void undo() {
        resource.setEmail(oldEmail);
    }
}
