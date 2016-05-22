package ru.khasang.cachoeira.viewcontroller.rowfactories;

import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.viewcontroller.contextmenus.ResourceContextMenu;
import ru.khasang.cachoeira.viewcontroller.draganddrop.TableDragAndDrop;
import ru.khasang.cachoeira.viewcontroller.tooltipfactory.ResourceTooltipFactory;
import ru.khasang.cachoeira.viewcontroller.tooltipfactory.TooltipFactory;

/**
 * Класс отвечающий за дополнительные фичи (контекстное меню, всплывающие подсказки, изменение порядка элементов с
 * помощью мышки) для каждой строки таблицы Ресурсов.
 */
public class ResourceTreeTableViewRowFactory implements Callback<TreeTableView<IResource>, TreeTableRow<IResource>> {
    private final MainWindowController controller;
    private final TableDragAndDrop dragAndDrop;

    public ResourceTreeTableViewRowFactory(MainWindowController controller, TableDragAndDrop dragAndDrop) {
        this.controller = controller;
        this.dragAndDrop = dragAndDrop;
    }

    @Override
    public TreeTableRow<IResource> call(TreeTableView<IResource> param) {
        TreeTableRow<IResource> row = new TreeTableRow<IResource>() {
            @Override
            protected void updateItem(IResource resource, boolean empty) {
                super.updateItem(resource, empty);
                if (empty) {
                    this.setTooltip(null);
                } else {
                    /* Tooltip & Context Menu */
                    TooltipFactory<IResource> tooltipFactory = new ResourceTooltipFactory();
                    ResourceContextMenu resourceContextMenu = new ResourceContextMenu(controller.getProject(), resource, controller);
                    this.setTooltip(tooltipFactory.createTooltip(resource));
                    resourceContextMenu.initMenus();
                    this.setContextMenu(resourceContextMenu);
                }
            }
        };

        /* Drag & Drop */
        row.setOnDragDetected(event -> dragAndDrop.dragDetected(event, row));
        row.setOnDragOver(event -> dragAndDrop.dragOver(event, row));
        row.setOnDragDropped(event -> dragAndDrop.dragDropped(event, row));
        return row;
    }
}
