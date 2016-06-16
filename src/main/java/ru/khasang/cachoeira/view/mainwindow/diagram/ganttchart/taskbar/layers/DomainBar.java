package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DomainBar implements TaskBarLayer {
    private static final String BAR_COLOR = "#03A9F4";
    private static final String BORDER_BAR_COLOR = "#03bdf4";
    private static final double BAR_HEIGHT = 18;
    private static final double BAR_ARC = 5;

    @Override
    public Rectangle create() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(BAR_COLOR));
        rectangle.setStroke(Color.valueOf(BORDER_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);
        rectangle.setHeight(BAR_HEIGHT);
        return rectangle;
    }
}
