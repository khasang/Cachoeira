package ru.khasang.cachoeira.view.contextmenus;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.DependentTask;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.TaskDependencyType;

/**
 * Created by Вячеслав on 11.02.2016.
 */
public class ParentTaskContextMenu extends ContextMenu {
    public ParentTaskContextMenu() {
    }

    public void initMenus(IController controller, ITask task) {
        this.getItems().clear();
        Menu assignDependencyTask = new Menu("Назначить предшественника");

        this.getItems().addAll(assignDependencyTask);  //заполняем меню

        this.setOnShowing(event -> refreshDependencyTaskMenu(
                assignDependencyTask.getItems(),
                task,
                controller.getProject().getTaskList()));
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
}
