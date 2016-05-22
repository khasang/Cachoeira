package ru.khasang.cachoeira.viewcontroller.contextmenus;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ru.khasang.cachoeira.commands.project.RemoveResourceFromProjectCommand;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

/**
 * Класс описывает контекстное меню всплывающее при нажатии правой кнопкой на ресурсе.
 */
public class ResourceContextMenu extends ContextMenu {
    private final IProject project;
    private final IResource resource;
    private final MainWindowController controller;

    public ResourceContextMenu(IProject project, IResource resource, MainWindowController controller) {
        this.project = project;
        this.resource = resource;
        this.controller = controller;
    }

    public void initMenus() {
        this.getItems().clear();
        Menu assignTaskMenu = new Menu("Assign Task");
        MenuItem removeResourceMenuItem = new MenuItem("Remove Resource");

        removeResourceMenuItem.setOnAction(event -> controller.getCommandExecutor().execute(
                new RemoveResourceFromProjectCommand(project, resource)));
        this.getItems().addAll(assignTaskMenu, removeResourceMenuItem);  //заполняем меню

        this.setOnShowing(event -> refreshTaskMenu(assignTaskMenu.getItems(), resource, project.getTaskList()));
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
                    controller.getCommandExecutor().execute(new AddResourceToTaskCommand(task, currentRowResource));
                } else {
                    controller.getCommandExecutor().execute(new RemoveResourceFromTaskCommand(task, currentRowResource));
                }
            });
            menuItemList.add(checkMenuItem);
        }
    }
}
