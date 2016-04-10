package ru.khasang.cachoeira.view.mainwindow.tooltipfactory.tooltips;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.view.UIControl;

import java.util.ResourceBundle;

/**
 * Класс описывающий поведение всплывающей подсказки при наведении курсора на ресурс.
 */
public class ResourceTooltip extends Tooltip {
    private ResourceBundle bundle = UIControl.bundle;

    public ResourceTooltip(IResource resource) {
        initTooltip(resource);
    }

    public void initTooltip(IResource resource) {
        textProperty().bind(Bindings
                .concat(bundle.getString("resource_name") + ": ").concat(resource.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(resource.descriptionProperty().isNull().or(resource.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat(bundle.getString("description") + ": ").concat(resource.descriptionProperty()).concat("\n")))
                .concat(Bindings
                        .when(resource.emailProperty().isNull().or(resource.emailProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat(bundle.getString("email") + ": ").concat(resource.emailProperty()).concat("\n")))
                .concat(bundle.getString("resource_type") + ": ").concat(resource.resourceTypeProperty())
        );
    }
}
