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
 * Класс описывает контекстное меню всплывающее при нажатии правой кнопкой на задаче.
 */
public class TaskContextMenu extends ContextMenu {
    public TaskContextMenu() {
    }

    public TaskContextMenu(IController controller, ITask task) {
        initMenus(controller, task);
    }

    public void initMenus(IController controller, ITask task) {
        this.getItems().clear();
        Menu assignTaskMenu = new Menu("Назначить ресурс");
        MenuItem getPropertiesMenuItem = new MenuItem("Свойства");
        MenuItem removeTaskMenuItem = new MenuItem("Удалить задачу");

        getPropertiesMenuItem.setOnAction(event -> {
            controller.setSelectedTask(task);
//                taskPaneController.openPropertiesTaskWindow(); // TODO: 25.11.2015 исправить
        });
        removeTaskMenuItem.setOnAction(event -> controller.handleRemoveTask(task));
        this.getItems().addAll(assignTaskMenu, getPropertiesMenuItem, removeTaskMenuItem);  //заполняем меню

        this.setOnShowing(event -> refreshResourceMenu(assignTaskMenu.getItems(), task, controller.getProject().getResourceList()));
    }

    private void refreshResourceMenu(ObservableList<MenuItem> menuItemList,
                                     ITask task,
                                     ObservableList<IResource> resourceList) {
        menuItemList.clear();
        for (IResource resource : resourceList) {                                                                  //берем список всех ресурсов
            CheckMenuItem checkMenuItem = new CheckMenuItem(resource.getName());                                //создаем элемент меню для каждого ресурса
            for (IResource resourceOfTask : task.getResourceList()) {                   //берем список ресурсов выделенной Задачи
                if (resource.equals(resourceOfTask)) {                                                          //если ресурс из общего списка равен ресурсу из списка Задачи, то
                    checkMenuItem.selectedProperty().setValue(Boolean.TRUE);                                    //делаем этот элемент выделенным и
                    break;                                                                                      //прерываем цикл
                } else {
                    checkMenuItem.selectedProperty().setValue(Boolean.FALSE);
                }
            }
            checkMenuItem.setOnAction(event -> {
                if (checkMenuItem.isSelected()) {
                    task.addResource(resource);
                } else {
                    task.removeResource(resource);
                }
            });
            menuItemList.add(checkMenuItem);
        }
    }
}