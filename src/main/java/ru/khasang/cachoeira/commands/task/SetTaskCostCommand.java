package ru.khasang.cachoeira.commands.task;

import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.ITask;

public class SetTaskCostCommand implements Command {
    private final ITask task;
    private final double cost;
    private double oldCost;

    public SetTaskCostCommand(ITask task, double cost) {
        this.task = task;
        this.cost = cost;
    }

    @Override
    public void execute() {
        oldCost = task.getCost();
        task.setCost(cost);
    }

    @Override
    public void undo() {
        task.setCost(oldCost);
    }

    @Override
    public void redo() {

    }
}
