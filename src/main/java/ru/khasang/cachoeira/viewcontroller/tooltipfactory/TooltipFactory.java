package ru.khasang.cachoeira.viewcontroller.tooltipfactory;

import javafx.scene.control.Tooltip;

public interface TooltipFactory<T> {
    Tooltip createTooltip(T object);
}
