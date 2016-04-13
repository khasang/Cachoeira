package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

import java.time.LocalDate;

public class SetTaskFinishDateCommand implements Command {
    private final ITask task;
    private final LocalDate finishDate;
    private LocalDate oldFinishDate;

    public SetTaskFinishDateCommand(ITask task, LocalDate finishDate) {
        this.task = task;
        this.finishDate = finishDate;
    }

    @Override
    public void execute() {
        oldFinishDate = task.getFinishDate();
        task.setFinishDate(finishDate);
    }

    @Override
    public void undo() {
        task.setFinishDate(oldFinishDate);
    }
}
