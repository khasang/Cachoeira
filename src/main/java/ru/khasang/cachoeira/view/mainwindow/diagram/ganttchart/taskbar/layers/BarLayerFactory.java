package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers;

import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public interface BarLayerFactory {
    Rectangle createDomainBar(TaskBar taskBar);

    Rectangle createProgressBar(TaskBar taskBar);

    Rectangle createBackgroundBar(TaskBar taskBar);

    Rectangle createLeftResizingAnchor(TaskBar taskBar);

    Rectangle createRightResizingAnchor(TaskBar taskBar);
}
