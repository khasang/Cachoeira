package ru.khasang.cachoeira.view;

import javafx.stage.Stage;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;

public class UIControl {
    MainWindow mainWindow;
    TaskWindow taskWindow;
    ResourceWindow resourceWindow;
    StartWindow startWindow;
    NewProjectWindow newProjectWindow;
    IController controller = new Controller();

    public void launchStartWindow() {
        startWindow = new StartWindow(controller, this);
        startWindow.launch();
    }

    public void launchNewProjectWindow() {
        newProjectWindow = new NewProjectWindow(controller, this);
        newProjectWindow.launch();
    }

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

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public TaskWindow getTaskWindow() {
        return taskWindow;
    }

    public ResourceWindow getResourceWindow() {
        return resourceWindow;
    }

    public StartWindow getStartWindow() {
        return startWindow;
    }

    public NewProjectWindow getNewProjectWindow() {
        return newProjectWindow;
    }
}
