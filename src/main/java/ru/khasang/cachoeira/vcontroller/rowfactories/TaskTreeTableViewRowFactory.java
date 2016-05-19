package ru.khasang.cachoeira.vcontroller.rowfactories;

import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.vcontroller.contextmenus.TaskContextMenu;
import ru.khasang.cachoeira.vcontroller.draganddrop.TableDragAndDrop;
import ru.khasang.cachoeira.vcontroller.tooltipfactory.TaskTooltipFactory;
import ru.khasang.cachoeira.vcontroller.tooltipfactory.TooltipFactory;

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
        return row;
    }
}