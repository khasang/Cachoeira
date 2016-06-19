package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class DomainBar implements TaskBarLayer {
    private static final String BAR_COLOR = "#03A9F4";
    private static final String BORDER_BAR_COLOR = "#03bdf4";
    private static final double BAR_HEIGHT = 18;
    private static final double BAR_ARC = 5;
    private static final double X_OFFSET = 0.5;
    private static final double DURATION = 200;

    private final TaskBar taskBar;
    private Delta dragDelta;

    public DomainBar(TaskBar taskBar) {
        this.taskBar = taskBar;
    }

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(BAR_COLOR));
        rectangle.setStroke(Color.valueOf(BORDER_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);
        rectangle.setHeight(BAR_HEIGHT);

        enableDrag(rectangle);
        return rectangle;
    }

    /**
     * Метод который включает возможность перемещения метки с помощью мышки по оси Х
     *
     * @param rectangle
     */
    private void enableDrag(Rectangle rectangle) {
        dragDelta = new Delta();
        rectangle.setOnMousePressed(this::mousePressedHandler);
        rectangle.setOnMouseDragged(event -> mouseDraggedHandler(event, rectangle));
        rectangle.setOnMouseReleased(this::mouseReleasedHandler);
    }

    private void mousePressedHandler(MouseEvent event) {
        // Выделяем нужный элемент в таблице
        taskBar.setTaskSelected();
        if (event.isPrimaryButtonDown()) {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = taskBar.getLayoutX() - event.getSceneX();
            taskBar.getScene().setCursor(Cursor.MOVE);
        }
    }

    private void mouseDraggedHandler(MouseEvent event, Rectangle rectangle) {
        if (event.isPrimaryButtonDown()) {
            double newX = event.getSceneX() + dragDelta.x;
            if (newX > 0
                    && newX + rectangle.getWidth() <= taskBar.getParent().getBoundsInParent().getWidth()) {
                taskBar.setLayoutX(newX - X_OFFSET);
            }
        }
    }

    private void mouseReleasedHandler(MouseEvent event) {
        taskBar.getScene().setCursor(Cursor.DEFAULT);
        Timeline timeline = taskBar.createTimelineAnimation(
                taskBar.layoutXProperty(),
                taskBar.getPixelsValueFromFullDays(
                        taskBar.getFullDaysFromValue(taskBar.getLayoutX())) - X_OFFSET, DURATION);
        timeline.play();
        taskBar.isMovedByMouse(true);
        taskBar.executeDraggingCommand();
        taskBar.isMovedByMouse(false);
    }

    private class Delta {
        double x;
    }
}
