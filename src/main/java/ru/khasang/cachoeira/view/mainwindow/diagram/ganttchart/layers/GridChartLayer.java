package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

public class GridChartLayer extends Pane implements AbstractChartLayer {
    private static final String LINE_COLOR = "#EDEDED";
    // This is to make the stroke be drawn 'on pixel'.
    private static final double HALF_PIXEL_OFFSET = -0.5;
    private MainWindowController controller;

    public GridChartLayer(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void createLayer() {
        this.setStyle("-fx-background-color: white");
    }

    @Override
    protected void layoutChildren() {
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int width = (int) getWidth() - left - right;
        final int height = (int) getHeight() - top - bottom;
        final int spacing = controller.getZoomMultiplier(); // Ширина дня в пикселях

        if (spacing <= 130 && spacing >= 55) {
            drawLines(width, height, spacing, 1);
        }
        if (spacing <= 54 && spacing >= 40) {
            drawLines(width, height, spacing, 2);
        }
        if (spacing <= 39 && spacing >= 20) {
            drawLines(width, height, spacing, 4);
        }
        if (spacing <= 19 && spacing >= 8) {
            drawLines(width, height, spacing, 7);
        }
        if (spacing <= 7 && spacing >= 4) {
            drawLines(width, height, spacing, 14);
        }
        if (spacing <= 3 && spacing >= 2) {
            drawLines(width, height, spacing, 28);
        }
    }

    /**
     * Method draws vertical lines across GridLayer width.
     *
     * @param width      Panel wight.
     * @param height     Panel height.
     * @param spacing    Zoom value.
     * @param multiplier Multiplier which increases distance between lines.
     */
    private void drawLines(int width,
                           int height,
                           int spacing,
                           int multiplier) {
        this.getChildren().clear();
        final int vLineCount = (int) Math.floor((width + 1) / (spacing * multiplier));

        for (int i = 0; i < vLineCount; i++) {
            Line line = new Line(snap((i + 1) * (spacing * multiplier)), 0, snap((i + 1) * (spacing * multiplier)), height);
            line.setStroke(Color.valueOf(LINE_COLOR));
            this.getChildren().add(line);
        }
    }

    private static double snap(double y) {
        return ((int) y) + HALF_PIXEL_OFFSET;
    }
}
