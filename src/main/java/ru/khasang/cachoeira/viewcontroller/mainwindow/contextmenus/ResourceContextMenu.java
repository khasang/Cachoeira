package ru.khasang.cachoeira.viewcontroller.mainwindow.contextmenus;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.project.RemoveResourceFromProjectCommand;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.UIControl;

import java.util.ResourceBundle;

/**
 * Класс описывает контекстное меню всплывающее при нажатии правой кнопкой на ресурсе.
 */
public class ResourceContextMenu extends ContextMenu {
    private ResourceBundle bundle = UIControl.bundle;

    public ResourceContextMenu() {
    }

    public ResourceContextMenu(IController controller, IResource resource) {
        initMenus(controller, resource);
    }

    public void initMenus(IController controller, IResource resource) {
        this.getItems().clear();
        Menu assignTaskMenu = new Menu(bundle.getString("assign_task"));
        MenuItem removeResourceMenuItem = new MenuItem(bundle.getString("remove_resource"));

        removeResourceMenuItem.setOnAction(event -> CommandControl.getInstance().execute(new RemoveResourceFromProjectCommand(controller.getProject(), resource)));
        this.getItems().addAll(assignTaskMenu, removeResourceMenuItem);  //заполняем меню

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
                    CommandControl.getInstance().execute(new AddResourceToTaskCommand(task, currentRowResource));
                } else {
                    CommandControl.getInstance().execute(new RemoveResourceFromTaskCommand(task, currentRowResource));
                }
            });
            menuItemList.add(checkMenuItem);
        }
    }
}
