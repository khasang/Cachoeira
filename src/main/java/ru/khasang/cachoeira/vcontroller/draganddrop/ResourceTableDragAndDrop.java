package ru.khasang.cachoeira.vcontroller.draganddrop;

import javafx.scene.control.TreeTableRow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import ru.khasang.cachoeira.commands.project.DragNDropResourceCommand;
import ru.khasang.cachoeira.vcontroller.MainWindowController;

public class ResourceTableDragAndDrop extends TableDragAndDrop {
    public ResourceTableDragAndDrop(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void dragDropped(DragEvent event, TreeTableRow row) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
            int draggedIndex = (Integer) dragboard.getContent(SERIALIZED_MIME_TYPE);
            controller.getCommandExecutor().execute(new DragNDropResourceCommand(controller.getProject(), row, draggedIndex));
            event.setDropCompleted(true);
            row.getTreeTableView().getSelectionModel().select(row.getIndex());
            event.consume();
        }
    }
}
