package ru.khasang.cachoeira.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Created by Raenar on 07.10.2015.
 */
public interface ITask {
    int getId();

    ReadOnlyIntegerProperty idProperty();

    void setId(int id);

    String getName();

    void setName(String name);

    StringProperty nameProperty();

    LocalDate getStartDate();

    void setStartDate(LocalDate start);

    ObjectProperty<LocalDate> startDateProperty();

    LocalDate getFinishDate();

    void setFinishDate(LocalDate finish);

    ObjectProperty<LocalDate> finishDateProperty();

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
