package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class BarLayerFactoryImpl implements BarLayerFactory {
    @Override
    public Rectangle createDomainBar(TaskBar taskBar) {
        TaskBarLayer barLayer = new DomainBar(taskBar);
        return barLayer.create();
    }

    @Override
    public Rectangle createProgressBar(TaskBar taskBar) {
        TaskBarLayer barLayer = new ProgressBar(taskBar);
        return barLayer.create();
    }

    @Override
    public Rectangle createBackgroundBar(TaskBar taskBar) {
        TaskBarLayer barLayer = new BackgroundBar(taskBar);
        return barLayer.create();
    }

    @Override
    public Rectangle createLeftResizingAnchor(TaskBar taskBar) {
        ResizingAnchor resizingAnchor = new LeftResizingAnchor(taskBar);
        return resizingAnchor.create();
    }

    @Override
    public Rectangle createRightResizingAnchor(TaskBar taskBar) {
        ResizingAnchor resizingAnchor = new RightResizingAnchor(taskBar);
        return resizingAnchor.create();
    }
}
