package ru.khasang.cachoeira.view;

import javafx.stage.Stage;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;

public class UIControl {
    MainWindow mainWindow;
    TaskWindow taskWindow;
    ResourceWindow resourceWindow;
    IController controller = new Controller();

    public void launchMainWindow() {
        mainWindow = new MainWindow(controller, this);
        mainWindow.launch();
    }

    public void launchNewTaskWindow(MainWindow mainWindow) { //запускаем для добавления новой Задачи
        taskWindow = new TaskWindow(mainWindow, controller, true);
        taskWindow.launch();
    }

    public void launchPropertiesTaskWindow(MainWindow mainWindow) { //для свойств задачи
        taskWindow = new TaskWindow(mainWindow, controller, false);
        taskWindow.launch();
    }

    public void launchResourceWindow(MainWindow mainWindow) { //для добавления нового ресурса
        resourceWindow = new ResourceWindow(mainWindow, controller, true);
        resourceWindow.launch();
    }

    public void launchPropertiesResourceWindow(MainWindow mainWindow) { //для свойств ресурса
        resourceWindow = new ResourceWindow(mainWindow, controller, false);
        resourceWindow.launch();
    }

    public void refreshTableModel() {
        mainWindow.refreshTaskTableModel();
    } //временный костыль

    public Stage getMainWindowStage() {
        return mainWindow.getStage();
    }
}
