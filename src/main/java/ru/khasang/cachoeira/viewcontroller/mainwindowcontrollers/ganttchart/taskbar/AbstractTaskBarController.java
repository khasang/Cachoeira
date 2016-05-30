package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.ganttchart.taskbar;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ru.khasang.cachoeira.commands.task.SetTaskFinishDateCommand;
import ru.khasang.cachoeira.commands.task.SetTaskStartAndFinishDateCommand;
import ru.khasang.cachoeira.commands.task.SetTaskStartDateCommand;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.AbstractTaskBar;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

public abstract class AbstractTaskBarController {
    protected final AbstractTaskBar taskBar;
    protected final MainWindowController controller;

    private Delta dragDelta;
    private Delta leftDelta;
    private Delta rightDelta;

    protected boolean wasMovedByMouse;

    public AbstractTaskBarController(AbstractTaskBar taskBar, MainWindowController controller) {
        this.taskBar = taskBar;
        this.controller = controller;
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
        // Выделяем нужный элемент в таблице
        int i = controller.getProject().getTaskList().indexOf(taskBar.getTask());
        controller.getTaskTableView().getSelectionModel().select(i);
        if (event.getButton() == MouseButton.PRIMARY) {
            dragDelta.x = taskBar.getLayoutX() - event.getSceneX();
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
                if (Math.round(newX / controller.getZoomMultiplier()) != dragDelta.oldX) {
                    dragDelta.oldX = Math.round(newX / controller.getZoomMultiplier() * controller.getZoomMultiplier() - 1.5);
                    wasMovedByMouse = true;
                    controller.getCommandExecutor().execute(new SetTaskStartAndFinishDateCommand(
                            taskBar.getTask(),
                            controller.getProject().getStartDate().plusDays(
                                    Math.round(newX / controller.getZoomMultiplier())
                            ),
                            Math.round(taskBar.getBackgroundRectangle().getWidth() / controller.getZoomMultiplier())
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
        taskBar.getRightResizableRectangle().setOnMousePressed(this::handleRightResizableRectangleMousePresses);
        taskBar.getRightResizableRectangle().setOnMouseDragged(this::handleRightResizableRectangleMouseDragged);
        taskBar.getRightResizableRectangle().setOnMouseReleased(this::handleRightResizableRectangleMouseReleased);
    }

    private void handleResizableRectangleMousePressed(MouseEvent event) {
        // Выделяем нужный элемент в таблице
        int i = controller.getProject().getTaskList().indexOf(taskBar.getTask());
        controller.getTaskTableView().getSelectionModel().select(i);
        if (event.getButton() == MouseButton.PRIMARY) {
            leftDelta.x = taskBar.getLayoutX() - event.getSceneX();
            taskBar.getScene().setCursor(Cursor.H_RESIZE);
        }
    }


    private void handleResizableRectangleMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newX = event.getSceneX() + leftDelta.x;
            if (newX >= 0 && newX <= taskBar.getLayoutX() + taskBar.getBackgroundRectangle().getWidth()) {
                if (Math.round(newX / controller.getZoomMultiplier()) != leftDelta.oldX) {
                    if (!(Math.round(newX / controller.getZoomMultiplier()) * controller.getZoomMultiplier() - 1.5 == taskBar.getLayoutX() + taskBar.getBackgroundRectangle().getWidth())) {
                        leftDelta.oldX = Math.round(newX / controller.getZoomMultiplier());
                        double oldX = taskBar.getLayoutX();
                        taskBar.setLayoutX(Math.round(newX / controller.getZoomMultiplier()) * controller.getZoomMultiplier() - 1.5);
                        taskBar.getBackgroundRectangle().setWidth(taskBar.getBackgroundRectangle().getWidth() - (taskBar.getLayoutX() - oldX));
                        wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        controller.getCommandExecutor().execute(new SetTaskStartDateCommand(
                                taskBar.getTask(),
                                controller.getProject().getStartDate().plusDays((Math.round(newX / controller.getZoomMultiplier())))));
                        wasMovedByMouse = false; // Когда окончили движение фолз
                    }
                }
            }
        }
    }

    private void handleResizableRectangleMouseReleased(MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            taskBar.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    private void handleRightResizableRectangleMousePresses(MouseEvent event) {
        // Выделяем нужный элемент в таблице
        int i = controller.getProject().getTaskList().indexOf(taskBar.getTask());
        controller.getTaskTableView().getSelectionModel().select(i);
        if (event.isPrimaryButtonDown()) {
            // record a delta distance for the drag and drop operation.
            rightDelta.x = taskBar.getBackgroundRectangle().getWidth() - event.getSceneX();
            taskBar.getScene().setCursor(Cursor.H_RESIZE);
        }
    }

    private void handleRightResizableRectangleMouseDragged(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            double newWidth = event.getSceneX() + rightDelta.x;
            if (newWidth >= controller.getZoomMultiplier() && taskBar.getLayoutX() + newWidth <= taskBar.getParent().getBoundsInLocal().getWidth()) {
                // Хреначим привязку к сетке
                if (Math.round(newWidth / controller.getZoomMultiplier()) != rightDelta.oldX) {
                    rightDelta.oldX = Math.round(newWidth / controller.getZoomMultiplier());
                    taskBar.getBackgroundRectangle().setWidth(Math.round(newWidth / controller.getZoomMultiplier()) * controller.getZoomMultiplier());
                    wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                    controller.getCommandExecutor().execute(new SetTaskFinishDateCommand(
                            taskBar.getTask(),
                            taskBar.getTask().getStartDate().plusDays(Math.round(taskBar.getBackgroundRectangle().getWidth() / controller.getZoomMultiplier()))));
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

    private class Delta {
        double x;
        double oldX;
    }
}
