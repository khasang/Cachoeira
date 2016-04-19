package ru.khasang.cachoeira.view.mainwindow.rowfactories;

import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.project.DragNDropTaskCommand;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.TaskPaneController;
import ru.khasang.cachoeira.view.mainwindow.contextmenus.TaskContextMenu;
import ru.khasang.cachoeira.view.mainwindow.tooltipfactory.TaskTooltipFactory;
import ru.khasang.cachoeira.view.mainwindow.tooltipfactory.TooltipFactory;

/**
 * Класс отвечающий за дополнительные фичи (контекстное меню, всплывающие подсказки, изменение порядка элементов с
 * помощью мышки) для каждой строки таблицы Задач.
 */
public class TaskTreeTableViewRowFactory implements Callback<TreeTableView<ITask>, TreeTableRow<ITask>> {
    private TaskPaneController taskPaneController;
    private IController controller;

    public TaskTreeTableViewRowFactory(TaskPaneController taskPaneController, IController controller) {
        this.taskPaneController = taskPaneController;
        this.controller = controller;
    }

    @Override
    public TreeTableRow<ITask> call(TreeTableView<ITask> param) {
        TreeTableRow<ITask> row = new TreeTableRow<ITask>() {
            /* Tooltip & Context Menu */
            TooltipFactory<ITask> tooltipFactory = new TaskTooltipFactory();
            TaskContextMenu taskContextMenu = new TaskContextMenu();

            @Override
            protected void updateItem(ITask task, boolean empty) {
                super.updateItem(task, empty);
                if (empty) {
                    setTooltip(null);
                    setContextMenu(null);
                } else {
                    setTooltip(tooltipFactory.createTooltip(task));
                    taskContextMenu.initMenus(controller, task);
                    setContextMenu(taskContextMenu);
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
                CommandControl.getInstance().execute(new DragNDropTaskCommand(controller.getProject(), row, draggedIndex));
                event.setDropCompleted(true);
                taskPaneController.getTaskTreeTableView().getSelectionModel().select(row.getIndex());
                event.consume();
            }
        });
        return row;
    }
}