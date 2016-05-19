package ru.khasang.cachoeira.vcontroller.draganddrop;

import javafx.scene.control.TreeTableRow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import ru.khasang.cachoeira.commands.project.DragNDropTaskCommand;
import ru.khasang.cachoeira.vcontroller.MainWindowController;

public class TaskTableDragAndDrop extends TableDragAndDrop {
    public TaskTableDragAndDrop(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void dragDropped(DragEvent event, TreeTableRow row) {
        Dragboard db = event.getDragboard();
        if (db.hasContent(SERIALIZED_MIME_TYPE)) {
            int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
            controller.getCommandExecutor().execute(new DragNDropTaskCommand(controller.getProject(), row, draggedIndex));
            event.setDropCompleted(true);
            row.getTreeTableView().getSelectionModel().select(row.getIndex());
            event.consume();
        }
    }
}
