package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class ProgressBar implements TaskBarLayer {
    private static final String PROGRESS_BAR_COLOR = "#0381f4";
    private static final double BAR_ARC = 5;
    private static final double Y_INDENT = 1.25;
    private static final double HEIGHT_SUBTRACT = Y_INDENT * 2;

    private TaskBar taskBar;

    public ProgressBar(TaskBar taskBar) {
        this.taskBar = taskBar;
    }

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(PROGRESS_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);

        rectangle.layoutYProperty().bind(taskBar.getDomainRectangle().layoutYProperty().add(Y_INDENT));
        rectangle.heightProperty().bind(taskBar.getDomainRectangle().heightProperty().subtract(HEIGHT_SUBTRACT));
        rectangle.widthProperty().bind(
                taskBar.getDomainRectangle().widthProperty()
                        .divide(100).multiply(taskBar.getTask().donePercentProperty()));

        rectangle.onMousePressedProperty().bind(taskBar.getDomainRectangle().onMousePressedProperty());
        rectangle.onMouseDraggedProperty().bind(taskBar.getDomainRectangle().onMouseDraggedProperty());
        return rectangle;
    }
}
