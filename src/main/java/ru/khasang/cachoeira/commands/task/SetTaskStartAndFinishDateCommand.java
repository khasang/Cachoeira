package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

import java.time.LocalDate;

public class SetTaskStartAndFinishDateCommand implements Command {
    private final ITask task;
    private final LocalDate startDate;
    private final LocalDate finishDate;
    private LocalDate oldStartDate;
    private LocalDate oldFinishDate;

    public SetTaskStartAndFinishDateCommand(ITask task, LocalDate startDate, LocalDate finishDate) {
        this.task = task;
        this.startDate = startDate;
        this.finishDate = finishDate;
        System.out.println(startDate);
        System.out.println(finishDate);
    }

    @Override
    public void execute() {
        oldStartDate = task.getStartDate();
        oldFinishDate = task.getFinishDate();
        task.setStartDate(startDate);
        task.setFinishDate(finishDate);
    }

    @Override
    public void undo() {
        System.out.println("------");
        System.out.println(oldStartDate);
        System.out.println(oldFinishDate);
        System.out.println();
        task.setStartDate(oldStartDate);
        task.setFinishDate(oldFinishDate);
    }
}
