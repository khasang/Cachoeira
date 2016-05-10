package ru.khasang.cachoeira.vcontroller.draganddrop;

import javafx.scene.control.TreeTableRow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.project.DragNDropTaskCommand;
import ru.khasang.cachoeira.model.IProject;

public class TaskTableDragAndDrop extends TableDragAndDrop {
    public TaskTableDragAndDrop(IProject project) {
        super(project);
    }

    @Override
    public void dragDropped(DragEvent event, TreeTableRow row) {
        Dragboard db = event.getDragboard();
        if (db.hasContent(SERIALIZED_MIME_TYPE)) {
            int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
            CommandControl.getInstance().execute(new DragNDropTaskCommand(project, row, draggedIndex));
            event.setDropCompleted(true);
            row.getTreeTableView().getSelectionModel().select(row.getIndex());
            event.consume();
        }
    }
}
