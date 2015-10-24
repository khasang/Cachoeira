package ru.khasang.cachoeira.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Raenar on 07.10.2015.
 */
public class Task implements ITask {
    private String name;
    private Date startDate;
    private Date finishDate;
    private int donePercent;
    private List<IDependentTask> dependentTasks = new ArrayList<>();
    private ITaskGroup taskGroup;
    private List<IResource> resources = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date start) {
        this.startDate = start;
    }

    @Override
    public Date getFinishDate() {
        return finishDate;
    }

    @Override
    public void setFinishDate(Date finish) {
        this.finishDate = finish;
    }

    @Override
    public int getDonePercent() {
        return donePercent;
    }

    @Override
    public void setDonePercent(int donePercent) {
        this.donePercent = donePercent;
    }

    @Override
    public void addDependentTask(IDependentTask dependentTask) {
        dependentTasks.add(dependentTask);
    }

    @Override
    public void removeDependentTask(IDependentTask dependentTask) {
        dependentTasks.remove(dependentTask);
    }

    @Override
    public List<IDependentTask> getDependentTasks() {
        return dependentTasks;
    }

    @Override
    public void setDependentTask(List<IDependentTask> dependentTask) {

    }

    @Override
    public ITaskGroup getGroup() {
        return taskGroup;
    }

    @Override
    public void setGroup(ITaskGroup group) {
        taskGroup = group;
    }

    @Override
    public void addResource(IResource resource) {

    }

    @Override
    public void removeResource(IResource resource) {

    }

    @Override
    public List<IResource> getResourceList() {
        return null;
    }

    @Override
    public void setResourceList(List<IResource> resources) {
        this.resources.addAll(resources);
    }
}
