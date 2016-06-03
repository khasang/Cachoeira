package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.ganttchart.taskbar;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ru.khasang.cachoeira.commands.task.SetTaskFinishDateCommand;
import ru.khasang.cachoeira.commands.task.SetTaskStartAndFinishDateCommand;
import ru.khasang.cachoeira.commands.task.SetTaskStartDateCommand;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.AbstractTaskBar;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

/**
 *
 */
public abstract class AbstractTaskBarController {
    public static final double PIXELS_OFFSET = 1.5;

    protected final AbstractTaskBar taskBar;
    protected final MainWindowController controller;
    protected final ITask task;

    private Delta dragDelta;
    private Delta leftDelta;
    private Delta rightDelta;

    protected boolean wasMovedByMouse;

    public AbstractTaskBarController(final AbstractTaskBar taskBar,
                                     final MainWindowController controller,
                                     final ITask task) {
        this.taskBar = taskBar;
        this.controller = controller;
        this.task = task;
    }

    /**
     *
     */
    public void build() {
        taskBar.createBar();
    }

    /**
     *
     */
    private void enableBarDrag() {
        dragDelta = new Delta();
        taskBar.getBackgroundRectangle().setOnMousePressed(this::handleBackgroundRectangleOnMousePressed);
        taskBar.getBackgroundRectangle().setOnMouseDragged(this::handleBackgroundRectangleOnMouseDragged);
        taskBar.getBackgroundRectangle().setOnMouseReleased(this::handleBackgroundRectangleOnMouseReleased);
    }

    /**
     * @param event
     */
    private void handleBackgroundRectangleOnMousePressed(final MouseEvent event) {
        setSelectedThisTaskInTableViewByIndex(getIndexOfTask(task));
        if (event.getButton() == MouseButton.PRIMARY) {
            dragDelta.x = getInitialDragCoordinate(event);
            taskBar.getScene().setCursor(Cursor.MOVE);
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            // TODO: 27.05.2016 context menu
        }
    }

