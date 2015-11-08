package ru.khasang.cachoeira.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Raenar on 07.10.2015.
 */
public interface ITask {
    String getName();

    void setName(String name);

    Date getStartDate();

    void setStartDate(Date start);

    Date getFinishDate();

    void setFinishDate(Date finish);

    int getDonePercent();

    void setDonePercent(int donePercent);

    String getDuration();

    int getCost();

    void setCost(int cost);

    void addDependentTask(IDependentTask dependentTask);

    void removeDependentTask(IDependentTask dependentTask);

    List<IDependentTask> getDependentTasks();

    void setDependentTask(List<IDependentTask> dependentTask);

    ITaskGroup getGroup();

    void setGroup(ITaskGroup group);

    void addResource(IResource resource);

    void removeResource(IResource resource);

    List<IResource> getResourceList();

    void setResourceList(List<IResource> resources);

    PriorityList getPriorityType();

    void setPriotityType(PriorityList type);

}
