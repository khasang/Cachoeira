package ru.khasang.cachoeira.commands.project;

import javafx.scene.control.TreeTableRow;
import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;

public class DragNDropTaskCommand implements Command {
    private final IProject project;
    private final TreeTableRow<ITask> row;
    private final int draggedIndex;
    private ITask draggedTask;

    public DragNDropTaskCommand(IProject project, TreeTableRow<ITask> row, int draggedIndex) {
        this.project = project;
        this.row = row;
        this.draggedIndex = draggedIndex;
    }

    @Override
    public void execute() {
        draggedTask = project.getTaskList().remove(draggedIndex);
        int dropIndex;
        if (row.isEmpty()) {
            dropIndex = row.getTreeTableView().getRoot().getChildren().size();
        } else {
            dropIndex = row.getIndex();
        }
        project.getTaskList().add(dropIndex, draggedTask);
    }

    @Override
    public void undo() {
        project.getTaskList().remove(draggedTask);
        project.getTaskList().add(draggedIndex, draggedTask);
    }
}