    /**
     * @param event
     */
    private void handleBackgroundRectangleOnMouseDragged(final MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newX = event.getSceneX() + dragDelta.x;
            if (newX > 0 && newX + taskBar.getBackgroundRectangle().getWidth()
                    <= taskBar.getParent().getBoundsInParent().getWidth()) {
                if (getFullDaysFromValue(newX) != dragDelta.oldX) {
                    dragDelta.oldX = getPixelsValueFromFullDays(getFullDaysFromValue(newX)) - PIXELS_OFFSET;
                    wasMovedByMouse = true;
                    controller.getCommandExecutor().execute(new SetTaskStartAndFinishDateCommand(
                            task,
                            controller.getProject().getStartDate().plusDays(
                                    getFullDaysFromValue(newX)
                            ),
                            getFullDaysFromValue(taskBar.getBackgroundRectangle().getWidth())
                    ));
                    wasMovedByMouse = false;
                }
            }
        }
    }

    /**
     * @param event
     */
    private void handleBackgroundRectangleOnMouseReleased(final MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            taskBar.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     *
     */
    private void enableBarResizing() {
        leftDelta = new Delta();
        rightDelta = new Delta();

//        leftResizableRectangle.hoverProperty().addListener(this::handleHoverAction);
        taskBar.getLeftResizableRectangle().setOnMousePressed(this::handleResizableRectangleMousePressed);
        taskBar.getLeftResizableRectangle().setOnMouseDragged(this::handleResizableRectangleMouseDragged);
        taskBar.getLeftResizableRectangle().setOnMouseReleased(this::handleResizableRectangleMouseReleased);


//        rightResizableRectangle.hoverProperty().addListener(this::handleHoverAction);
        taskBar.getRightResizableRectangle().setOnMousePressed(this::handleRightResizableRectangleMousePressed);
        taskBar.getRightResizableRectangle().setOnMouseDragged(this::handleRightResizableRectangleMouseDragged);
        taskBar.getRightResizableRectangle().setOnMouseReleased(this::handleRightResizableRectangleMouseReleased);
    }

    /**
     * @param event
     */
    private void handleResizableRectangleMousePressed(final MouseEvent event) {
        setSelectedThisTaskInTableViewByIndex(getIndexOfTask(task));
        if (event.getButton() == MouseButton.PRIMARY) {
            leftDelta.x = getInitialDragCoordinate(event);
            taskBar.getScene().setCursor(Cursor.H_RESIZE);
        }
    }

    /**
     * @param event
     */
    private void handleResizableRectangleMouseDragged(final MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newX = event.getSceneX() + leftDelta.x;
            if (newX >= 0 && newX <= getTaskBarEndCoordinate()) {
                if (getFullDaysFromValue(newX) != leftDelta.oldX) {
                    if (!isNewCoordinateEqualsTaskBarEndCoordinate(newX)) {
                        leftDelta.oldX = getFullDaysFromValue(newX);
                        double oldX = taskBar.getLayoutX();
                        taskBar.setLayoutX(getPixelsValueFromFullDays(getFullDaysFromValue(newX)) - PIXELS_OFFSET);
                        taskBar.getBackgroundRectangle().setWidth(
                                taskBar.getBackgroundRectangle().getWidth() - (taskBar.getLayoutX() - oldX));
                        // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        wasMovedByMouse = true;
                        controller.getCommandExecutor().execute(new SetTaskStartDateCommand(
                                task,
                                controller.getProject().getStartDate().plusDays((getFullDaysFromValue(newX)))));
                        // Когда окончили движение фолз
                        wasMovedByMouse = false;
                    }
                }
            }
        }
    }

    /**
     * @param event
     */
    private void handleRightResizableRectangleMousePressed(final MouseEvent event) {
        setSelectedThisTaskInTableViewByIndex(getIndexOfTask(task));
        if (event.getButton() == MouseButton.PRIMARY) {
            // record a delta distance for the drag and drop operation.
            rightDelta.x = taskBar.getBackgroundRectangle().getWidth() - event.getSceneX();
            taskBar.getScene().setCursor(Cursor.H_RESIZE);
        }
    }

    /**
     * @param event
     */
    private void handleRightResizableRectangleMouseDragged(final MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newWidth = event.getSceneX() + rightDelta.x;
            if (newWidth >= controller.getZoomMultiplier() && taskBar.getLayoutX() + newWidth
                    <= taskBar.getParent().getBoundsInLocal().getWidth()) {
                // Хреначим привязку к сетке
                if (getFullDaysFromValue(newWidth) != rightDelta.oldX) {
                    rightDelta.oldX = getFullDaysFromValue(newWidth);
                    taskBar.getBackgroundRectangle().setWidth(
                            getPixelsValueFromFullDays(getFullDaysFromValue(newWidth)));
                    wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                    controller.getCommandExecutor().execute(new SetTaskFinishDateCommand(
                            task,
                            task.getStartDate().plusDays(getFullDaysFromValue(
                                    taskBar.getBackgroundRectangle().getWidth()))));
                    wasMovedByMouse = false; // Когда окончили движение фолз
                }
            }
        }
    }

    /**
     * @param event
     */
    private void handleRightResizableRectangleMouseReleased(final MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            taskBar.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * @param newX
     * @return
     */
    private boolean isNewCoordinateEqualsTaskBarEndCoordinate(final double newX) {
        return getPixelsValueFromFullDays(getFullDaysFromValue(newX)) - PIXELS_OFFSET == getTaskBarEndCoordinate();
    }

    /**
     * @param event
     * @return
     */
    private double getInitialDragCoordinate(final MouseEvent event) {
        return taskBar.getLayoutX() - event.getSceneX();
    }

    /**
     * @return
     */
    private double getTaskBarEndCoordinate() {
        return taskBar.getLayoutX() + taskBar.getBackgroundRectangle().getWidth();
    }

    /**
     * @param event
     */
    private void handleResizableRectangleMouseReleased(final MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            taskBar.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Set selected item in task table view by index.
     *
     * @param indexOfTask index of item you want select.
     */
    private void setSelectedThisTaskInTableViewByIndex(final int indexOfTask) {
        // Выделяем нужный элемент в таблице
        controller.getTaskTableView().getSelectionModel().select(indexOfTask);
    }

    /**
     * Returns index of task in project task list.
     *
     * @param task task.
     * @return index in int.
     */
    private int getIndexOfTask(final ITask task) {
        return controller.getProject().getTaskList().indexOf(task);
    }

    /**
     * Convert some value (X coordinate or width) to full days.
     *
     * @param value x-coordinate.
     * @return full days.
     */
    private long getFullDaysFromValue(final double value) {
        return Math.round(value / controller.getZoomMultiplier());
    }

    /**
     * Convert full days to value in pixels.
     *
     * @param days full days.
     * @return value in pixels.
     */
    private long getPixelsValueFromFullDays(final long days) {
        return days * controller.getZoomMultiplier();
    }

    /**
     *
     */
    private class Delta {
        double x;
        double oldX;
    }
}
