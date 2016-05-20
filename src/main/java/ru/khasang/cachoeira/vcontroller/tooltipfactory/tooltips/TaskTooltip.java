package ru.khasang.cachoeira.vcontroller.tooltipfactory.tooltips;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.ITask;

import java.time.temporal.ChronoUnit;

/**
 * Класс описывающий поведение всплывающей подсказки при наведении курсора на ресурс.
 */
public class TaskTooltip extends Tooltip {
    public TaskTooltip(ITask task) {
        initTooltip(task);
    }

    public void initTooltip(ITask task) {
        textProperty().bind(Bindings
                .concat("task_name" + ": ").concat(task.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(task.descriptionProperty().isNull().or(task.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat("description" + ": ").concat(task.descriptionProperty()).concat("\n")))
                .concat("start_date" + ": ").concat(task.startDateProperty()).concat("\n")
                .concat("finish_date" + ": ").concat(task.finishDateProperty()).concat("\n")
                .concat("duration" + ": ").concat(ChronoUnit.DAYS.between(task.startDateProperty().getValue(), task.finishDateProperty().getValue())).concat("\n")
                .concat("done_percent" + ": ").concat(task.donePercentProperty()).concat("\n")
                .concat("cost" + ": ").concat(task.costProperty())
        );
    }
}
