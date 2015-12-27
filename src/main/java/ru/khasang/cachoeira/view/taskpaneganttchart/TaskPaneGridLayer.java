package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ru.khasang.cachoeira.view.UIControl;

public class TaskPaneGridLayer extends Pane {
    // This is to make the stroke be drawn 'on pixel'.
    private static final double HALF_PIXEL_OFFSET = -0.5;

    private final Canvas canvas = new Canvas();
    private UIControl uiControl;

    public TaskPaneGridLayer(UIControl uiControl) {
        this.uiControl = uiControl;

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
        final int spacing = uiControl.getZoomMultiplier();

        canvas.setLayoutX(left);
        canvas.setLayoutY(top);

        if (width != canvas.getWidth() || height != canvas.getHeight()) {
            canvas.setWidth(width);
            canvas.setHeight(height);

            GraphicsContext g = canvas.getGraphicsContext2D();
            g.clearRect(0, 0, width, height);
            g.setStroke(Color.rgb(237, 237, 237));

            if (spacing <= 130 && spacing >= 55) {
                final int vLineCount = (int) Math.floor((width + 1) / spacing);

                for (int i = 0; i < vLineCount; i++) {
                    g.strokeLine(snap((i + 1) * spacing), 0, snap((i + 1) * spacing), height);
                }
            }
            if (spacing <= 54 && spacing >= 40) {
                final int vLineCount = (int) Math.floor((width + 1) / (spacing * 2));

                for (int i = 0; i < vLineCount; i++) {
                    g.strokeLine(snap((i + 1) * (spacing * 2)), 0, snap((i + 1) * (spacing * 2)), height);
                }
            }
            if (spacing <= 39 && spacing >= 20) {
                final int vLineCount = (int) Math.floor((width + 1) / (spacing * 4));

                for (int i = 0; i < vLineCount; i++) {
                    g.strokeLine(snap((i + 1) * (spacing * 4)), 0, snap((i + 1) * (spacing * 4)), height);
                }
            }
            if (spacing <= 19 && spacing >= 8) {
                final int vLineCount = (int) Math.floor((width + 1) / (spacing * 7));

                for (int i = 0; i < vLineCount; i++) {
                    g.strokeLine(snap((i + 1) * (spacing * 7)), 0, snap((i + 1) * (spacing * 7)), height);
                }
            }
            if (spacing <= 7 && spacing >= 4) {
                final int vLineCount = (int) Math.floor((width + 1) / (spacing * 14));

                for (int i = 0; i < vLineCount; i++) {
                    g.strokeLine(snap((i + 1) * (spacing * 14)), 0, snap((i + 1) * (spacing * 14)), height);
                }
            }
            if (spacing <= 3 && spacing >= 2) {
                final int vLineCount = (int) Math.floor((width + 1) / (spacing * 28));

                for (int i = 0; i < vLineCount; i++) {
                    g.strokeLine(snap((i + 1) * (spacing * 28)), 0, snap((i + 1) * (spacing * 28)), height);
                }
            }
        }
    }

    private static double snap(double y) {
        return ((int) y) + HALF_PIXEL_OFFSET;
    }
}