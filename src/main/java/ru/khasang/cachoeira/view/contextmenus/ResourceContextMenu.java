package ru.khasang.cachoeira.view.contextmenus;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

/**
 * Класс описывает контекстное меню всплывающее при нажатии правой кнопкой на ресурсе.
 */
public class ResourceContextMenu extends ContextMenu {
    public ResourceContextMenu() {
    }

    public ResourceContextMenu(IController controller, IResource resource) {
        initMenus(controller, resource);
    }

    public void initMenus(IController controller, IResource resource) {
        this.getItems().clear();
        Menu assignTaskMenu = new Menu("Назначить задачу");
        MenuItem getPropertiesMenuItem = new MenuItem("Свойства");
        MenuItem removeResourceMenuItem = new MenuItem("Удалить ресурс");

        getPropertiesMenuItem.setOnAction(event -> {
            controller.setSelectedResource(resource);
//                resourcePaneController.openPropertiesResourceWindow(); // TODO: 25.11.2015 исправить
        });
        removeResourceMenuItem.setOnAction(event -> controller.handleRemoveResource(resource));
        this.getItems().addAll(assignTaskMenu, getPropertiesMenuItem, removeResourceMenuItem);  //заполняем меню

        this.setOnShowing(event -> refreshTaskMenu(assignTaskMenu.getItems(), resource, controller.getProject().getTaskList()));
    }

    private void refreshTaskMenu(ObservableList<MenuItem> menuItemList,
                                 IResource currentRowResource,
                                 ObservableList<ITask> taskList) {
        menuItemList.clear();
        for (ITask task : taskList) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(task.getName());
            task.getResourceList()
                    .stream()
                    .filter(resource -> resource.equals(currentRowResource) && !checkMenuItem.isSelected())
                    .forEach(resource -> checkMenuItem.setSelected(true));
            checkMenuItem.setOnAction(event -> {
                if (checkMenuItem.isSelected()) {
                    task.addResource(currentRowResource);
                } else {
                    task.removeResource(currentRowResource);
                }
            });
            menuItemList.add(checkMenuItem);
        }
    }
}
