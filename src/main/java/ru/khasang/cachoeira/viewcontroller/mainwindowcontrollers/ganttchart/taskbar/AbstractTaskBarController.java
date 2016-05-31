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

public abstract class AbstractTaskBarController {
    public static final double PIXELS_OFFSET = 1.5;

    protected final AbstractTaskBar taskBar;
    protected final MainWindowController controller;
    protected final ITask task;

    private Delta dragDelta;
    private Delta leftDelta;
    private Delta rightDelta;

    protected boolean wasMovedByMouse;

    public AbstractTaskBarController(AbstractTaskBar taskBar, MainWindowController controller, ITask task) {
        this.taskBar = taskBar;
        this.controller = controller;
        this.task = task;
    }

    public void build() {
        taskBar.createBar();
    }

    private void enableBarDrag() {
        dragDelta = new Delta();
        taskBar.getBackgroundRectangle().setOnMousePressed(this::handleBackgroundRectangleOnMousePressed);
        taskBar.getBackgroundRectangle().setOnMouseDragged(this::handleBackgroundRectangleOnMouseDragged);
        taskBar.getBackgroundRectangle().setOnMouseReleased(this::handleBackgroundRectangleOnMouseReleased);
    }

    private void handleBackgroundRectangleOnMousePressed(MouseEvent event) {
        setSelectedThisTaskInTableViewByIndex(getIndexOfTask(task));
        if (event.getButton() == MouseButton.PRIMARY) {
            dragDelta.x = getInitialDragCoordinate(event);
            taskBar.getScene().setCursor(Cursor.MOVE);
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            // TODO: 27.05.2016 context menu
        }
    }

    private void handleBackgroundRectangleOnMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newX = event.getSceneX() + dragDelta.x;
            if (newX > 0 && newX + taskBar.getBackgroundRectangle().getWidth() <= taskBar.getParent().getBoundsInParent().getWidth()) {
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

    private void handleBackgroundRectangleOnMouseReleased(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            taskBar.getScene().setCursor(Cursor.DEFAULT);
        }
    }

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

    private void handleResizableRectangleMousePressed(MouseEvent event) {
        setSelectedThisTaskInTableViewByIndex(getIndexOfTask(task));
        if (event.getButton() == MouseButton.PRIMARY) {
            leftDelta.x = getInitialDragCoordinate(event);
            taskBar.getScene().setCursor(Cursor.H_RESIZE);
        }
    }

    private void handleResizableRectangleMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newX = event.getSceneX() + leftDelta.x;
            if (newX >= 0 && newX <= getTaskBarEndCoordinate()) {
                if (getFullDaysFromValue(newX) != leftDelta.oldX) {
                    if (!isNewCoordinateEqualsTaskBarEndCoordinate(newX)) {
                        leftDelta.oldX = getFullDaysFromValue(newX);
                        double oldX = taskBar.getLayoutX();
                        taskBar.setLayoutX(getPixelsValueFromFullDays(getFullDaysFromValue(newX)) - PIXELS_OFFSET);
                        taskBar.getBackgroundRectangle().setWidth(taskBar.getBackgroundRectangle().getWidth() - (taskBar.getLayoutX() - oldX));
                        wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        controller.getCommandExecutor().execute(new SetTaskStartDateCommand(
                                task,
                                controller.getProject().getStartDate().plusDays((getFullDaysFromValue(newX)))));
                        wasMovedByMouse = false; // Когда окончили движение фолз
                    }
                }
            }
        }
    }

    private void handleRightResizableRectangleMousePressed(MouseEvent event) {
        setSelectedThisTaskInTableViewByIndex(getIndexOfTask(task));
        if (event.getButton() == MouseButton.PRIMARY) {
            // record a delta distance for the drag and drop operation.
            rightDelta.x = taskBar.getBackgroundRectangle().getWidth() - event.getSceneX();
            taskBar.getScene().setCursor(Cursor.H_RESIZE);
        }
    }

    private void handleRightResizableRectangleMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newWidth = event.getSceneX() + rightDelta.x;
            if (newWidth >= controller.getZoomMultiplier() && taskBar.getLayoutX() + newWidth <= taskBar.getParent().getBoundsInLocal().getWidth()) {
                // Хреначим привязку к сетке
                if (getFullDaysFromValue(newWidth) != rightDelta.oldX) {
                    rightDelta.oldX = getFullDaysFromValue(newWidth);
                    taskBar.getBackgroundRectangle().setWidth(getPixelsValueFromFullDays(getFullDaysFromValue(newWidth)));
                    wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                    controller.getCommandExecutor().execute(new SetTaskFinishDateCommand(
                            task,
                            task.getStartDate().plusDays(getFullDaysFromValue(taskBar.getBackgroundRectangle().getWidth()))));
                    wasMovedByMouse = false; // Когда окончили движение фолз
                }
            }
        }
    }

    private void handleRightResizableRectangleMouseReleased(MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            taskBar.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    private boolean isNewCoordinateEqualsTaskBarEndCoordinate(double newX) {
        return getPixelsValueFromFullDays(getFullDaysFromValue(newX)) - PIXELS_OFFSET == getTaskBarEndCoordinate();
    }

    private double getInitialDragCoordinate(MouseEvent event) {
        return taskBar.getLayoutX() - event.getSceneX();
    }

    private double getTaskBarEndCoordinate() {
        return taskBar.getLayoutX() + taskBar.getBackgroundRectangle().getWidth();
    }

    private void handleResizableRectangleMouseReleased(MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            taskBar.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    private void setSelectedThisTaskInTableViewByIndex(int indexOfTask) {
        // Выделяем нужный элемент в таблице
        controller.getTaskTableView().getSelectionModel().select(indexOfTask);
    }

    private int getIndexOfTask(ITask task) {
        return controller.getProject().getTaskList().indexOf(task);
    }

    private long getFullDaysFromValue(double value) {
        return Math.round(value / controller.getZoomMultiplier());
    }

    private long getPixelsValueFromFullDays(long days) {
        return days * controller.getZoomMultiplier();
    }

    private class Delta {
        double x;
        double oldX;
    }
}
