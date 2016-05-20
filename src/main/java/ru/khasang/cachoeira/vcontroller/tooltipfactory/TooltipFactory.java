package ru.khasang.cachoeira.vcontroller.tooltipfactory;

import javafx.scene.control.Tooltip;

public interface TooltipFactory<T> {
    Tooltip createTooltip(T object);
}
