package ru.khasang.cachoeira.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;

public class UIControl {
    private MainWindow mainWindow;
    private StartWindow startWindow;
    private NewProjectWindow newProjectWindow;
    private IController controller = new Controller();
    private IntegerProperty zoomMultiplier = new SimpleIntegerProperty(this, "zoomMultiplier", 70);
    private DoubleProperty horizontalScroll = new SimpleDoubleProperty(this, "horizontalScroll", 0);

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

    public IController getController() {
        return controller;
    }

    public int getZoomMultiplier() {
        return zoomMultiplier.get();
    }

    public IntegerProperty zoomMultiplierProperty() {
        return zoomMultiplier;
    }

    public void setZoomMultiplier(int zoomMultiplier) {
        this.zoomMultiplier.set(zoomMultiplier);
    }

    public double getHorizontalScroll() {
        return horizontalScroll.get();
    }

    public DoubleProperty horizontalScrollProperty() {
        return horizontalScroll;
    }

    public void setHorizontalScroll(double horizontalScroll) {
        this.horizontalScroll.set(horizontalScroll);
    }
}
