package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

import java.time.LocalDate;

public class SetTaskStartAndFinishDateCommand implements Command {
    private final ITask task;
    private final LocalDate startDate;
    private final long taskWidthInDays;
    private LocalDate oldStartDate;
    private LocalDate oldFinishDate;

    public SetTaskStartAndFinishDateCommand(ITask task, LocalDate startDate, long taskWidthInDays) {
        this.task = task;
        this.startDate = startDate;
        this.taskWidthInDays = taskWidthInDays;
    }

    @Override
    public void execute() {
        oldStartDate = task.getStartDate();
        oldFinishDate = task.getFinishDate();
        task.setStartDateAndVerify(startDate);
        task.setFinishDateAndVerify(startDate.plusDays(taskWidthInDays));
    }

    @Override
    public void undo() {
        task.setStartDateAndVerify(oldStartDate);
        task.setFinishDateAndVerify(oldFinishDate);
    }
}
