package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AbstractProgressBar {
    public Rectangle createProgressRectangle(Rectangle backgroundRectangle) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(DONE_PERCENT_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);

        rectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty().add(1.25));
        rectangle.heightProperty().bind(backgroundRectangle.heightProperty().subtract(2.5));

        donePercentRectangle.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        donePercentRectangle.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());
        return rectangle;
    }
}
