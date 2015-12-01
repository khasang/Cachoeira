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
import ru.khasang.cachoeira.view.TaskPaneController;

/**
 * Created by truesik on 18.11.2015.
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
            Tooltip tooltip = new Tooltip();
            @Override
            protected void updateItem(ITask task, boolean empty) {
                super.updateItem(task, empty);
                if (empty) {
                    setTooltip(null);
                } else {
                    tooltip.textProperty().bind(Bindings
                            .concat("Описание: ").concat(task.descriptionProperty()).concat("\n")
                            .concat("Дата начала: ").concat(task.startDateProperty()).concat("\n")
                            .concat("Дата окончания: ").concat(task.finishDateProperty()).concat("\n")
                            .concat("Прогресс: ").concat(task.donePercentProperty()).concat("\n")
                            .concat("Стоимость: ").concat(task.costProperty()));
                    setTooltip(tooltip);
                }
            }
        };

        /**Drag & Drop **/
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
                ITask draggedTask = controller.getProject().getTaskList().remove(draggedIndex);
                int dropIndex;
                if (row.isEmpty()) {
                    dropIndex = taskPaneController.getTaskTreeTableView().getRoot().getChildren().size();
                } else {
                    dropIndex = row.getIndex();
                }
                controller.getProject().getTaskList().add(dropIndex, draggedTask);
                event.setDropCompleted(true);
                taskPaneController.getTaskTreeTableView().getSelectionModel().select(dropIndex);
                event.consume();
            }
        });

        /** Row Context Menu **/
        ContextMenu rowMenu = new ContextMenu();
        Menu setResource = new Menu("Назначить ресурс");

        MenuItem getProperties = new MenuItem("Свойства");
        MenuItem removeTask = new MenuItem("Удалить задачу");

        getProperties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.setSelectedTask(row.getTreeItem().getValue());
//                taskPaneController.openPropertiesTaskWindow(); // TODO: 25.11.2015 исправить
            }
        });
        removeTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               controller.handleRemoveTask(row.getTreeItem().getValue());
            }
        });
        rowMenu.getItems().addAll(setResource, getProperties, removeTask);  //заполняем меню

        rowMenu.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                refreshResourceMenu(setResource, row);
            }
        });
        row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))   //если на не пустом месте кликаем,
                        .then(rowMenu)                                  //то выводим одно меню,
                        .otherwise((ContextMenu) null));                //а
        return row;
    }

    public void refreshResourceMenu(Menu setResource, TreeTableRow<ITask> row) {
        setResource.getItems().clear();
        for (IResource resource : controller.getProject().getResourceList()) {                  //берем список всех ресурсов
            CheckMenuItem checkMenuItem = new CheckMenuItem(resource.getName());                                //создаем элемент меню для каждого ресурса
            for (IResource resourceOfTask : row.getTreeItem().getValue().getResourceList()) {                   //берем список ресурсов выделенной Задачи
                if (resource.equals(resourceOfTask)) {                                                          //если ресурс из общего списка равен ресурсу из списка Задачи, то
                    checkMenuItem.selectedProperty().setValue(Boolean.TRUE);                                    //делаем этот элемент выделенным и
                    break;                                                                                      //прерываем цикл
                } else {
                    checkMenuItem.selectedProperty().setValue(Boolean.FALSE);
                }
            }
            checkMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (checkMenuItem.isSelected()) {
                        row.getTreeItem().getValue().addResource(resource);
                    } else {
                        row.getTreeItem().getValue().removeResource(resource);
                    }
                }
            });
            setResource.getItems().add(checkMenuItem);
        }
    }
}
