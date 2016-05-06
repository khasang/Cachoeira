package ru.khasang.cachoeira.viewcontroller.mainwindow.tooltipfactory;

import javafx.scene.control.Tooltip;

public interface TooltipFactory<T> {
    Tooltip createTooltip(T object);
}
