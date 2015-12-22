package ru.khasang.cachoeira.view.contextmenus;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

/**
 * Created by truesik on 22.12.2015.
 */
public class TaskContextMenu extends ContextMenu {
    public TaskContextMenu() {
    }

    public TaskContextMenu(IController controller, ITask task) {
        initMenus(controller, task);
    }

    public void initMenus(IController controller, ITask task) {
        this.getItems().clear();
        Menu setResource = new Menu("Назначить ресурс");
        MenuItem getProperties = new MenuItem("Свойства");
        MenuItem removeTask = new MenuItem("Удалить задачу");

        getProperties.setOnAction(event -> {
            controller.setSelectedTask(task);
//                taskPaneController.openPropertiesTaskWindow(); // TODO: 25.11.2015 исправить
        });
        removeTask.setOnAction(event -> controller.handleRemoveTask(task));
        this.getItems().addAll(setResource, getProperties, removeTask);  //заполняем меню

        this.setOnShowing(event -> refreshResourceMenu(setResource, task, controller.getProject().getResourceList()));
    }

    private void refreshResourceMenu(Menu setResource, ITask task, ObservableList<IResource> resources) {
        setResource.getItems().clear();
        for (IResource resource : resources) {                                                                  //берем список всех ресурсов
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
            setResource.getItems().add(checkMenuItem);
        }
    }
}