package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class BackgroundBar implements TaskBarLayer {
    public static final int BAR_ARC = 10;

    private TaskBar taskBar;

    public BackgroundBar(TaskBar taskBar) {
        this.taskBar = taskBar;
    }

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);

        rectangle.layoutXProperty().bind(taskBar.getDomainRectangle().layoutXProperty().subtract(10));
        rectangle.widthProperty().bind(taskBar.getDomainRectangle().widthProperty().add(20));
        rectangle.layoutYProperty().bind(taskBar.getDomainRectangle().layoutYProperty());
        rectangle.heightProperty().bind(taskBar.getDomainRectangle().heightProperty());
        return rectangle;
    }
}
