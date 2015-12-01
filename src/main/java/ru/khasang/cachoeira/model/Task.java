package ru.khasang.cachoeira.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Raenar on 07.10.2015.
 */
public class Task implements ITask {
    private final ReadOnlyIntegerWrapper id = new ReadOnlyIntegerWrapper(this, "id", taskSequence.incrementAndGet());
    private StringProperty name = new SimpleStringProperty(this, "name");
    private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(this, "startDate");
    private ObjectProperty<LocalDate> finishDate = new SimpleObjectProperty<>(this, "finishDate");
    private IntegerProperty donePercent = new SimpleIntegerProperty(this, "donePercent");
    private StringProperty duration = new SimpleStringProperty(this, "duration");
    private ObjectProperty<PriorityType> priorityType = new SimpleObjectProperty<>(this, "priorityType");
    private DoubleProperty cost = new SimpleDoubleProperty(this, "cost");
    private ObservableList<IDependentTask> dependentTasks = FXCollections.observableArrayList();
    private ObjectProperty<ITaskGroup> taskGroup = new SimpleObjectProperty<>(this, "taskGroup");
    private ObservableList<IResource> resources = FXCollections.observableArrayList(new Callback<IResource, Observable[]>() {
        @Override
        public Observable[] call(IResource param) {
            return new Observable[] {
                    param.nameProperty(),
                    param.resourceTypeProperty(),
                    param.emailProperty()
            };
        }
    });

    /** Запоминаем количество задач **/
    private static AtomicInteger taskSequence = new AtomicInteger(-1); // -1, потому что первым идет рутовый элемент в таблице задач (rootTask)

    /** Конструктор с дефолтовыми значениями **/
    public Task() {
        this.name.setValue("Задача " + id.getValue());
        this.startDate.setValue(LocalDate.now());
        this.finishDate.setValue(startDate.getValue().plusDays(1));
        this.priorityType.setValue(PriorityType.Normal);
    }

    @Override
    public final int getId() {
        return id.get();
    }

    @Override
    public final ReadOnlyIntegerProperty idProperty() {
        return id.getReadOnlyProperty();
    }

    @Override
    public final void setId(int id) {
        this.id.set(id);
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
    public final void setStartDate(LocalDate start) {
        this.startDate.set(start);
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
    public final void setFinishDate(LocalDate finish) {
        this.finishDate.set(finish);
    }

    @Override
    public final ObjectProperty<LocalDate> finishDateProperty() {
        return finishDate;
    }

    @Override
    public final int getDonePercent() {
        return donePercent.get();
    }

    @Override
    public final void setDonePercent(int donePercent) {
        this.donePercent.set(donePercent);
    }

    @Override
    public final IntegerProperty donePercentProperty() {
        return donePercent;
    }

    @Override
    public final String getDuration() {
//        long difference = finishDate.getValue().getTime() - startDate.getValue().getTime();
//        duration.set(String.valueOf(difference / (24 * 60 * 60 * 1000)) + " дн.");
        return duration.get();
    }

    @Override
    public final StringProperty durationProperty() {
        getDuration();
        return duration;
    }

    @Override
    public final double getCost() {
        return cost.get();
    }

    @Override
    public final void setCost(double cost) {
        this.cost.set(cost);
    }

    @Override
    public final DoubleProperty costProperty() {
        return cost;
    }

    @Override
    public final PriorityType getPriorityType() {
        return priorityType.get();
    }

    @Override
    public final void setPriorityType(PriorityType type) {
        this.priorityType.set(type);
    }

    @Override
    public final ObjectProperty<PriorityType> priorityTypeProperty() {
        return priorityType;
    }

    @Override
    public final void addDependentTask(IDependentTask dependentTask) {
        dependentTasks.add(dependentTask);
    }

    @Override
    public final void removeDependentTask(IDependentTask dependentTask) {
        dependentTasks.remove(dependentTask);
    }

    @Override
    public final ObservableList<IDependentTask> getDependentTasks() {
        return dependentTasks;
    }

    @Override
    public final void setDependentTask(ObservableList<IDependentTask> dependentTask) {
        this.dependentTasks = dependentTask;
    }

    @Override
    public final ITaskGroup getGroup() {
        return taskGroup.get();
    }

    @Override
    public final void setGroup(ITaskGroup group) {
        taskGroup.set(group);
    }

    @Override
    public final ObjectProperty<ITaskGroup> groupProperty() {
        return taskGroup;
    }

    @Override
    public final void addResource(IResource resource) {
        resources.add(resource);
    }

    @Override
    public final void removeResource(IResource resource) {
        resources.remove(resource);
    }

    @Override
    public final ObservableList<IResource> getResourceList() {
        return resources;
    }

    @Override
    public final void setResourceList(ObservableList<IResource> resources) {
        this.resources = resources;
    }
}
