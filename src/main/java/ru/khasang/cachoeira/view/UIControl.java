package ru.khasang.cachoeira.view;

import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;

public class UIControl {
    MainWindow mainWindow;
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

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public StartWindow getStartWindow() {
        return startWindow;
    }

    public NewProjectWindow getNewProjectWindow() {
        return newProjectWindow;
    }
}
