package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

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
        Menu setTask = new Menu("Назначить задачу");

//        refreshTaskMenu(setTask);

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
