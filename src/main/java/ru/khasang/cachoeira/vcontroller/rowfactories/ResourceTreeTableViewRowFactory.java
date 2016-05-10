package ru.khasang.cachoeira.vcontroller.rowfactories;

import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.vcontroller.contextmenus.ResourceContextMenu;
import ru.khasang.cachoeira.vcontroller.draganddrop.TableDragAndDrop;
import ru.khasang.cachoeira.vcontroller.tooltipfactory.ResourceTooltipFactory;
import ru.khasang.cachoeira.vcontroller.tooltipfactory.TooltipFactory;

/**
 * Класс отвечающий за дополнительные фичи (контекстное меню, всплывающие подсказки, изменение порядка элементов с
 * помощью мышки) для каждой строки таблицы Ресурсов.
 */
public class ResourceTreeTableViewRowFactory implements Callback<TreeTableView<IResource>, TreeTableRow<IResource>> {
    private final IProject project;
    private final TableDragAndDrop dragAndDrop;

    public ResourceTreeTableViewRowFactory(IProject project, TableDragAndDrop dragAndDrop) {
        this.project = project;
        this.dragAndDrop = dragAndDrop;
    }

    @Override
    public TreeTableRow<IResource> call(TreeTableView<IResource> param) {
        TreeTableRow<IResource> row = new TreeTableRow<IResource>() {
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
                    resourceContextMenu.initMenus(project, resource);
                    setContextMenu(resourceContextMenu);
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
