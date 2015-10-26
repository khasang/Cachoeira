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

    public void launchPropertiesTaskWindow(MainWindow mainWindow) {
        taskWindow = new TaskWindow(mainWindow, controller, false);
        taskWindow.launch();
    }

    public void launchResourceWindow() {
        resourceWindow = new ResourceWindow();
        resourceWindow.launch();
    }

    public void refreshTableModel() {
        mainWindow.refreshTableModel();
    } //временный костыль

    public Stage getMainWindowStage() {
        return mainWindow.getStage();
    }
}
