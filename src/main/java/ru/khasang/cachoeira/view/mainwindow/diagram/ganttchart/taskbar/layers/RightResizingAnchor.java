package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.commands.task.SetTaskFinishDateCommand;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class RightResizingAnchor extends ResizingAnchor {
    private static final double DURATION = 200;

    public RightResizingAnchor(TaskBar taskBar) {
        super(taskBar);
    }

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.widthProperty().bind(taskBar.getDomainRectangle().widthProperty().divide(100).multiply(5));
        // Привязываем этот прямоугольник к правой стороне таскбара
        rectangle.xProperty().bind(
                taskBar.getDomainRectangle().xProperty()
                        .add(taskBar.getDomainRectangle().widthProperty())
                        .subtract(rectangle.widthProperty())
        );
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
        // Ивент при нажатии на прямоугольник
        rectangle.setOnMousePressed(event -> {
            // Выделяем нужный элемент в таблице
            taskBar.setTaskSelected();
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                delta.x = taskBar.getDomainRectangle().getWidth() - event.getSceneX();
                taskBar.getScene().setCursor(Cursor.H_RESIZE);
            }
        });
        // Ивент при движении прямоугольника
        rectangle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newWidth = event.getSceneX() + delta.x;
                if (newWidth >= taskBar.getController().getZoomMultiplier()
                        && taskBar.getLayoutX() + newWidth <= taskBar.getParent().getBoundsInLocal().getWidth()) {
                    taskBar.getDomainRectangle().setWidth(newWidth);
                }
            }
        });
        rectangle.setOnMouseReleased(event -> {
            if (!event.isPrimaryButtonDown()) {
                taskBar.getScene().setCursor(Cursor.DEFAULT);
                Timeline timeline = taskBar.createTimelineAnimation(
                        taskBar.getDomainRectangle().widthProperty(),
                        taskBar.getPixelsValueFromFullDays(taskBar.getFullDaysFromValue(
                                taskBar.getDomainRectangle().getWidth())),
                        DURATION
                );
                timeline.play();
                taskBar.isMovedByMouse(true);
                taskBar.getController().getCommandExecutor().execute(new SetTaskFinishDateCommand(
                        taskBar.getTask(),
                        taskBar.getTask().getStartDate().plusDays(taskBar.getFullDaysFromValue(
                                taskBar.getDomainRectangle().getWidth()))));
                taskBar.isMovedByMouse(false);
            }
        });
    }
}
