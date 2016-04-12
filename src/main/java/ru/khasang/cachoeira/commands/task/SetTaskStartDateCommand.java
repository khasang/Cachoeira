package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

import java.time.LocalDate;

public class SetTaskStartDateCommand implements Command {
    private final ITask task;
    private final LocalDate startDate;
    private LocalDate oldStartDate;

    public SetTaskStartDateCommand(ITask task, LocalDate startDate) {
        this.task = task;
        this.startDate = startDate;
    }

    @Override
    public void execute() {
        oldStartDate = task.getStartDate();
        task.setStartDate(startDate);
    }

    @Override
    public void undo() {
        task.setStartDate(oldStartDate);
    }

    @Override
    public void redo() {

    }
}
