package ru.khasang.cachoeira.viewcontroller.mainwindow.rowfactories;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.project.DragNDropResourceCommand;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.viewcontroller.mainwindow.ResourcePaneController;
import ru.khasang.cachoeira.viewcontroller.mainwindow.contextmenus.ResourceContextMenu;
import ru.khasang.cachoeira.viewcontroller.mainwindow.tooltipfactory.ResourceTooltipFactory;
import ru.khasang.cachoeira.viewcontroller.mainwindow.tooltipfactory.TooltipFactory;

/**
 * Класс отвечающий за дополнительные фичи (контекстное меню, всплывающие подсказки, изменение порядка элементов с
 * помощью мышки) для каждой строки таблицы Ресурсов.
 */
public class ResourceTableViewRowFactory implements Callback<TableView<IResource>, TableRow<IResource>> {
    private final ResourcePaneController resourcePaneController;
    private IController controller;

    public ResourceTableViewRowFactory(ResourcePaneController resourcePaneController, IController controller) {
        this.resourcePaneController = resourcePaneController;
        this.controller = controller;
    }

    @Override
    public TableRow<IResource> call(TableView<IResource> param) {
        TableRow<IResource> row = new TableRow<IResource>() {
            /* Tooltip & Context Menu */
            TooltipFactory<IResource> tooltipFactory = new ResourceTooltipFactory();
            ResourceContextMenu resourceContextMenu = new ResourceContextMenu();

            @Override
            protected void updateItem(IResource resource, boolean empty) {
                super.updateItem(resource, empty);
                if (empty) {
                    setTooltip(null);
                } else {
                    setTooltip(tooltipFactory.createTooltip(resource));
                    resourceContextMenu.initMenus(controller, resource);
                    setContextMenu(resourceContextMenu);
                }
            }
        };

        /* Drag & Drop */
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
                CommandControl.getInstance().execute(new DragNDropResourceCommand(controller.getProject(), row, draggedIndex));
                event.setDropCompleted(true);
                resourcePaneController.getResourceTableView().getSelectionModel().select(row.getIndex());
                event.consume();
            }
        });
        return row;
    }
}