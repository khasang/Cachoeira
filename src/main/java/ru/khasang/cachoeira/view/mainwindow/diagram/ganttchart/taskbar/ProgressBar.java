package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ProgressBar implements TaskBarLayer {
    private static final String PROGRESS_BAR_COLOR = "#0381f4";
    private static final double BAR_ARC = 5;
    private static final double Y_OFFSET = 1.25;
    private static final double HEIGHT_SUBTRACT = Y_OFFSET * 2;

    private Rectangle backgroundRectangle;

    public ProgressBar(Rectangle backgroundRectangle) {
        this.backgroundRectangle = backgroundRectangle;
    }

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(PROGRESS_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);

        rectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty().add(Y_OFFSET));
        rectangle.heightProperty().bind(backgroundRectangle.heightProperty().subtract(HEIGHT_SUBTRACT));

        rectangle.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        rectangle.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());
        return rectangle;
    }
}
