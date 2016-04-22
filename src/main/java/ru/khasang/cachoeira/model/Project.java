package ru.khasang.cachoeira.model;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Класс описывающий проект.
 */
public class Project implements IProject {

    private StringProperty name = new SimpleStringProperty(this, "name");
    private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(this, "startDate");
    private ObjectProperty<LocalDate> finishDate = new SimpleObjectProperty<>(this, "finishDate");
    private StringProperty description = new SimpleStringProperty(this, "description");
    private ObservableList<ITask> tasks = FXCollections.observableArrayList(task -> new Observable[]{
            task.nameProperty(),
            task.startDateProperty(),
            task.finishDateProperty(),
            task.donePercentProperty(),
            task.priorityTypeProperty(),
            task.costProperty(),
            task.getParentTasks(),
            task.getChildTasks(),
            task.groupProperty(),
            task.getResourceList(),
            task.descriptionProperty()
    });
    private ObservableList<IResource> resources = FXCollections.observableArrayList(resource -> new Observable[]{
            resource.nameProperty(),
            resource.resourceTypeProperty(),
            resource.emailProperty(),
            resource.descriptionProperty()
    });

    public Project() {
    }

    public Project(String name) {
        this.name.set(name);
    }

    @Override
    public final String getName() {
        return name.get();
    }

    @Override
    public final void setName(String name) {
        this.name.set(name);
    }

    @Override
    public final StringProperty nameProperty() {
        return name;
    }

    @Override
    public final LocalDate getStartDate() {
        return startDate.get();
    }

    @Override
    public final void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
        if (finishDate.getValue() != null) {
            if (startDate.isEqual(finishDate.getValue()) || startDate.isAfter(finishDate.getValue())) {
                finishDate.setValue(startDate.plusDays(1));
            }
        }
    }

    @Override
    public final ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    @Override
    public final LocalDate getFinishDate() {
        return finishDate.get();
    }

    @Override
    public final void setFinishDate(LocalDate finishDate) {
        this.finishDate.set(finishDate);
    }

    @Override
    public final ObjectProperty<LocalDate> finishDateProperty() {
        return finishDate;
    }

    @Override
    public final ObservableList<ITask> getTaskList() {
        return tasks;
    }

    @Override
    public final void setTaskList(ObservableList<ITask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public final ObservableList<IResource> getResourceList() {
        return resources;
    }

    @Override
    public final void setResourceList(ObservableList<IResource> resources) {
        this.resources = resources;
    }

    @Override
    public final String getDescription() {
        return description.get();
    }

    @Override
    public final void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public final StringProperty descriptionProperty() {
        return description;
    }
}
