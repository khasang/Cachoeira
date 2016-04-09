package ru.khasang.cachoeira.view.mainwindow.tooltips;

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
    private ResourceBundle bundle = UIControl.bundle;

    public TaskTooltip(ITask task) {
        initTooltip(task);
    }

    public void initTooltip(ITask task) {
        textProperty().bind(Bindings
                .concat(bundle.getString("task_name") + ": ").concat(task.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(task.descriptionProperty().isNull().or(task.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat(bundle.getString("description") + ": ").concat(task.descriptionProperty()).concat("\n")))
                .concat(bundle.getString("start_date") + ": ").concat(task.startDateProperty()).concat("\n")
                .concat(bundle.getString("finish_date") + ": ").concat(task.finishDateProperty()).concat("\n")
                .concat(bundle.getString("duration") + ": ").concat(ChronoUnit.DAYS.between(task.startDateProperty().getValue(), task.finishDateProperty().getValue())).concat("\n")
                .concat(bundle.getString("done_percent") + ": ").concat(task.donePercentProperty()).concat("\n")
                .concat(bundle.getString("cost") + ": ").concat(task.costProperty()).concat("\n")
                .concat(bundle.getString("priority_type") + ": ").concat(task.priorityTypeProperty())
        );
    }
}
