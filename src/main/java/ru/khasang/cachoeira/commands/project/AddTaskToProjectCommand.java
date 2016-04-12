package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;

public class AddTaskToProjectCommand implements Command {
    private final IProject project;
    private final ITask task;

    public AddTaskToProjectCommand(IProject project, ITask task) {
        this.project = project;
        this.task = task;
    }

    @Override
    public void execute() {
        project.getTaskList().add(task);
    }

    @Override
    public void undo() {
        project.getTaskList().remove(task);
    }

    @Override
    public void redo() {

    }
}
