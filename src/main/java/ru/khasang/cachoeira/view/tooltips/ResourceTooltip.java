package ru.khasang.cachoeira.view.tooltips;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.view.UIControl;

import java.util.ResourceBundle;

/**
 * Класс описывающий поведение всплывающей подсказки при наведении курсора на ресурс.
 */
public class ResourceTooltip extends Tooltip {
    public ResourceTooltip() {
    }

    public ResourceTooltip(IResource resource) {
        initToolTip(resource);
    }

    public void initToolTip(IResource resource) {
        ResourceBundle resourceBundle = UIControl.BUNDLE;
        textProperty().bind(Bindings
                .concat(resourceBundle.getString("resource_name") + ": ").concat(resource.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(resource.descriptionProperty().isNull().or(resource.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat(resourceBundle.getString("description") + ": ").concat(resource.descriptionProperty()).concat("\n")))
                .concat(Bindings
                        .when(resource.emailProperty().isNull().or(resource.emailProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat(resourceBundle.getString("email") + ": ").concat(resource.emailProperty()).concat("\n")))
                .concat(resourceBundle.getString("resource_type") + ": ").concat(resource.resourceTypeProperty())
        );
    }
}
