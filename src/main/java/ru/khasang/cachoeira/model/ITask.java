package ru.khasang.cachoeira.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public interface ITask {
    int getId();

    ReadOnlyIntegerProperty idProperty();

    void setId(int id);

    String getName();

    void setName(String name);

    StringProperty nameProperty();

    LocalDate getStartDate();

    void setStartDateAndVerify(LocalDate startDate);

    ObjectProperty<LocalDate> startDateProperty();

    LocalDate getFinishDate();

    void setFinishDateAndVerify(LocalDate finishDate);

    ObjectProperty<LocalDate> finishDateProperty();

    int getDuration();

    IntegerProperty durationProperty();

    void setDuration(int duration);

    int getDonePercent();

    void setDonePercent(int donePercent);

    IntegerProperty donePercentProperty();

    double getCost();

    void setCost(double cost);

    DoubleProperty costProperty();

    String getDescription();

    void setDescription(String description);

    StringProperty descriptionProperty();

    void addParentTask(IDependentTask parentTask);

    void removeParentTask(IDependentTask parentTask);

    ObservableList<IDependentTask> getParentTasks();

    void setParentTasks(ObservableList<IDependentTask> parentTasks);

    void addChildTask(IDependentTask childTask);

    void removeChildTask(IDependentTask childTask);

    ObservableList<IDependentTask> getChildTasks();

    void setChildTasks(ObservableList<IDependentTask> childTasks);

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
