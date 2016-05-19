package ru.khasang.cachoeira.vcontroller.contextmenus;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ru.khasang.cachoeira.commands.project.RemoveTaskFromProjectCommand;
import ru.khasang.cachoeira.commands.task.*;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.vcontroller.MainWindowController;

/**
 * Класс описывает контекстное меню всплывающее при нажатии правой кнопкой на задаче.
 */
public class TaskContextMenu extends ContextMenu {
    private final IProject project;
    private final ITask task;
    private final MainWindowController controller;

    public TaskContextMenu(IProject project, ITask task, MainWindowController controller) {
        this.project = project;
        this.task = task;
        this.controller = controller;
    }

    public void initMenus() {
        this.getItems().clear();
        Menu assignResourceMenu = new Menu("Assign Resource");
        Menu assignDependencyTask = new Menu("Assign Predecessor");
        MenuItem removeTaskMenuItem = new MenuItem("Remove Task");

        removeTaskMenuItem.setOnAction(event -> controller.getCommandExecutor().execute(new RemoveTaskFromProjectCommand(project, task)));
        this.getItems().addAll(assignResourceMenu, assignDependencyTask, removeTaskMenuItem);  //заполняем меню

        this.setOnShowing(event -> {
            refreshResourceMenu(assignResourceMenu.getItems(), task, project.getResourceList());
            refreshDependencyTaskMenu(assignDependencyTask.getItems(), task, project.getTaskList());
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
                            controller.getCommandExecutor().execute(new AddParentTaskToTaskCommand(task, new DependentTask(parentTask, TaskDependencyType.FINISHSTART)));
                            controller.getCommandExecutor().execute(new AddChildTaskToTaskCommand(parentTask, new DependentTask(task, null)));
                        } else {
                            task.getParentTasks().stream()
                                    .filter(dependentTask -> dependentTask.getTask().equals(parentTask))
                                    .findFirst()
                                    .ifPresent(dependentTask -> controller.getCommandExecutor().execute(new RemoveParentTaskFromTaskCommand(task, dependentTask)));
                            parentTask.getChildTasks().stream()
                                    .filter(dependentTask -> dependentTask.getTask().equals(task))
                                    .findFirst()
                                    .ifPresent(dependentTask -> controller.getCommandExecutor().execute(new RemoveChildTaskFromTaskCommand(parentTask, dependentTask)));
                        }
                    });
                    menuItemsList.add(checkMenuItem);
                });
        // Отключаем те задачи, которые уже находятся в списке последователей (childTasks),
        // чтобы нельзя было сделать закольцованность (например Задача 2 начинается после
        // Задачи 1, а Задача 1, после Задачи 2)
        setDisableSelectionChildTasks(task, menuItemsList);
    }

    /**
     * Рекурсивный метод который отключает возможность сделать предшественником задачу, если она уже находится
     * в списке наследников (или в списках наследников наследников).
     * Например, есть последовательность Задача 1 -> Задача 2 -> Задача 3.
     * Нажав правой кнопкой на Задаче 1 мы не сможем сделать предшественником ни Задачу 2, ни Задачу 3 (до тех пор пока
     * не уберем предшественником Задачу 1 у Задачи 2).
     *
     * @param task      Задача на которую нажали правой кнопкой.
     * @param menuItems Список меню с задачами.
     */
    private void setDisableSelectionChildTasks(ITask task, ObservableList<MenuItem> menuItems) {
        if (!task.getChildTasks().isEmpty()) {
            task.getChildTasks()
                    .stream()
                    .forEach(childTask -> {
                        setDisableSelectionChildTasks(childTask.getTask(), menuItems);
                        menuItems
                                .stream()
                                .filter(menuItem -> menuItem.getText().equals(childTask.getTask().getName()))
                                .forEach(menuItem -> menuItem.setDisable(true));
                    });
        }
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
                    controller.getCommandExecutor().execute(new AddResourceToTaskCommand(task, resource));
                } else {
                    controller.getCommandExecutor().execute(new RemoveResourceFromTaskCommand(task, resource));
                }
            });
            menuItemList.add(checkMenuItem);
        }
    }
}