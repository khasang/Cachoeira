package ru.khasang.cachoeira.view.tooltips;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * Класс описывающий поведение всплывающей подсказки при наведении курсора на ресурс.
 */
public class TaskTooltip extends Tooltip {
    public TaskTooltip() {
    }

    public TaskTooltip(ITask task) {
        initToolTip(task);
    }

    public void initToolTip(ITask task) {
        ResourceBundle resourceBundle = UIControl.BUNDLE;
        textProperty().bind(Bindings
                .concat(resourceBundle.getString("task_name") + ": ").concat(task.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(task.descriptionProperty().isNull().or(task.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat(resourceBundle.getString("description") + ": ").concat(task.descriptionProperty()).concat("\n")))
                .concat(resourceBundle.getString("start_date") + ": ").concat(task.startDateProperty()).concat("\n")
                .concat(resourceBundle.getString("finish_date") + ": ").concat(task.finishDateProperty()).concat("\n")
                .concat(resourceBundle.getString("duration") + ": ").concat(ChronoUnit.DAYS.between(task.startDateProperty().getValue(), task.finishDateProperty().getValue())).concat("\n")
                .concat(resourceBundle.getString("done_percent") + ": ").concat(task.donePercentProperty()).concat("\n")
                .concat(resourceBundle.getString("cost") + ": ").concat(task.costProperty()).concat("\n")
                .concat(resourceBundle.getString("priority_type") + ": ").concat(task.priorityTypeProperty())
        );
    }
}
