package ru.khasang.cachoeira.view.mainwindow.tooltipfactory;

import javafx.scene.control.Tooltip;

public interface TooltipFactory<T> {
    Tooltip createTooltip(T object);
}
