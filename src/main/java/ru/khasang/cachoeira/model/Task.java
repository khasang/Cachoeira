package ru.khasang.cachoeira.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс описывающий задачу.
 */
public class Task implements ITask {
    private final ReadOnlyIntegerWrapper id = new ReadOnlyIntegerWrapper(this, "id", taskSequence.incrementAndGet());
    private StringProperty name = new SimpleStringProperty(this, "name");
    private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(this, "startDate");
    private ObjectProperty<LocalDate> finishDate = new SimpleObjectProperty<>(this, "finishDate");
    private IntegerProperty duration = new SimpleIntegerProperty(this, "duration");
    private IntegerProperty donePercent = new SimpleIntegerProperty(this, "donePercent");
    private DoubleProperty cost = new SimpleDoubleProperty(this, "cost");
    private StringProperty description = new SimpleStringProperty(this, "description");
    private ObservableList<IDependentTask> parentTasks = FXCollections.observableArrayList(this::setObservableParentTaskFields);
    private ObservableList<IDependentTask> childTasks = FXCollections.observableArrayList(this::setObservableChildTaskFields);
    private ObjectProperty<ITaskGroup> taskGroup = new SimpleObjectProperty<>(this, "taskGroup");
    private ObservableList<IResource> resources = FXCollections.observableArrayList(this::setObservableResourceFields);

    private static AtomicInteger taskSequence = new AtomicInteger(-1); // -1, потому что первым идет рутовый элемент в таблице задач (rootTask)

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<LocalDate> startDateChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<LocalDate> finishDateChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IDependentTask> dependentTaskListChangeListener;

    /**
     * Конструктор с дефолтовыми значениями.
     */
    public Task() {
        this.name.setValue("Task" + " " + id.getValue());
        this.startDate.setValue(LocalDate.now());
        this.finishDate.setValue(startDate.getValue().plusDays(1));
        this.duration.setValue(1);

        // В случае изменения дат пересчитываем duration
        startDateChangeListener = (observable, oldValue, newValue) -> {
            long between = ChronoUnit.DAYS.between(newValue, this.finishDate.getValue());
            this.duration.setValue(between);
        };
        finishDateChangeListener = (observable, oldValue, newValue) -> {
            long between = ChronoUnit.DAYS.between(this.startDate.getValue(), newValue);
            this.duration.setValue(between);
        };
        this.startDate.addListener(new WeakChangeListener<>(startDateChangeListener));
        this.finishDate.addListener(new WeakChangeListener<>(finishDateChangeListener));

        dependentTaskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::dependencyConditions);
                }
                if (change.wasRemoved()) {
                    change.getList().forEach(this::dependencyConditions);
                }
                if (change.wasUpdated()) {
                    change.getList().subList(change.getFrom(), change.getTo()).forEach(this::dependencyConditions);
                }
            }
        };
        this.parentTasks.addListener(new WeakListChangeListener<>(dependentTaskListChangeListener));
    }

    private void dependencyConditions(IDependentTask dependentTask) {
        long between = ChronoUnit.DAYS.between(startDate.getValue(), finishDate.getValue());
        if (dependentTask.getDependenceType().equals(TaskDependencyType.FINISHSTART)) {
            // Финиш-Старт
            // Находим самую позднюю конечную дату из списка привязанных задач
            LocalDate latestFinishDate = parentTasks.stream()
                    .map(parentTask -> parentTask.getTask().getFinishDate())
                    .sorted(Comparator.reverseOrder())
                    .findFirst()
                    .orElseGet(null);
            this.startDate.setValue(latestFinishDate);
            this.finishDate.setValue(startDate.getValue().plusDays(between));
        }
        if (dependentTask.getDependenceType().equals(TaskDependencyType.FINISHFINISH)) {
            // Финиш-Финиш
            // TODO: 11.02.2016 протестировать
            LocalDate latestFinishDate = parentTasks.stream()
                    .map(parentTask -> parentTask.getTask().getFinishDate())
                    .sorted(Comparator.reverseOrder())
                    .findFirst()
                    .orElseGet(null);
            this.finishDate.setValue(latestFinishDate);
            this.startDate.setValue(finishDate.getValue().minusDays(between));
        }
        if (dependentTask.getDependenceType().equals(TaskDependencyType.STARTFINISH)) {
            // Старт-Финиш
            // TODO: 11.02.2016 протестировать
            LocalDate earliestStartDate = parentTasks.stream()
                    .map(parentTask -> parentTask.getTask().getStartDate())
                    .sorted()
                    .findFirst()
                    .orElseGet(null);
            this.finishDate.setValue(earliestStartDate);
            this.startDate.setValue(finishDate.getValue().minusDays(between));
        }
        if (dependentTask.getDependenceType().equals(TaskDependencyType.STARTSTART)) {
            // Старт-Старт
            // TODO: 11.02.2016 протестировать
            LocalDate earliestStartDate = parentTasks.stream()
                    .map(parentTask -> parentTask.getTask().getStartDate())
                    .sorted()
                    .findFirst()
                    .orElseGet(null);
            this.startDate.setValue(earliestStartDate);
            this.finishDate.setValue(startDate.getValue().plusDays(between));
        }
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
    public void addParentTask(IDependentTask parentTask) {
        this.parentTasks.add(parentTask);
    }

    @Override
    public void removeParentTask(IDependentTask parentTask) {
        this.parentTasks.remove(parentTask);
    }

    public final ObservableList<IDependentTask> getParentTasks() {
        return parentTasks;
    }

    @Override
    public void setParentTasks(ObservableList<IDependentTask> parentTasks) {
        this.parentTasks.addAll(parentTasks);
    }

    @Override
    public void addChildTask(IDependentTask childTask) {
        this.childTasks.add(childTask);
    }

    @Override
    public void removeChildTask(IDependentTask childTask) {
        this.childTasks.remove(childTask);
    }

    @Override
    public ObservableList<IDependentTask> getChildTasks() {
        return childTasks;
    }

    @Override
    public void setChildTasks(ObservableList<IDependentTask> childTasks) {
        this.childTasks.addAll(childTasks);
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

    private Observable[] setObservableParentTaskFields(IDependentTask dependentTask) {
        return new Observable[]{
                dependentTask.getTask().finishDateProperty(),
                dependentTask.dependenceTypeProperty()
        };
    }

    private Observable[] setObservableChildTaskFields(IDependentTask dependentTask) {
        return new Observable[]{
                dependentTask.taskProperty(),
                dependentTask.dependenceTypeProperty()
        };
    }

    private Observable[] setObservableResourceFields(IResource resource) {
        return new Observable[]{
                resource.nameProperty(),
                resource.resourceTypeProperty(),
                resource.emailProperty(),
                resource.descriptionProperty()
        };
    }
}
