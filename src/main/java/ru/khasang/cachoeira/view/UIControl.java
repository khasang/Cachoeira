package ru.khasang.cachoeira.view;

import javafx.beans.property.*;
import ru.khasang.cachoeira.controller.Controller;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.data.ISettingsManager;
import ru.khasang.cachoeira.data.SettingsManager;
import ru.khasang.cachoeira.view.mainwindow.MainWindow;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class UIControl {
    private MainWindow mainWindow;

    private StartWindow startWindow;
    private NewProjectWindow newProjectWindow;
    private ISettingsManager settingsManager = SettingsManager.getInstance();
    private IController controller = new Controller();

    // Присваиваем значения по умолчанию для тех случаев, если добавилось еще одно поле которое нужно считать из
    // файла с настройками, но файл уже создан (чтобы не было NullPointerException).
    public static ResourceBundle bundle = ResourceBundle.getBundle("locale.messages", new Locale("EN"));

    private IntegerProperty zoomMultiplier = new SimpleIntegerProperty(this, "zoomMultiplier", 70);
    private DoubleProperty horizontalScrollValue = new SimpleDoubleProperty(this, "horizontalScrollValue", 0);
    private DoubleProperty taskVerticalScrollValue = new SimpleDoubleProperty(this, "taskVerticalScrollValue", 0);
    private DoubleProperty resourceVerticalScrollValue = new SimpleDoubleProperty(this, "resourceVerticalScrollValue", 0);
    private DoubleProperty taskHorizontalScrollValue = new SimpleDoubleProperty(this, "taskHorizontalScrollValue", 0);
    private DoubleProperty resourceHorizontalScrollValue = new SimpleDoubleProperty(this, "resourceHorizontalScrollValue", 0);
    private DoubleProperty splitPaneDividerValue = new SimpleDoubleProperty(this, "splitPaneDividerValue", 0.3);
    private DoubleProperty widthOfWindow = new SimpleDoubleProperty(this, "widthOfWindow", 1280);
    private DoubleProperty heightOfWindow = new SimpleDoubleProperty(this, "heightOfWindow", 720);
    private BooleanProperty isMaximized = new SimpleBooleanProperty(this, "isMaximized", false);

    private File file;

    public UIControl() {
        if (settingsManager.getProgramPropertyByKey("Language") != null) {
            bundle = ResourceBundle.getBundle("locale.messages", new Locale(settingsManager.getProgramPropertyByKey("Language")));
        }
        if (settingsManager.getUIValueByKey("ZoomValue") != null) {
            zoomMultiplier = new SimpleIntegerProperty(this, "zoomMultiplier", Double.valueOf(settingsManager.getUIValueByKey("ZoomValue")).intValue());
        }
        if (settingsManager.getUIValueByKey("DiagramDividerValue") != null) {
            splitPaneDividerValue = new SimpleDoubleProperty(this, "splitPaneDividerValue", Double.parseDouble(settingsManager.getUIValueByKey("DiagramDividerValue")));
        }
        if (settingsManager.getUIValueByKey("WidthOfWindow") != null) {
            widthOfWindow = new SimpleDoubleProperty(this, "widthOfWindow", Double.parseDouble(settingsManager.getUIValueByKey("WidthOfWindow")));
        }
        if (settingsManager.getUIValueByKey("HeightOfWindow") != null) {
            heightOfWindow = new SimpleDoubleProperty(this, "heightOfWindow", Double.parseDouble(settingsManager.getUIValueByKey("HeightOfWindow")));
        }
        isMaximized = new SimpleBooleanProperty(this, "isMaximized", isMaximized());
    }

    private boolean isMaximized() {
        return settingsManager.getUIValueByKey("MaximizedWindow") != null && settingsManager.getUIValueByKey("MaximizedWindow").equals("1");
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

    public double getWidthOfWindow() {
        return widthOfWindow.get();
    }

    public DoubleProperty widthOfWindowProperty() {
        return widthOfWindow;
    }

    public double getHeightOfWindow() {
        return heightOfWindow.get();
    }

    public DoubleProperty heightOfWindowProperty() {
        return heightOfWindow;
    }

    public boolean getIsMaximized() {
        return isMaximized.get();
    }

    public BooleanProperty isMaximizedProperty() {
        return isMaximized;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
