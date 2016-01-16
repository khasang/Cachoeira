package ru.khasang.cachoeira.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс описывающий задачу.
 */
public class Task implements ITask {
    // Айди (изменить нельзя)
    private final ReadOnlyIntegerWrapper id = new ReadOnlyIntegerWrapper(this, "id", taskSequence.incrementAndGet());
    // Имя задачи
    private StringProperty name = new SimpleStringProperty(this, "name");
    // Начальная дата
    private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(this, "startDate");
    // Конечная дата
    private ObjectProperty<LocalDate> finishDate = new SimpleObjectProperty<>(this, "finishDate");
    // Продолжительность (начальная дата минус конечная)
    private IntegerProperty duration = new SimpleIntegerProperty(this, "duration");
    // Процент выполнения задачи
    private IntegerProperty donePercent = new SimpleIntegerProperty(this, "donePercent");
    // Приоритет задачи
    private ObjectProperty<PriorityType> priorityType = new SimpleObjectProperty<>(this, "priorityType");
    // Стоимость задачи
    private DoubleProperty cost = new SimpleDoubleProperty(this, "cost");
    // Описание задачи (комментарий)
    private StringProperty description = new SimpleStringProperty(this, "description");
    // Список задач после завершения которых начинает выполнение эта задача (наверное)
    private ObservableList<IDependentTask> dependentTasks = FXCollections.observableArrayList();
    // Группа задач в которой находится эта задача
    private ObjectProperty<ITaskGroup> taskGroup = new SimpleObjectProperty<>(this, "taskGroup");
    // Список ресурсов к которым привязана эта задача
    private ObservableList<IResource> resources = FXCollections.observableArrayList(new Callback<IResource, Observable[]>() {
        @Override
        public Observable[] call(IResource resource) {
            return new Observable[]{
                    resource.nameProperty(),
                    resource.resourceTypeProperty(),
                    resource.emailProperty(),
                    resource.descriptionProperty()
            };
        }
    });

    // Запоминаем количество задач
    private static AtomicInteger taskSequence = new AtomicInteger(-1); // -1, потому что первым идет рутовый элемент в таблице задач (rootTask)

    /**
     * Конструктор с дефолтовыми значениями.
     */
    public Task() {
        this.name.setValue("Задача " + id.getValue());
        this.startDate.setValue(LocalDate.now());
        this.finishDate.setValue(startDate.getValue().plusDays(1));
        this.duration.setValue(1);
        this.priorityType.setValue(PriorityType.Normal);

        // В случае изменения дат пересчитываем duration
        this.startDate.addListener((observable, oldValue, newValue) -> {
            long between = ChronoUnit.DAYS.between(newValue, this.finishDate.getValue());
            this.duration.setValue(between);
        });
        this.finishDate.addListener((observable, oldValue, newValue) -> {
            long between = ChronoUnit.DAYS.between(this.startDate.getValue(), newValue);
            this.duration.setValue(between);
        });
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
    public final void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
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

    @Override
    public int getDuration() {
        return duration.get();
    }

    @Override
    public IntegerProperty durationProperty() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration.set(duration);
    }
}
