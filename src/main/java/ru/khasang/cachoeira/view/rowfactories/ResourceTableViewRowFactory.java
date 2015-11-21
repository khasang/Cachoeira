package ru.khasang.cachoeira.view.rowfactories;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.MainWindow;

/**
 * Created by truesik on 25.10.2015.
 */
public class ResourceTableViewRowFactory implements Callback<TableView<IResource>, TableRow<IResource>> {
    private final MainWindow mainWindow;
    private IController controller;

    public ResourceTableViewRowFactory(MainWindow mainWindow, IController controller) {
        this.mainWindow = mainWindow;
        this.controller = controller;
    }

    @Override
    public TableRow<IResource> call(TableView<IResource> param) {
        TableRow<IResource> row = new TableRow<>();

        /** Drag & Drop **/
        row.setOnDragDetected(event -> {
            if (!row.isEmpty()) {
                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(row.snapshot(null, null));
                ClipboardContent cc = new ClipboardContent();
                cc.put(Controller.getSerializedMimeType(), row.getIndex());
                db.setContent(cc);
                event.consume();
            }
        });

        row.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(Controller.getSerializedMimeType())) {
                if (row.getIndex() != (Integer) db.getContent(Controller.getSerializedMimeType())) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            }
        });

        row.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(Controller.getSerializedMimeType())) {
                int draggedIndex = (Integer) db.getContent(Controller.getSerializedMimeType());
                IResource draggedResource = mainWindow.getResourceTableView().getItems().remove(draggedIndex);
                int dropIndex;
                if (row.isEmpty()) {
                    dropIndex = mainWindow.getResourceTableView().getItems().size();
                } else {
                    dropIndex = row.getIndex();
                }
                mainWindow.getResourceTableView().getItems().add(dropIndex, draggedResource);
                event.setDropCompleted(true);
                mainWindow.getResourceTableView().getSelectionModel().select(dropIndex);
                event.consume();
            }
        });


        /** Row Context Menu**/
        ContextMenu rowMenu = new ContextMenu();
        Menu setTask = new Menu("Назначить задачу");

        MenuItem getProperties = new MenuItem("Свойства");
        MenuItem removeResource = new MenuItem("Удалить ресурс");

        getProperties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainWindow.getController().setSelectedResource(row.getItem());
                mainWindow.openPropertiesResourceWindow();
            }
        });
        removeResource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainWindow.getController().handleRemoveResource(row.getItem());
            }
        });
        rowMenu.getItems().addAll(setTask, getProperties, removeResource);  //заполняем меню

        rowMenu.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                refreshTaskMenu(setTask, row);
            }
        });

        row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))   //если на не пустом месте кликаем,
                        .then(rowMenu)                                  //то выводим одно меню,
                        .otherwise((ContextMenu) null));                //а если на пустом, то другое
        return row;
    }

    private void refreshTaskMenu(Menu setTask, TableRow<IResource> row) {
        setTask.getItems().clear();
        for (ITask task : mainWindow.getController().getProject().getTaskList()) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(task.getName());
            for (IResource resource : task.getResourceList()) {
                if (resource.equals(row.getItem())) {
                    checkMenuItem.selectedProperty().setValue(Boolean.TRUE);
                    break;
                } else {
                    checkMenuItem.selectedProperty().setValue(Boolean.FALSE);
                }
            }
            checkMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (checkMenuItem.isSelected()) {
                        task.addResource(row.getItem());
                    } else {
                        task.removeResource(row.getItem());
                    }
                }
            });
            setTask.getItems().add(checkMenuItem);
        }
    }
}
