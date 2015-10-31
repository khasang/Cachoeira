package ru.khasang.cachoeira.controller;

import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;

import java.util.Date;
import java.util.List;

/**
 * Created by truesik on 26.10.2015.
 */
public interface IController {
    IProject getDefaultProject();

    IProject getProject();

    void handleChangeProject(IProject project);

    void notifyChangeProject(IProject project);

    void handleAddTask(String nameOfTask, Date startDate, Date finishDate, List<IResource> resources);

    void notifyAddTask(ITask task);

    void handleRemoveTask(ITask task);

    void notifyRemoveTask(ITask task);

    void handleChangeTask(String taskNameField, Date taskStartDate, Date taskFinishDate);

    void notifyChangeTask(ITask task);

    //мои добавки
    ITask getSelectedTask();

    void setSelectedTask(ITask task);

    void handleAddResource(String resourceName, String email, ResourceType type);

    void notifyAddResource(IResource resource);

    void handleRemoveResource(IResource resource);

    void notifyRemoveResource(IResource resource);

    void handleChangeResource(String resourceName, String email, ResourceType type);

    void notifyChangeResource(IResource resource);

    IResource getSelectedResource();

    void setSelectedResource(IResource resource);
}
