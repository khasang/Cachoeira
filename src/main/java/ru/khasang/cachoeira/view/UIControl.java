package ru.khasang.cachoeira.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.data.ISettingsDAO;
import ru.khasang.cachoeira.data.SettingsDAO;

import java.util.Locale;
import java.util.ResourceBundle;

public class UIControl {
    public static ResourceBundle bundle;

    private MainWindow mainWindow;
    private StartWindow startWindow;
    private NewProjectWindow newProjectWindow;
    private ISettingsDAO settingsDAO = SettingsDAO.getInstance();
    private IController controller = new Controller();

    private IntegerProperty zoomMultiplier;
    private DoubleProperty horizontalScrollValue;
    private DoubleProperty taskVerticalScrollValue;
    private DoubleProperty resourceVerticalScrollValue;
    private DoubleProperty taskHorizontalScrollValue;
    private DoubleProperty resourceHorizontalScrollValue;
    private DoubleProperty splitPaneDividerValue;

    public UIControl() {
        bundle = ResourceBundle.getBundle("locale.messages", new Locale(settingsDAO.getProgramPropertyByKey("Language")));
        zoomMultiplier = new SimpleIntegerProperty(this, "zoomMultiplier", Double.valueOf(settingsDAO.getUIValueByKey("ZoomValue")).intValue());
        horizontalScrollValue = new SimpleDoubleProperty(this, "horizontalScrollValue", 0);
        taskVerticalScrollValue = new SimpleDoubleProperty(this, "taskVerticalScrollValue", 0);
        resourceVerticalScrollValue = new SimpleDoubleProperty(this, "resourceVerticalScrollValue", 0);
        taskHorizontalScrollValue = new SimpleDoubleProperty(this, "taskHorizontalScrollValue", 0);
        resourceHorizontalScrollValue = new SimpleDoubleProperty(this, "resourceHorizontalScrollValue", 0);
        splitPaneDividerValue = new SimpleDoubleProperty(this, "splitPaneDividerValue", Double.parseDouble(settingsDAO.getUIValueByKey("DiagramDividerValue")));
    }

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

    public double getSplitPaneDividerValue() {
        return splitPaneDividerValue.get();
    }
}
