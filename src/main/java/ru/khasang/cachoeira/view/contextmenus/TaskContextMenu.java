package ru.khasang.cachoeira.view.contextmenus;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.*;

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
        Menu assignResourceMenu = new Menu("Назначить ресурс");
        Menu assignDependencyTask = new Menu("Назначить предшественника");
        MenuItem removeTaskMenuItem = new MenuItem("Удалить задачу");

        removeTaskMenuItem.setOnAction(event -> controller.handleRemoveTask(task));
        this.getItems().addAll(assignResourceMenu, assignDependencyTask, removeTaskMenuItem);  //заполняем меню

        this.setOnShowing(event -> {
            refreshResourceMenu(assignResourceMenu.getItems(), task, controller.getProject().getResourceList());
            refreshDependencyTaskMenu(assignDependencyTask.getItems(), task, controller.getProject().getTaskList());
        });
    }

    private void refreshDependencyTaskMenu(ObservableList<MenuItem> menuItemsList,
                                           ITask task,
                                           ObservableList<ITask> taskList) {
        menuItemsList.clear();
        taskList.stream()
                .filter(parentTask -> !parentTask.equals(task)) // Убираем возможность присвоить предшественником саму себя
                .forEach(parentTask -> {
                    CheckMenuItem checkMenuItem = new CheckMenuItem(parentTask.getName());
                    // Расставляем галочки на нужных пунктах
                    task.getParentTasks()
                            .stream()
                            .filter(dependentTask -> parentTask.equals(dependentTask.getTask()) && !checkMenuItem.isSelected())
                            .forEach(dependentTask -> checkMenuItem.setSelected(true));
                    // Вешаем ивент при нажатии на каком-либо пункте меню
                    checkMenuItem.setOnAction(event -> {
                        if (checkMenuItem.isSelected()) {
                            IDependentTask parentDependentTask = new DependentTask();
                            parentDependentTask.setTask(parentTask);
                            parentDependentTask.setDependenceType(TaskDependencyType.FINISHSTART);
                            task.addParentTask(parentDependentTask);

                            IDependentTask childTask = new DependentTask();
                            childTask.setTask(task);
                            parentTask.addChildTask(childTask);
                        } else {
                            task.getParentTasks().removeIf(parentDependentTask -> parentDependentTask.getTask().equals(parentTask));
                            parentTask.getChildTasks().removeIf(childTask -> childTask.getTask().equals(task));
                        }
                    });
                    menuItemsList.add(checkMenuItem);
                    // Отключаем те задачи, которые уже находятся в списке последователей (childTasks),
                    // чтобы нельзя было сделать закольцованность (например Задача 2 начинается после
                    // Задачи 1, а Задача 1, после Задачи 2)
                    task.getChildTasks()
                            .stream()
                            .filter(dependentTask -> dependentTask.getTask().equals(parentTask))
                            .forEach(dependentTask -> checkMenuItem.setDisable(true));
                });
    }

    private void refreshResourceMenu(ObservableList<MenuItem> menuItemList,
                                     ITask task,
                                     ObservableList<IResource> resourceList) {
        menuItemList.clear();
        for (IResource resource : resourceList) {                                                                  //берем список всех ресурсов
            CheckMenuItem checkMenuItem = new CheckMenuItem(resource.getName());                                //создаем элемент меню для каждого ресурса
            task.getResourceList()
                    .stream()
                    .filter(resourceOfTask -> resource.equals(resourceOfTask) && !checkMenuItem.isSelected())
                    .forEach(resourceOfTask -> checkMenuItem.setSelected(true));
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