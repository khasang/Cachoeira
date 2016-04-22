package ru.khasang.cachoeira.commands.project;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;

import java.time.LocalDate;

public class SetProjectFinishDateCommand implements Command {
    private final IProject project;
    private final LocalDate finishDate;
    private LocalDate oldFinishDate;

    public SetProjectFinishDateCommand(IProject project, LocalDate finishDate) {
        this.project = project;
        this.finishDate = finishDate;
    }

    @Override
    public void execute() {
        oldFinishDate = project.getFinishDate();
        project.setFinishDateAndVerify(finishDate);
    }

    @Override
    public void undo() {
        project.setFinishDateAndVerify(oldFinishDate);
    }
}
