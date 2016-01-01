package ru.khasang.cachoeira.view.resourcepaneganttchart;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ru.khasang.cachoeira.view.UIControl;

/**
 * Класс который рисует сетку на заднем фоне диаграммы Ганта.
 * <p>
 * При слишком длинном проекте (в полгода и более, в зависимости от мощности компьютера)
 * выдает эксепшон. Проблема заключается в том что сетка рисуется на канвасе, а канвас это картинка.
 * Соответственно при длинном проекте размер картинки получается, что-то вроде 100000x1080.
 * Вероятно придется это дело менять, либо делать так чтобы канвас рисовался только тогда когда он попадает во вьюпорт
 * в главном окне.
 */

public class ResourcePaneGridLayer extends Pane {
    // This is to make the stroke be drawn 'on pixel'.
    private static final double HALF_PIXEL_OFFSET = -0.5;

    private final Canvas canvas = new Canvas();
    private UIControl uiControl;

    public ResourcePaneGridLayer(UIControl uiControl) {
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
        final int spacing = uiControl.getZoomMultiplier(); // Ширина дня в пикселях

        canvas.setLayoutX(left);
        canvas.setLayoutY(top);

        if (width != canvas.getWidth() || height != canvas.getHeight()) {
            canvas.setWidth(width);
            canvas.setHeight(height);

            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, width, height);
            graphicsContext.setStroke(Color.rgb(237, 237, 237));

            if (spacing <= 130 && spacing >= 55) {
                drawLines(width, height, spacing, graphicsContext, 1);
            }
            if (spacing <= 54 && spacing >= 40) {
                drawLines(width, height, spacing, graphicsContext, 2);
            }
            if (spacing <= 39 && spacing >= 20) {
                drawLines(width, height, spacing, graphicsContext, 4);
            }
            if (spacing <= 19 && spacing >= 8) {
                drawLines(width, height, spacing, graphicsContext, 7);
            }
            if (spacing <= 7 && spacing >= 4) {
                drawLines(width, height, spacing, graphicsContext, 14);
            }
            if (spacing <= 3 && spacing >= 2) {
                drawLines(width, height, spacing, graphicsContext, 28);
            }
        }
    }

    /**
     * При вызове этого метода рисуются вертикальные линии по всей ширине GridLayer'а
     *
     * @param width           Ширина панели
     * @param height          Высота панели
     * @param spacing         Значение зума
     * @param graphicsContext Картина-полотно, которая лежит поверх панели и на которой и рисуем линии
     * @param multiplier      Множитель для увеличения расстояния между линиями
     */
    private void drawLines(int width,
                           int height,
                           int spacing,
                           GraphicsContext graphicsContext,
                           int multiplier) {
        final int vLineCount = (int) Math.floor((width + 1) / (spacing * multiplier));

        for (int i = 0; i < vLineCount; i++) {
            graphicsContext.strokeLine(snap((i + 1) * (spacing * multiplier)), 0, snap((i + 1) * (spacing * multiplier)), height);
        }
    }

    private static double snap(double y) {
        return ((int) y) + HALF_PIXEL_OFFSET;
    }
}