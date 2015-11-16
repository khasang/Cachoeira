package ru.khasang.cachoeira.model;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.List;

/**
 * Created by Raenar on 07.10.2015.
 */
public class Task implements ITask {
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<Date> startDate = new SimpleObjectProperty<>();
    private ObjectProperty<Date> finishDate = new SimpleObjectProperty<>();
    private IntegerProperty donePercent = new SimpleIntegerProperty();
    private StringProperty duration = new SimpleStringProperty();
    private ObjectProperty<PriorityType> priorityType = new SimpleObjectProperty<>();
    private DoubleProperty cost = new SimpleDoubleProperty();
    private ObservableList<IDependentTask> dependentTasks = FXCollections.observableArrayList();
    private ListProperty<IDependentTask> dependentTaskListProperty = new SimpleListProperty<>(dependentTasks);
    private ObjectProperty<ITaskGroup> taskGroup = new SimpleObjectProperty<>();
    private ObservableList<IResource> resources = FXCollections.observableArrayList();
    private ListProperty<IResource> resourceListProperty = new SimpleListProperty<>(resources);

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public Date getStartDate() {
        return startDate.get();
    }

    @Override
    public void setStartDate(Date start) {
        this.startDate.set(start);
    }

    @Override
    public ObjectProperty<Date> startDateProperty() {
        return startDate;
    }

    @Override
    public Date getFinishDate() {
        return finishDate.get();
    }

    @Override
    public void setFinishDate(Date finish) {
        this.finishDate.set(finish);
    }

    @Override
    public ObjectProperty<Date> finishDateProperty() {
        return finishDate;
    }

    @Override
    public int getDonePercent() {
        return donePercent.get();
    }

    @Override
    public void setDonePercent(int donePercent) {
        this.donePercent.set(donePercent);
    }

    @Override
    public IntegerProperty donePercentProperty() {
        return donePercent;
    }

    @Override
    public String getDuration() {
        long difference = finishDate.getValue().getTime() - startDate.getValue().getTime();
        duration.set(String.valueOf(difference / (24 * 60 * 60 * 1000)) + " дн.");
        return duration.get();
    }

    @Override
    public StringProperty durationProperty() {
        getDuration();
        return duration;
    }


    @Override
    public double getCost() {
        return cost.get();
    }

    @Override
    public void setCost(double cost) {
        this.cost.set(cost);
    }

    @Override
    public DoubleProperty costProperty() {
        return cost;
    }

    @Override
    public PriorityType getPriorityType() {
        return priorityType.get();
    }

    @Override
    public void setPriorityType(PriorityType type) {
        this.priorityType.set(type);
    }

    @Override
    public ObjectProperty<PriorityType> priorityTypeProperty() {
        return priorityType;
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
    public ObservableList<IDependentTask> getDependentTasks() {
        return dependentTasks;
    }

    @Override
    public void setDependentTask(ObservableList<IDependentTask> dependentTask) {
        this.dependentTasks = dependentTask;
    }

    @Override
    public ListProperty<IDependentTask> dependentTaskListProperty() {
        return dependentTaskListProperty;
    }

    @Override
    public ITaskGroup getGroup() {
        return taskGroup.get();
    }

    @Override
    public void setGroup(ITaskGroup group) {
        taskGroup.set(group);
    }

    @Override
    public ObjectProperty<ITaskGroup> groupProperty() {
        return taskGroup;
    }

    @Override
    public void addResource(IResource resource) {
        resources.add(resource);
    }

    @Override
    public void removeResource(IResource resource) {
        resources.remove(resource);
    }

    @Override
    public ObservableList<IResource> getResourceList() {
        return resources;
    }

    @Override
    public void setResourceList(ObservableList<IResource> resources) {
        this.resources = resources;
    }

    @Override
    public ListProperty<IResource> resourceListProperty() {
        return resourceListProperty;
    }
}
