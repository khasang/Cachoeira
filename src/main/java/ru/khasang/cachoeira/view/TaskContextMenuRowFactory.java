package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.ITask;

/**
 * Created by truesik on 25.10.2015.
 */
public class TaskContextMenuRowFactory implements Callback<TreeTableView<ITask>, TreeTableRow<ITask>> {
    private MainWindow mainWindow;

    public TaskContextMenuRowFactory(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public TreeTableRow<ITask> call(TreeTableView<ITask> param) {
        TreeTableRow<ITask> row = new TreeTableRow<>();
        ContextMenu rowMenu = new ContextMenu();
        MenuItem setResource = new MenuItem("Назначить ресурс");
        MenuItem getProperties = new MenuItem("Свойства");
        MenuItem removeTask = new MenuItem("Удалить задачу");
        setResource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //назначаем ресурс
            }
        });
        getProperties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainWindow.getController().setSelectedTask(row.getTreeItem().getValue());
                mainWindow.openPropertiesTaskWindow();
            }
        });
        removeTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainWindow.getController().handleRemoveTask(row.getTreeItem().getValue());
                mainWindow.refreshTableModel();
            }
        });
        rowMenu.getItems().addAll(setResource, getProperties, removeTask);  //заполняем меню

        row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))   //если на не пустом месте кликаем,
                        .then(rowMenu)                                  //то выводим одно меню,
                        .otherwise((ContextMenu) null));                //а если на пустом, то другое
        return row;
    }
}
