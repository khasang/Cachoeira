package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RemoveResourceFromProjectCommand implements Command {
    private final IProject project;
    private final IResource resource;
    private int index;
    private ArrayList<ITask> oldTaskList;

    public RemoveResourceFromProjectCommand(IProject project, IResource resource) {
        this.project = project;
        this.resource = resource;
    }

    @Override
    public void execute() {
        index = project.getResourceList().indexOf(resource);
        oldTaskList = new ArrayList<>(project.getTaskList().stream()
                .filter(task -> task.getResourceList().contains(resource))
                .collect(Collectors.toList()));
        // Удаляем
        project.getTaskList().stream()
                .filter(task -> task.getResourceList().contains(resource))
                .forEach(task -> task.getResourceList().remove(resource));
        project.getResourceList().remove(index);
    }

    @Override
    public void undo() {
        project.getResourceList().add(index, resource);
        for (ITask task : oldTaskList) {
            task.getResourceList().add(resource);
        }
    }
}
