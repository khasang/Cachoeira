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
public class TaskContextMenuRowFactory implements Callback<TreeTableView<ITask>, TreeTableRow<ITask>> {
    private MainWindow mainWindow;

    public TaskContextMenuRowFactory(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public TreeTableRow<ITask> call(TreeTableView<ITask> param) {
        TreeTableRow<ITask> row = new TreeTableRow<>();
        ContextMenu rowMenu = new ContextMenu();
        Menu setResource = new Menu("Назначить ресурс");

//        refreshResourceMenu(setResource);

        MenuItem getProperties = new MenuItem("Свойства");
        MenuItem removeTask = new MenuItem("Удалить задачу");

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
                mainWindow.refreshTaskTableModel();
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
                        .otherwise((ContextMenu) null));                //а если на пустом, то другое
        return row;
    }

    public void refreshResourceMenu(Menu setResource, TreeTableRow<ITask> row) {
        setResource.getItems().clear();
        for (IResource resource : mainWindow.getController().getProject().getResourceList()) {                  //берем список всех ресурсов
            CheckMenuItem checkMenuItem = new CheckMenuItem(resource.getName());                                //создаем элемент меню для каждого ресурса
            for (IResource resourceOfTask : row.getTreeItem().getValue().getResourceList()) {   //берем список ресурсов выделенной Задачи
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
