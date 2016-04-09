package ru.khasang.cachoeira.view.mainwindow.tooltipfactory;

import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.view.mainwindow.tooltipfactory.tooltips.ResourceTooltip;

public class ResourceTooltipFactory implements TooltipFactory<IResource> {
    @Override
    public Tooltip createTooltip(IResource resource) {
        return new ResourceTooltip(resource);
    }
}
