package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public abstract class ResizingAnchor implements TaskBarLayer {
    protected TaskBar taskBar;
    protected Delta delta;

    public ResizingAnchor(TaskBar taskBar) {
        this.taskBar = taskBar;
    }

    @Override
    public abstract Rectangle create();

    protected abstract void enableResize(Rectangle rectangle);

    // records relative x co-ordinate.
    protected class Delta {
        double x;
    }
}
