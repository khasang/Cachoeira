package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.commands.task.SetTaskStartDateCommand;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class LeftResizingAnchor extends ResizingAnchor {
    private static final double DURATION = 200;

    public LeftResizingAnchor(TaskBar taskBar) {
        super(taskBar);
    }

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.widthProperty().bind(taskBar.getDomainRectangle().widthProperty().divide(100).multiply(5));
        // Привязываем этот прямоугольник к левой стороне таскбара
        rectangle.xProperty().bind(taskBar.getDomainRectangle().xProperty());
        rectangle.heightProperty().bind(taskBar.getDomainRectangle().heightProperty());
        rectangle.layoutYProperty().bind(taskBar.getDomainRectangle().layoutYProperty());
        // При наведении на левую сторону таскбара будет меняться курсор
        rectangle.hoverProperty().addListener(observable -> taskBar.hoverHandler(rectangle));

        enableResize(rectangle);
        return rectangle;
    }

    @Override
    protected void enableResize(Rectangle rectangle) {
        delta = new Delta();
        rectangle.setOnMousePressed(this::mousePressed);
        rectangle.setOnMouseDragged(this::mouseDraggedHandler);
        rectangle.setOnMouseReleased(this::mouseReleasedHandler);
    }

    private void mousePressed(MouseEvent event) {
        // Выделяем нужный элемент в таблице
        taskBar.setTaskSelected();
        if (event.isPrimaryButtonDown()) {
            // record a delta distance for the drag and drop operation.
            delta.x = taskBar.getLayoutX() - event.getSceneX();
            taskBar.getScene().setCursor(Cursor.H_RESIZE);
        }
    }

    private void mouseDraggedHandler(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            double newX = event.getSceneX() + delta.x;
            if (newX >= 0
                    && newX <= taskBar.getLayoutX() + taskBar.getDomainRectangle().getWidth()
                    - taskBar.getController().getZoomMultiplier()) {
                double oldX = taskBar.getLayoutX();
                taskBar.setLayoutX(newX);
                taskBar.getDomainRectangle().setWidth(
                        taskBar.getDomainRectangle().getWidth() - (taskBar.getLayoutX() - oldX));
            }
        }
    }

    private void mouseReleasedHandler(MouseEvent event) {
        taskBar.getScene().setCursor(Cursor.DEFAULT);
        double oldX = taskBar.getLayoutX();
        Timeline timeline = taskBar.createTimelineAnimation(
                taskBar.layoutXProperty(),
                taskBar.getPixelsValueFromFullDays(taskBar.getFullDaysFromValue(taskBar.getLayoutX())),
                DURATION
        );
        Timeline compensateAnimation = taskBar.createTimelineAnimation(
                taskBar.getDomainRectangle().widthProperty(),
                taskBar.getDomainRectangle().getWidth()
                        - (taskBar.getPixelsValueFromFullDays(taskBar.getFullDaysFromValue(
                        taskBar.getLayoutX())) - oldX),
                DURATION
        );
        timeline.play();
        compensateAnimation.play();

        taskBar.isMovedByMouse(true);
        taskBar.getController().getCommandExecutor().execute(new SetTaskStartDateCommand(
                taskBar.getTask(),
                taskBar.getController().getProject().getStartDate().plusDays(taskBar.getFullDaysFromValue(
                        taskBar.getLayoutX()))));
        taskBar.isMovedByMouse(false);
    }
}
