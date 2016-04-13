package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;

public class RenameProjectCommand implements Command {
    private final IProject project;
    private final String name;
    private String oldName;

    public RenameProjectCommand(IProject project, String name) {
        this.project = project;
        this.name = name;
    }

    @Override
    public void execute() {
        oldName = project.getName();
        project.setName(name);
    }

    @Override
    public void undo() {
        project.setName(oldName);
    }
}
