package ru.khasang.cachoeira.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;

import java.util.Locale;
import java.util.ResourceBundle;

public class UIControl {
    public final static ResourceBundle BUNDLE = ResourceBundle.getBundle("locale.messages", Locale.ENGLISH);

    private MainWindow mainWindow;
    private StartWindow startWindow;
    private NewProjectWindow newProjectWindow;
    private IController controller = new Controller();

    private IntegerProperty zoomMultiplier = new SimpleIntegerProperty(this, "zoomMultiplier", 70);
    private DoubleProperty horizontalScrollValue = new SimpleDoubleProperty(this, "horizontalScrollValue", 0);
    private DoubleProperty taskVerticalScrollValue = new SimpleDoubleProperty(this, "taskVerticalScrollValue", 0);
    private DoubleProperty resourceVerticalScrollValue = new SimpleDoubleProperty(this, "resourceVerticalScrollValue", 0);
    private DoubleProperty taskHorizontalScrollValue = new SimpleDoubleProperty(this, "taskHorizontalScrollValue", 0);
    private DoubleProperty resourceHorizontalScrollValue = new SimpleDoubleProperty(this, "resourceHorizontalScrollValue", 0);
    private DoubleProperty splitPaneDividerValue = new SimpleDoubleProperty(this, "splitPaneDividerValue", 0.3);

    public void launchStartWindow() {
        startWindow = new StartWindow(this);
        startWindow.launch();
    }

    public void launchNewProjectWindow() {
        newProjectWindow = new NewProjectWindow(this);
        newProjectWindow.launch();
    }

    public void launchMainWindow() {
        mainWindow = new MainWindow(this);
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

    public DoubleProperty horizontalScrollValueProperty() {
        return horizontalScrollValue;
    }

    public DoubleProperty taskVerticalScrollValueProperty() {
        return taskVerticalScrollValue;
    }

    public DoubleProperty resourceVerticalScrollValueProperty() {
        return resourceVerticalScrollValue;
    }

    public DoubleProperty taskHorizontalScrollValueProperty() {
        return taskHorizontalScrollValue;
    }

    public DoubleProperty resourceHorizontalScrollValueProperty() {
        return resourceHorizontalScrollValue;
    }

    public DoubleProperty splitPaneDividerValueProperty() {
        return splitPaneDividerValue;
    }
}
