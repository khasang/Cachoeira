package ru.khasang.cachoeira.commands.project;

import javafx.scene.control.TreeTableRow;
import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;

public class DragNDropResourceCommand implements Command {
    private final IProject project;
    private final TreeTableRow<IResource> row;
    private final int draggedIndex;
    private IResource draggedResource;

    public DragNDropResourceCommand(IProject project, TreeTableRow<IResource> row, int draggedIndex) {
        this.project = project;
        this.row = row;
        this.draggedIndex = draggedIndex;
    }

    @Override
    public void execute() {
        draggedResource = project.getResourceList().remove(draggedIndex);
        int dropIndex;
        if (row.isEmpty()) {
            dropIndex = row.getTreeTableView().getRoot().getChildren().size();
        } else {
            dropIndex = row.getIndex();
        }
        project.getResourceList().add(dropIndex, draggedResource);
    }

    @Override
    public void undo() {
        project.getResourceList().remove(draggedResource);
        project.getResourceList().add(draggedIndex, draggedResource);
    }
}
