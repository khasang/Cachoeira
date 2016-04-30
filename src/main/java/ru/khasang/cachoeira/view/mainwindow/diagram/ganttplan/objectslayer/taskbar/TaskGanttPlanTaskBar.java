package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar;

import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;

import java.time.LocalDate;

public class TaskGanttPlanTaskBar extends TaskBar {
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<LocalDate> startDateChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<LocalDate> finishDateChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IResource> resourceListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener hoverListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IDependentTask> dependentTaskListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener zoomListener;

    public TaskGanttPlanTaskBar(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void initTaskRectangle(ITask task, IResource resource) {
        super.initTaskRectangle(task, resource);
    }

    @Override
    protected void setParameters(ITask task, IResource resource, Rectangle backgroundRectangle) {
        backgroundRectangle.setWidth(taskWidth(
                task.getStartDate(),
                task.getFinishDate(),
                controller.getZoomMultiplier()));
        backgroundRectangle.setLayoutY(6);
        this.setLayoutX(taskX(
                task.getStartDate(),
                controller.getProject().getStartDate(),
                controller.getZoomMultiplier()));
        this.setLayoutY(taskY(controller.getProject().getTaskList().indexOf(task)));
    }

    @Override
    protected double taskY(int rowIndex) {
        this.rowIndex = rowIndex;
        return (rowIndex * ROW_HEIGHT);
    }

    @Override
    protected void setListeners(ITask task, IResource resource, Rectangle backgroundRectangle, Rectangle donePercentRectangle) {
        taskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    // Анимация при удалении и добавления элемента на диаграмме
                    Timeline timeline = createTimelineAnimation(
                            this.layoutYProperty(),
                            taskY(controller.getProject().getTaskList().indexOf(task)),
                            (rowIndex + 1) * 150);
                    timeline.play();
                }
            }
        };
        startDateChangeListener = (observable, oldValue, newValue) -> {
            // ... если эти изменения произошли не спомощью мышки, то...
            if (!wasMovedByMouse) {
                // ... меняем положение метки на диаграмме...
                // Animation
                Timeline timeline = createTimelineAnimation(
                        this.layoutXProperty(),
                        taskX(
                                task.getStartDate(),
                                controller.getProject().getStartDate(),
                                controller.getZoomMultiplier()
                        ),
                        400
                );
                Timeline timeline1 = createTimelineAnimation(
                        backgroundRectangle.widthProperty(),
                        taskWidth(
                                task.getStartDate(),
                                task.getFinishDate(),
                                controller.getZoomMultiplier()
                        ),
                        400
                );
                timeline.play();
                timeline1.play();
            }
        };
        finishDateChangeListener = (observable, oldValue, newValue) -> {
            if (!wasMovedByMouse) {
                // Animation
                Timeline timeline = createTimelineAnimation(
                        backgroundRectangle.widthProperty(),
                        taskWidth(
                                task.getStartDate(),
                                task.getFinishDate(),
                                controller.getZoomMultiplier()
                        ),
                        400
                );
                timeline.play();
            }
        };
        resourceListChangeListener = change -> {
            while (change.next()) {
                // Если добавился
                if (change.wasAdded()) {
                    for (IResource res : change.getAddedSubList()) {
                        controller.getView().getResourceGanttPlan().getObjectsLayer().addTaskBar(task, res);
                    }
                }
                // Если удалился
                if (change.wasRemoved()) {
                    for (IResource res : change.getRemoved()) {
                        controller.getView().getResourceGanttPlan().getObjectsLayer().removeTaskBarByResource(task, res);
                    }
                }
            }
        };
        hoverListener = observable -> {
            if (this.isHover()) {
                backgroundRectangle.setFill(Color.valueOf("#03bdf4"));
                backgroundRectangle.setStroke(Color.valueOf("#03d1f4"));
                donePercentRectangle.setFill(Color.valueOf("#0395f4"));
            } else {
                backgroundRectangle.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
                backgroundRectangle.setStroke(Color.valueOf("#03bdf4"));
                donePercentRectangle.setFill(Color.valueOf("#0381f4"));
            }
        };
        dependentTaskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(dependentTask -> controller.getView().getTaskGanttPlan()
                            .getRelationsLayer()
                            .addRelation(dependentTask, controller.getView().getTaskTableView().getSelectionModel().getSelectedItem().getValue(), controller));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(dependentTask ->controller.getView().getTaskGanttPlan()
                            .getRelationsLayer().removeRelation(dependentTask.getTask(), this.task));
                }
            }
        };
        zoomListener = observable -> {
            this.setLayoutX(taskX(task.getStartDate(), controller.getProject().getStartDate(), controller.getZoomMultiplier()));
            backgroundRectangle.setWidth(taskWidth(task.getStartDate(), task.getFinishDate(), controller.getZoomMultiplier()));
        };
        /*
         Следим за изменениями в списке задач, если произошло добавление или удаление элемента в списке,
         то пересчитываем индексы у элементов на диаграмме
         */
        controller.getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));
        // Если начальная дата изменилась, то...
        task.startDateProperty().addListener(new WeakChangeListener<>(startDateChangeListener));
        // Если конечная дата изменилась, то...
        task.finishDateProperty().addListener(new WeakChangeListener<>(finishDateChangeListener));
        // Следим за списком ресурсов привязанных к данной задаче/
        task.getResourceList().addListener(new WeakListChangeListener<>(resourceListChangeListener));
        //подсветка при наведении // TODO: 15.01.2016 Сделать анимацию
        this.hoverProperty().addListener(hoverListener);

        controller.getView().getTaskTableView().getSelectionModel().getSelectedItem().getValue().getParentTasks().addListener(new WeakListChangeListener<>(dependentTaskListChangeListener));
        controller.zoomMultiplierProperty().addListener(new WeakInvalidationListener(zoomListener));
    }
}
