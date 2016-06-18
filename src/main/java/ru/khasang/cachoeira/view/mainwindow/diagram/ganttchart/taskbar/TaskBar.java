package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ru.khasang.cachoeira.commands.task.SetTaskStartAndFinishDateCommand;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers.BarLayerFactory;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class TaskBar extends Pane {
    protected static final double ROW_HEIGHT = 31;
    private static final double X_OFFSET = 0.5;

    protected ITask task;
    protected MainWindowController controller;

    private IResource resource;
    protected boolean wasMovedByMouse;
    protected int rowIndex;                        //координата Y (строка задачи)

    protected Rectangle domainRectangle;
    protected Rectangle progressRectangle;
    protected Rectangle leftResizingAnchor;
    protected Rectangle rightResizingAnchor;
    protected Rectangle backgroundRectangle;

    public TaskBar(ITask task, BarLayerFactory layerFactory, MainWindowController controller) {
        this.task = task;
        domainRectangle = layerFactory.createDomainBar(this);
        progressRectangle = layerFactory.createProgressBar(this);
        leftResizingAnchor = layerFactory.createLeftResizingAnchor(this);
        rightResizingAnchor = layerFactory.createRightResizingAnchor(this);
        backgroundRectangle = layerFactory.createBackgroundBar(this);

        this.controller = controller;
        this.setPadding(new Insets(0, 0, 5, 0));
    }

    public void createBar() {
        this.setParameters(task, resource, domainRectangle);

        this.getChildren().add(backgroundRectangle);
        this.getChildren().add(domainRectangle);
        this.getChildren().add(progressRectangle);
        this.getChildren().addAll(leftResizingAnchor, rightResizingAnchor);

        this.setListeners(task, resource, domainRectangle, progressRectangle);
    }

    protected abstract void setParameters(ITask task,
                                          IResource resource,
                                          Rectangle backgroundRectangle);

    protected double taskWidth(LocalDate taskStartDate,
                               LocalDate taskFinishDate,
                               int columnWidth) {
        return (ChronoUnit.DAYS.between(taskStartDate, taskFinishDate) * columnWidth);
    }

    protected abstract double taskY(int rowIndex);

    protected double taskX(LocalDate taskStartDate,
                           LocalDate projectStartDate,
                           int columnWidth) {
        return ((ChronoUnit.DAYS.between(projectStartDate, taskStartDate)) * columnWidth) - X_OFFSET;
    }

    protected abstract void setListeners(ITask task,
                                         IResource resource,
                                         Rectangle backgroundRectangle,
                                         Rectangle donePercentRectangle);

    public Timeline createTimelineAnimation(DoubleProperty target, double endValue, double duration) {
        KeyValue endKeyValue = new KeyValue(target, endValue, Interpolator.SPLINE(0.4, 0, 0.2, 1));
        KeyFrame endKeyFrame = new KeyFrame(Duration.millis(duration), endKeyValue);
        return new Timeline(endKeyFrame);
    }

    public void hoverHandler(Rectangle rectangle) {
        if (rectangle.isHover()) {
            getScene().setCursor(Cursor.H_RESIZE);
            rectangle.setFill(Color.valueOf("#0381f4"));
        } else {
            getScene().setCursor(Cursor.DEFAULT);
            rectangle.setFill(Color.TRANSPARENT);
        }
    }

    public void setSelected(boolean enabled) {
        if (enabled) {
            backgroundRectangle.setStroke(Color.valueOf("#03A9F4"));
            backgroundRectangle.setFill(Color.valueOf("#e4f6ff"));
        } else {
            backgroundRectangle.setStroke(Color.TRANSPARENT);
            backgroundRectangle.setFill(Color.TRANSPARENT);
        }
    }

    /**
     * Convert some value (X coordinate or width) to full days.
     *
     * @param value x-coordinate.
     * @return full days.
     */
    public long getFullDaysFromValue(double value) {
        return Math.round(value / controller.getZoomMultiplier());
    }

    /**
     * Convert full days to value in pixels.
     *
     * @param days full days.
     * @return value in pixels.
     */
    public long getPixelsValueFromFullDays(long days) {
        return days * controller.getZoomMultiplier();
    }

    public ITask getTask() {
        return task;
    }

    public Rectangle getDomainRectangle() {
        return domainRectangle;
    }

    public Rectangle getProgressRectangle() {
        return progressRectangle;
    }

    public Rectangle getBackgroundRectangle() {
        return backgroundRectangle;
    }

    public void setTaskSelected() {
        int i = controller.getProject().getTaskList().indexOf(task);
        controller.getTaskTableView().getSelectionModel().select(i);
    }

    public void executeDraggingCommand() {
        controller.getCommandExecutor().execute(new SetTaskStartAndFinishDateCommand(
                task,
                controller.getProject().getStartDate().plusDays(
                        getFullDaysFromValue(getLayoutX())),
                getFullDaysFromValue(domainRectangle.getWidth())
        ));
    }

    public MainWindowController getController() {
        return controller;
    }
}