package ru.khasang.cachoeira.vcontroller.tooltipfactory.tooltips;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.IResource;

/**
 * Класс описывающий поведение всплывающей подсказки при наведении курсора на ресурс.
 */
public class ResourceTooltip extends Tooltip {
    public ResourceTooltip(IResource resource) {
        initTooltip(resource);
    }

    public void initTooltip(IResource resource) {
        textProperty().bind(Bindings
                .concat("resource_name" + ": ").concat(resource.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(resource.descriptionProperty().isNull().or(resource.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat("description" + ": ").concat(resource.descriptionProperty()).concat("\n")))
                .concat(Bindings
                        .when(resource.emailProperty().isNull().or(resource.emailProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat("email" + ": ").concat(resource.emailProperty()).concat("\n")))
                .concat("resource_type" + ": ").concat(resource.resourceTypeProperty())
        );
    }
}
