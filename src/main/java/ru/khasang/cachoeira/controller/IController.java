package ru.khasang.cachoeira.controller;

import javafx.collections.ObservableList;
import ru.khasang.cachoeira.model.*;

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
    //добавил:
    void handleAddTask(String nameOfTask, Date startDate, Date finishDate, Double cost, double donePercent, PriorityType priorityType, ObservableList<IResource> resources);

    void notifyAddTask(ITask task);

    void handleRemoveTask(ITask task);

    void notifyRemoveTask(ITask task);
    //добавил:
    void handleChangeTask(String taskNameField, Date taskStartDate, Date taskFinishDate, Double taskCost, double taskDonePercent, PriorityType taskPriorityType, ObservableList<IResource> resources);

    void notifyChangeTask(ITask task);

    //мои добавки
    ITask getSelectedTask();

    void setSelectedTask(ITask task);

    void handleAddResource(String resourceName, String email, ResourceType type, List<ITask> tasks);

    void notifyAddResource(IResource resource);

    void handleRemoveResource(IResource resource);

    void notifyRemoveResource(IResource resource);

    void handleChangeResource(String resourceName, String email, ResourceType type, List<ITask> tasks);

    void notifyChangeResource(IResource resource);

    IResource getSelectedResource();

    void setSelectedResource(IResource resource);

    void notifyAddProject(String projectName, Date startDate, Date finishDate, String description);

    void setProject(IProject project);
}
