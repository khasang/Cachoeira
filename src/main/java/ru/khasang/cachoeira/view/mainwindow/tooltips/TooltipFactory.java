package ru.khasang.cachoeira.view.mainwindow.tooltips;

import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

public class TooltipFactory {
    public Tooltip createTooltip(IResource resource) {
        return new ResourceTooltip(resource);
    }

    public Tooltip createTooltip(ITask task) {
        return new TaskTooltip(task);
    }
}
