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
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

import java.time.LocalDate;

public class ResourceGanttPlanTaskBar extends TaskBar {
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IResource> resourceListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<LocalDate> startDateChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<LocalDate> finishDateChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener hoverListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener zoomListener;

    public ResourceGanttPlanTaskBar(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void initTaskRectangle(ITask task, IResource resource) {
        super.initTaskRectangle(task, resource);
        setLabel(task, backgroundRectangle);
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
        this.setLayoutY(taskY(controller.getProject().getResourceList().indexOf(resource)));
    }

    @Override
    protected double taskY(int rowIndex) {
        return (rowIndex * ROW_HEIGHT);
    }

    @Override
    protected void setListeners(ITask task, IResource resource, Rectangle backgroundRectangle, Rectangle donePercentRectangle) {
        resourceListChangeListener = change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    // Анимация при удалении и добавления элемента на диаграмме
                    Timeline timeline = createTimelineAnimation(
                            this.layoutYProperty(),
                            taskY(controller.getProject().getResourceList().indexOf(resource)),
                            (controller.getProject().getResourceList().indexOf(resource) + 1) * 150);
                    timeline.play();
                }
            }
        };
        startDateChangeListener = (observable1, oldValue1, newValue1) -> {
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
                        400);
                timeline.play();
            }
        };
        hoverListener = observable -> {
            if (this.isHover()) {
                backgroundRectangle.setFill(Color.valueOf("03bdf4"));
                backgroundRectangle.setStroke(Color.valueOf("#03d1f4"));
                donePercentRectangle.setFill(Color.valueOf("#0395f4"));
            } else {
                backgroundRectangle.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
                backgroundRectangle.setStroke(Color.valueOf("#03bdf4"));
                donePercentRectangle.setFill(Color.valueOf("#0381f4"));
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
        controller.getProject().getResourceList().addListener(new WeakListChangeListener<>(resourceListChangeListener));
        // Если начальная дата изменилась, то...
        task.startDateProperty().addListener(new WeakChangeListener<>(startDateChangeListener));
        // Если конечная дата изменилась, то...
        task.finishDateProperty().addListener(new WeakChangeListener<>(finishDateChangeListener));

        // подсветка при наведении // TODO: 15.01.2016 Сделать анимацию
        this.hoverProperty().addListener(new WeakInvalidationListener(hoverListener));

        controller.zoomMultiplierProperty().addListener(new WeakInvalidationListener(zoomListener));
    }
}
