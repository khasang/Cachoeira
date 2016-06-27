package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class BackgroundBar implements TaskBarLayer {
    private static final int BAR_ARC = 10;
    private static final double OPACITY = 0.5;

    private final TaskBar taskBar;

    public BackgroundBar(TaskBar taskBar) {
        this.taskBar = taskBar;
    }

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();

        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);
        rectangle.setOpacity(OPACITY);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setFill(Color.TRANSPARENT);

        rectangle.layoutXProperty().bind(taskBar.getDomainRectangle().layoutXProperty().subtract(10));
        rectangle.widthProperty().bind(taskBar.getDomainRectangle().widthProperty().add(20));
        rectangle.layoutYProperty().bind(taskBar.getDomainRectangle().layoutYProperty().subtract(5));
        rectangle.heightProperty().bind(taskBar.getDomainRectangle().heightProperty().add(10));
        return rectangle;
    }
}
