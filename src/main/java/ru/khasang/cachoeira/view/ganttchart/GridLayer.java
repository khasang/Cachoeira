package ru.khasang.cachoeira.view.ganttchart;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GridLayer extends Pane {
    // This is to make the stroke be drawn 'on pixel'.
    private static final double HALF_PIXEL_OFFSET = -0.5;

    private final Canvas canvas = new Canvas();
    private int columnWidth;

    public GridLayer(int columnWidth) {
        this.columnWidth = columnWidth;
        setStyle("-fx-background-color: white");
        getChildren().add(canvas);
    }

    @Override
    protected void layoutChildren() {
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int width = (int) getWidth() - left - right;
        final int height = (int) getHeight() - top - bottom;
        final int spacing = columnWidth;

        canvas.setLayoutX(left);
        canvas.setLayoutY(top);

        if (width != canvas.getWidth() || height != canvas.getHeight()) {
            canvas.setWidth(width);
            canvas.setHeight(height);

            GraphicsContext g = canvas.getGraphicsContext2D();
            g.clearRect(0, 0, width, height);
            g.setStroke(Color.rgb(237, 237, 237));

            final int vLineCount = (int) Math.floor((width + 1) / spacing);

            for (int i = 0; i < vLineCount; i++) {
                g.strokeLine(snap((i + 1) * spacing), 0, snap((i + 1) * spacing), height);
            }
        }
    }

    private static double snap(double y) {
        return ((int) y) + HALF_PIXEL_OFFSET;
    }
}