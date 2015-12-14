package ru.khasang.cachoeira.view.tooltips;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.IResource;

/**
 * Created by truesik on 09.12.2015.
 */
public class ResourceTooltip extends Tooltip {
    public ResourceTooltip() {
    }

    public ResourceTooltip(IResource resource) {
        initToolTip(resource);
    }

    public void initToolTip(IResource resource) {
        textProperty().bind(Bindings
                .concat("Наименование: ").concat(resource.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(resource.descriptionProperty().isNull().or(resource.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat("Описание: ").concat(resource.descriptionProperty()).concat("\n")))
                .concat(Bindings
                        .when(resource.emailProperty().isNull().or(resource.emailProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat("Электронная почта: ").concat(resource.emailProperty()).concat("\n")))
                .concat("Тип ресурса: ").concat(resource.resourceTypeProperty())
        );
    }
}
