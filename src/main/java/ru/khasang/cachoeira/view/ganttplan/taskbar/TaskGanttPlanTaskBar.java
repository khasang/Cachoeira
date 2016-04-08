package ru.khasang.cachoeira.view.ganttplan.taskbar;

import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

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

    @Override
    void setParameters(UIControl uiControl, ITask task, IResource resource, Rectangle backgroundRectangle) {
        backgroundRectangle.setWidth(taskWidth(
                task.getStartDate(),
                task.getFinishDate(),
                uiControl.getZoomMultiplier()));
        backgroundRectangle.setLayoutY(6);
        this.setLayoutX(taskX(
                task.getStartDate(),
                uiControl.getController().getProject().getStartDate(),
                uiControl.getZoomMultiplier()));
        this.setLayoutY(taskY(uiControl.getController().getProject().getTaskList().indexOf(task)));
    }

    @Override
    double taskY(int rowIndex) {
        this.rowIndex = rowIndex;
        return (rowIndex * ROW_HEIGHT);
    }

    @Override
    void setListeners(UIControl uiControl, ITask task, IResource resource, Rectangle backgroundRectangle, Rectangle donePercentRectangle) {
        taskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    // Анимация при удалении и добавления элемента на диаграмме
                    Timeline timeline = createTimelineAnimation(
                            this.layoutYProperty(),
                            taskY(uiControl.getController().getProject().getTaskList().indexOf(task)),
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
                                uiControl.getController().getProject().getStartDate(),
                                uiControl.getZoomMultiplier()
                        ),
                        400
                );
                Timeline timeline1 = createTimelineAnimation(
                        backgroundRectangle.widthProperty(),
                        taskWidth(
                                task.getStartDate(),
                                task.getFinishDate(),
                                uiControl.getZoomMultiplier()
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
                                uiControl.getZoomMultiplier()
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
                        uiControl.getMainWindow().getDiagramPaneController().getResourcePaneController().getGanttPlan().getObjectsLayer().addTaskBar(task, res);
                    }
                }
                // Если удалился
                if (change.wasRemoved()) {
                    for (IResource res : change.getRemoved()) {
                        uiControl.getMainWindow().getDiagramPaneController().getResourcePaneController().getGanttPlan().getObjectsLayer().removeTaskBarByResource(task, res);
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
//        dependentTaskListChangeListener = change -> {
//            while (change.next()) {
//                if (change.wasAdded()) {
//                    change.getAddedSubList().forEach(dependentTask -> uiControl.getMainWindow()
//                        .getDiagramPaneController().getTaskPaneController()
//                        .getGanttPlan().getTaskPaneRelationsLayer()
//                        .addRelation(dependentTask, uiControl.getController().getSelectedTask(), uiControl));
//                }
//                if (change.wasRemoved()) {
//                    change.getRemoved().forEach(dependentTask -> uiControl.getMainWindow()
//                            .getDiagramPaneController().getTaskPaneController()
//                            .getGanttPlan().getTaskPaneRelationsLayer()
//                            .removeRelation(dependentTask.getTask(), this.task));
//                }
//            }
//        };
        zoomListener = observable -> {
            this.setLayoutX(taskX(task.getStartDate(), uiControl.getController().getProject().getStartDate(), uiControl.getZoomMultiplier()));
            backgroundRectangle.setWidth(taskWidth(task.getStartDate(), task.getFinishDate(), uiControl.getZoomMultiplier()));
        };
    }
}
