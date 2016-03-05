package ru.khasang.cachoeira.view.tooltips;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Tooltip;
import ru.khasang.cachoeira.model.ITask;

import java.time.temporal.ChronoUnit;

public class TaskTooltip extends Tooltip {
    public TaskTooltip() {
    }

    public TaskTooltip(ITask task) {
        initToolTip(task);
    }

    public void initToolTip(ITask task) {
        textProperty().bind(Bindings
                .concat("Наименование: ").concat(task.nameProperty()).concat("\n")
                .concat(Bindings
                        .when(task.descriptionProperty().isNull().or(task.descriptionProperty().isEmpty()))
                        .then("")
                        .otherwise(Bindings.concat("Описание: ").concat(task.descriptionProperty()).concat("\n")))
                .concat("Дата начала: ").concat(task.startDateProperty()).concat("\n")
                .concat("Дата окончания: ").concat(task.finishDateProperty()).concat("\n")
                .concat("Продолжительность: ").concat(ChronoUnit.DAYS.between(task.startDateProperty().getValue(), task.finishDateProperty().getValue())).concat("\n")
                .concat("Прогресс: ").concat(task.donePercentProperty()).concat("\n")
                .concat("Стоимость: ").concat(task.costProperty()).concat("\n")
                .concat("Приоритет: ").concat(task.priorityTypeProperty())
        );
    }
}
