package ru.khasang.cachoeira.controller;

import javafx.stage.Stage;
import ru.khasang.cachoeira.view.MainWindow;
import ru.khasang.cachoeira.view.ResourceWindow;
import ru.khasang.cachoeira.view.TaskWindow;

public class ViewController {
    MainWindow mainWindow;
    TaskWindow taskWindow;
    ResourceWindow resourceWindow;
    TaskController taskController = new TaskController();

    public void launchMainWindow() {
        mainWindow = new MainWindow(taskController, this);
        mainWindow.launch();
    }

    public void launchNewTaskWindow(MainWindow mainWindow) { //запускаем для добавления новой Задачи
        taskWindow = new TaskWindow(mainWindow, taskController, true);
        taskWindow.launch();
    }

    public void launchPropertiesTaskWindow(MainWindow mainWindow) {
        taskWindow = new TaskWindow(mainWindow, taskController, false);
        taskWindow.launch();
    }

    public void launchResourceWindow() {
        resourceWindow = new ResourceWindow();
        resourceWindow.launch();
    }

    public void refreshTableModel() {
        mainWindow.refreshTableModel();
    }

    public Stage getMainWindowStage() {
        return mainWindow.getStage();
    }
}
