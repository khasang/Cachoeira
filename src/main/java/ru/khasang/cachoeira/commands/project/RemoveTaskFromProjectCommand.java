package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;

public class RemoveTaskFromProjectCommand implements Command {
    private final IProject project;
    private final ITask task;
    private int index;

    public RemoveTaskFromProjectCommand(IProject project, ITask task) {
        this.project = project;
        this.task = task;
    }

    @Override
    public void execute() {
        index = project.getTaskList().indexOf(task);
        project.getTaskList().remove(index);
    }

    @Override
    public void undo() {
        project.getTaskList().add(index, task);
    }
}
