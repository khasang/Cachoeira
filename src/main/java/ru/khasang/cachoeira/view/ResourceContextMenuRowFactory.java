package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.IResource;

/**
 * Created by truesik on 25.10.2015.
 */
public class ResourceContextMenuRowFactory implements Callback<TableView<IResource>, TableRow<IResource>> {
    private final MainWindow mainWindow;

    public ResourceContextMenuRowFactory(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public TableRow<IResource> call(TableView<IResource> param) {
        TableRow<IResource> row = new TableRow<>();
        ContextMenu rowMenu = new ContextMenu();
        MenuItem setTask = new MenuItem("Назначить задачу");
        MenuItem getProperties = new MenuItem("Свойства");
        MenuItem removeResource = new MenuItem("Удалить ресурс");
        setTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //назначаем задачу
            }
        });
        getProperties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //переписать под ресурсы
//                mainWindow.getTaskController().setSelectedTask(row.getTreeItem().getValue());
//                mainWindow.openPropertiesTaskWindow();
            }
        });
        removeResource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //переписать под ресурсы
//                mainWindow.getTaskController().removeTask(row.getTreeItem().getValue());
//                mainWindow.refreshTableModel();
            }
        });
        rowMenu.getItems().addAll(setTask, getProperties, removeResource);  //заполняем меню

        row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))   //если на не пустом месте кликаем,
                        .then(rowMenu)                                  //то выводим одно меню,
                        .otherwise((ContextMenu) null));                //а если на пустом, то другое
        return row;
    }
}
