package ru.khasang.cachoeira.viewcontroller.rowfactories;

import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.viewcontroller.contextmenus.TaskContextMenu;
import ru.khasang.cachoeira.viewcontroller.draganddrop.TableDragAndDrop;
import ru.khasang.cachoeira.viewcontroller.tooltipfactory.TaskTooltipFactory;
import ru.khasang.cachoeira.viewcontroller.tooltipfactory.TooltipFactory;

/**
 * Класс отвечающий за дополнительные фичи (контекстное меню, всплывающие подсказки, изменение порядка элементов с
 * помощью мышки) для каждой строки таблицы Задач.
 */
public class TaskTreeTableViewRowFactory implements Callback<TreeTableView<ITask>, TreeTableRow<ITask>> {
    private final MainWindowController controller;
    private final TableDragAndDrop dragAndDrop;

    public TaskTreeTableViewRowFactory(MainWindowController controller, TableDragAndDrop dragAndDrop) {
        this.controller = controller;
        this.dragAndDrop = dragAndDrop;
    }

    @Override
    public TreeTableRow<ITask> call(TreeTableView<ITask> param) {
        TreeTableRow<ITask> row = new TreeTableRow<ITask>() {
            @Override
            protected void updateItem(ITask task, boolean empty) {
                super.updateItem(task, empty);
                if (empty) {
                    this.setTooltip(null);
                    this.setContextMenu(null);
                } else {
                    /* Tooltip & Context Menu */
                    TooltipFactory<ITask> tooltipFactory = new TaskTooltipFactory();
                    TaskContextMenu taskContextMenu = new TaskContextMenu(controller.getProject(), task, controller);
                    this.setTooltip(tooltipFactory.createTooltip(task));
                    taskContextMenu.initMenus();
                    this.setContextMenu(taskContextMenu);
                }
            }
        };

        /* Drag & Drop */
        row.setOnDragDetected(event -> dragAndDrop.dragDetected(event, row));
        row.setOnDragOver(event -> dragAndDrop.dragOver(event, row));
        row.setOnDragDropped(event -> dragAndDrop.dragDropped(event, row));
        /* Properties double-click */
        row.setOnMouseClicked(this::handleDoubleClick);
        return row;
    }

    private void handleDoubleClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            controller.getSidePanel().getSelectionModel().select(controller.getSidePanel().getTaskPropertiesTab());
        }
    }
}