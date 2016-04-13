package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;

public class SetProjectDescriptionCommand implements Command {
    private final IProject project;
    private final String description;
    private String oldDescription;

    public SetProjectDescriptionCommand(IProject project, String description) {
        this.project = project;
        this.description = description;
    }

    @Override
    public void execute() {
        oldDescription = project.getDescription();
        project.setDescription(description);
    }

    @Override
    public void undo() {
        project.setDescription(oldDescription);
    }
}
