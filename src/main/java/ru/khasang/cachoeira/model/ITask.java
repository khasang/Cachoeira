package ru.khasang.cachoeira.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.Date;

/**
 * Created by Raenar on 07.10.2015.
 */
public interface ITask {
    String getName();

    void setName(String name);

    StringProperty nameProperty();

    Date getStartDate();

    void setStartDate(Date start);

    ObjectProperty<Date> startDateProperty();

    Date getFinishDate();

    void setFinishDate(Date finish);

    ObjectProperty<Date> finishDateProperty();

    int getDonePercent();

    void setDonePercent(int donePercent);

    IntegerProperty donePercentProperty();

    String getDuration();

    StringProperty durationProperty();

    double getCost();

    void setCost(double cost);

    DoubleProperty costProperty();

    void addDependentTask(IDependentTask dependentTask);

    void removeDependentTask(IDependentTask dependentTask);

    ObservableList<IDependentTask> getDependentTasks();

    void setDependentTask(ObservableList<IDependentTask> dependentTask);

    ITaskGroup getGroup();

    void setGroup(ITaskGroup group);

    ObjectProperty<ITaskGroup> groupProperty();

    void addResource(IResource resource);

    void removeResource(IResource resource);

    ObservableList<IResource> getResourceList();

    void setResourceList(ObservableList<IResource> resources);

    PriorityType getPriorityType();

    void setPriorityType(PriorityType type);

    ObjectProperty<PriorityType> priorityTypeProperty();
}
