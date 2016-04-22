package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;

import java.time.LocalDate;

public class SetProjectStartDateCommand implements Command {
    private final IProject project;
    private final LocalDate startDate;
    private LocalDate oldStartDate;

    public SetProjectStartDateCommand(IProject project, LocalDate startDate) {
        this.project = project;
        this.startDate = startDate;
    }

    @Override
    public void execute() {
        oldStartDate = project.getStartDate();
        project.setStartDateAndVerify(startDate);
    }

    @Override
    public void undo() {
        project.setStartDateAndVerify(oldStartDate);
    }
}
