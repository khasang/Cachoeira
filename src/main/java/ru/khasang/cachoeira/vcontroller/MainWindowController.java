package ru.khasang.cachoeira.vcontroller;

import javafx.beans.property.*;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.view.mainwindow.MainWindowView;

public class MainWindowController {
    private IntegerProperty zoomMultiplier = new SimpleIntegerProperty(this, "zoomMultiplier", 70);
    private DoubleProperty ganttHorizontalScrollValue = new SimpleDoubleProperty(this, "ganttHorizontalScrollValue", 0);
    private DoubleProperty taskVerticalScrollValue = new SimpleDoubleProperty(this, "taskVerticalScrollValue", 0);
    private DoubleProperty resourceVerticalScrollValue = new SimpleDoubleProperty(this, "resourceVerticalScrollValue", 0);
    private DoubleProperty taskHorizontalScrollValue = new SimpleDoubleProperty(this, "taskHorizontalScrollValue", 0);
    private DoubleProperty resourceHorizontalScrollValue = new SimpleDoubleProperty(this, "resourceHorizontalScrollValue", 0);
    private DoubleProperty splitPaneDividerValue = new SimpleDoubleProperty(this, "splitPaneDividerValue", 0.3);

    private DoubleProperty widthOfWindow = new SimpleDoubleProperty(this, "widthOfWindow", 1280);
    private DoubleProperty heightOfWindow = new SimpleDoubleProperty(this, "heightOfWindow", 720);
    private BooleanProperty isMaximized = new SimpleBooleanProperty(this, "isMaximized", false);

    private final IProject project;
    private final MainWindowView mainWindowView;

    public MainWindowController(IProject project) {
        this.project = project;
        mainWindowView = new MainWindowView(this, project);
    }

    public void launch() {
        mainWindowView.createView();
    }

    public int getZoomMultiplier() {
        return zoomMultiplier.get();
    }

    public IntegerProperty zoomMultiplierProperty() {
        return zoomMultiplier;
    }

    public double getGanttHorizontalScrollValue() {
        return ganttHorizontalScrollValue.get();
    }

    public DoubleProperty ganttHorizontalScrollValueProperty() {
        return ganttHorizontalScrollValue;
    }

    public double getTaskVerticalScrollValue() {
        return taskVerticalScrollValue.get();
    }

    public DoubleProperty taskVerticalScrollValueProperty() {
        return taskVerticalScrollValue;
    }

    public double getResourceVerticalScrollValue() {
        return resourceVerticalScrollValue.get();
    }

    public DoubleProperty resourceVerticalScrollValueProperty() {
        return resourceVerticalScrollValue;
    }

    public double getTaskHorizontalScrollValue() {
        return taskHorizontalScrollValue.get();
    }

    public DoubleProperty taskHorizontalScrollValueProperty() {
        return taskHorizontalScrollValue;
    }

    public double getResourceHorizontalScrollValue() {
        return resourceHorizontalScrollValue.get();
    }

    public DoubleProperty resourceHorizontalScrollValueProperty() {
        return resourceHorizontalScrollValue;
    }

    public double getSplitPaneDividerValue() {
        return splitPaneDividerValue.get();
    }

    public DoubleProperty splitPaneDividerValueProperty() {
        return splitPaneDividerValue;
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
}
