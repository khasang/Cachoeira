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
    private ObservableList<ITask> tasks = FXCollections.observableArrayList(this::setObservableTaskFields);
    private ObservableList<IResource> resources = FXCollections.observableArrayList(this::setObservableResourceFields);

    public Project() {
    }

    public Project(String name) {
        this.name.set(name);
    }

    public Project(String name, LocalDate startDate, LocalDate finishDate, String description) {
        this.name.setValue(name);
        this.startDate.setValue(startDate);
        this.finishDate.setValue(finishDate);
        this.description.setValue(description);
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
    public final void setStartDateAndVerify(LocalDate startDate) {
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
    public final void setFinishDateAndVerify(LocalDate finishDate) {
        this.finishDate.set(finishDate);
        if (startDate.getValue() != null) {
            if (finishDate.isEqual(startDate.getValue()) || finishDate.isBefore(startDate.getValue())) {
                startDate.setValue(finishDate.minusDays(1));
            }
        }
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

    private Observable[] setObservableTaskFields(ITask task) {
        return new Observable[] {
                task.nameProperty(),
                task.startDateProperty(),
                task.finishDateProperty(),
                task.donePercentProperty(),
                task.costProperty(),
                task.getParentTasks(),
                task.getChildTasks(),
                task.groupProperty(),
                task.getResourceList(),
                task.descriptionProperty()
        };
    }

    private Observable[] setObservableResourceFields(IResource resource) {
        return new Observable[] {
                resource.nameProperty(),
                resource.resourceTypeProperty(),
                resource.emailProperty(),
                resource.descriptionProperty()
        };
    }
}
