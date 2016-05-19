package ru.khasang.cachoeira.vcontroller.draganddrop;

import javafx.scene.control.TreeTableRow;
import javafx.scene.input.*;
import ru.khasang.cachoeira.vcontroller.MainWindowController;

public abstract class TableDragAndDrop {
    protected static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    protected MainWindowController controller;

    public void dragDetected(MouseEvent event, TreeTableRow<?> row) {
        if (!row.isEmpty()) {
            Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);
            dragboard.setDragView(row.snapshot(null, null));
            ClipboardContent content = new ClipboardContent();
            content.put(SERIALIZED_MIME_TYPE, row.getIndex());
            dragboard.setContent(content);
            event.consume();
        }
    }

    public void dragOver(DragEvent event, TreeTableRow row) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
            if (row.getIndex() != (Integer) dragboard.getContent(SERIALIZED_MIME_TYPE)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        }
    }

    public abstract void dragDropped(DragEvent event, TreeTableRow row);
}
