package ru.khasang.cachoeira.view.mainwindow.tooltipfactory;

import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.tooltipfactory.tooltips.TaskTooltip;

public class TaskTooltipFactory implements TooltipFactory<ITask>{
    @Override
    public Tooltip createTooltip(ITask task) {
        return new TaskTooltip(task);
    }
}
