package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.commands.task.SetTaskStartAndFinishDateCommand;
import ru.khasang.cachoeira.commands.task.SetTaskStartDateCommand;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

public abstract class AbstractTaskBar extends Pane {
    protected static final String BAR_COLOR = "#03A9F4";
    protected static final String HOVER_BAR_COLOR = "#0381f4";
    protected static final String BORDER_BAR_COLOR = "#03bdf4";
    protected static final String DONE_PERCENT_BAR_COLOR = "#0381f4";
    protected static final double BAR_HEIGHT = 18;
    protected static final double ROW_HEIGHT = 31;
    protected static final double BAR_ARC = 5;

    private final ITask task;
    private final MainWindowController controller;

    protected Rectangle backgroundRectangle;
    protected Rectangle donePercentRectangle;

    private Delta dragDelta;
    private Delta leftDelta;
    private Delta rightDelta;

    public AbstractTaskBar(ITask task, MainWindowController controller) {
        this.task = task;
        this.controller = controller;
        this.setPadding(new Insets(0, 0, 5, 0));
    }

    public void createBar() {
        backgroundRectangle = createBackgroundRectangle();
        donePercentRectangle = createDonePercentRectangle(backgroundRectangle);
        enableBarDrag();
        enableBarResizing();

    }

    private Rectangle createBackgroundRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(BAR_COLOR));
        rectangle.setStroke(Color.valueOf(BORDER_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);
        rectangle.setHeight(BAR_HEIGHT);
        return rectangle;
    }

    private Rectangle createDonePercentRectangle(Rectangle backgroundRectangle) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(DONE_PERCENT_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);

        rectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty().add(1.25));
        rectangle.heightProperty().bind(backgroundRectangle.heightProperty().subtract(2.5));

        rectangle.widthProperty().bind(
                backgroundRectangle.widthProperty().divide(100).multiply(task.donePercentProperty()));

        donePercentRectangle.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        donePercentRectangle.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());
        return rectangle;
    }

    private void enableBarDrag() {
        dragDelta = new Delta();
        backgroundRectangle.setOnMousePressed(this::handleBackgroundRectangleOnMousePressed);
        backgroundRectangle.setOnMouseDragged(this::handleBackgroundRectangleOnMouseDragged);
        backgroundRectangle.setOnMouseReleased(this::handleBackgroundRectangleOnMouseReleased);
    }

    private void handleBackgroundRectangleOnMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            dragDelta.x = getLayoutX() - event.getSceneX();
            getScene().setCursor(Cursor.MOVE);
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            // TODO: 27.05.2016 context menu
        }
    }

    private void handleBackgroundRectangleOnMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newX = event.getSceneX() + dragDelta.x;
            if (newX > 0 && newX + backgroundRectangle.getWidth() <= getParent().getBoundsInParent().getWidth()) {
                if (Math.round(newX / controller.getZoomMultiplier()) != dragDelta.oldX) {
                    dragDelta.oldX = Math.round(newX / controller.getZoomMultiplier() * controller.getZoomMultiplier() - 1.5);
                    controller.getCommandExecutor().execute(new SetTaskStartAndFinishDateCommand(
                            task,
                            controller.getProject().getStartDate().plusDays(
                                    Math.round(newX / controller.getZoomMultiplier())
                            ),
                            Math.round(backgroundRectangle.getWidth() / controller.getZoomMultiplier())
                    ));
                }
            }
        }
    }

    private void handleBackgroundRectangleOnMouseReleased(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            getScene().setCursor(Cursor.DEFAULT);
        }
    }

    private void enableBarResizing() {
        leftDelta = new Delta();
        rightDelta = new Delta();
        Rectangle leftResizableRectangle = createLeftResizableRectangle(backgroundRectangle);
//        leftResizableRectangle.hoverProperty().addListener(this::handleHoverAction);
        leftResizableRectangle.setOnMousePressed(this::handleResizableRectangleMousePressed);
        leftResizableRectangle.setOnMouseDragged(this::handleResizableRectangleMouseDragged);
        leftResizableRectangle.setOnMouseReleased(this::handleResizableRectangleMouseReleased);
    }

    private void handleResizableRectangleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            leftDelta.x = getLayoutX() - event.getSceneX();
            getScene().setCursor(Cursor.H_RESIZE);
        }
    }

    private void handleResizableRectangleMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double newX = event.getSceneX() + leftDelta.x;
            if (newX >= 0 && newX <= getLayoutX() + backgroundRectangle.getWidth()) {
                if (Math.round(newX / controller.getZoomMultiplier()) != leftDelta.oldX) {
                    if (!(Math.round(newX / controller.getZoomMultiplier()) * controller.getZoomMultiplier() - 1.5 == getLayoutX() + backgroundRectangle.getWidth())) {
                        leftDelta.oldX = Math.round(newX / controller.getZoomMultiplier());
                        double oldX = getLayoutX();
                        this.setLayoutX(Math.round(newX / controller.getZoomMultiplier()) * controller.getZoomMultiplier() - 1.5);
                        backgroundRectangle.setWidth(backgroundRectangle.getWidth() - (this.getLayoutX() - oldX));
                        wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        controller.getCommandExecutor().execute(new SetTaskStartDateCommand(
                                task,
                                controller.getProject().getStartDate().plusDays((Math.round(newX / controller.getZoomMultiplier())))));
                        wasMovedByMouse = false; // Когда окончили движение фолз
                    }
                }
            }
        }
    }

    private void handleResizableRectangleMouseReleased(MouseEvent event) {
        if (!event.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
        }
    }

    public Rectangle createLeftResizableRectangle(Rectangle backgroundRectangle) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.widthProperty().bind(backgroundRectangle.widthProperty().divide(100).multiply(5));
        rectangle.xProperty().bind(backgroundRectangle.xProperty());
        rectangle.heightProperty().bind(backgroundRectangle.heightProperty());
        rectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty());
        return rectangle;
    }

    private class Delta {
        double x;
        double oldX;
    }
}
