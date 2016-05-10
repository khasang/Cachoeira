package ru.khasang.cachoeira.vcontroller.draganddrop;

import javafx.scene.control.TreeTableRow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.project.DragNDropResourceCommand;
import ru.khasang.cachoeira.model.IProject;

public class ResourceTableDragAndDrop extends TableDragAndDrop {
    public ResourceTableDragAndDrop(IProject project) {
        super(project);
    }

    @Override
    public void dragDropped(DragEvent event, TreeTableRow row) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
            int draggedIndex = (Integer) dragboard.getContent(SERIALIZED_MIME_TYPE);
            CommandControl.getInstance().execute(new DragNDropResourceCommand(project, row, draggedIndex));
            event.setDropCompleted(true);
            row.getTreeTableView().getSelectionModel().select(row.getIndex());
            event.consume();
        }
    }
}
