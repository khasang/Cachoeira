package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.backgroundtaskbar;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;

public abstract class BackgroundTaskBar extends Rectangle {
    private final TaskBar taskBar;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<ITask>> selectedTaskListener;
    private MainWindowController controller;

    public BackgroundTaskBar(TaskBar taskBar,
                             TaskBarLabel taskLabel,
                             MainWindowController controller) {
        this.taskBar = taskBar;
        this.controller = controller;

        this.setArcHeight(10);
        this.setArcWidth(10);

        this.layoutXProperty().bind(taskBar.layoutXProperty().subtract(10));
        this.widthProperty().bind(taskBar.widthProperty().add(20).add(taskLabel.widthProperty()));
        this.layoutYProperty().bind(taskBar.layoutYProperty());
        this.heightProperty().bind(taskBar.heightProperty());

        this.setSelected(true);
        this.setListener();
    }

    public void setListener() {
        selectedTaskListener = (observable, oldValue, newValue) -> {
            if (newValue != null && taskBar.getTask().equals(newValue.getValue())) {
                setSelected(true);
            }
            if (newValue == null || !taskBar.getTask().equals(newValue.getValue())) {
                setSelected(false);
            }
        };
        controller.getView().getTaskTableView().getSelectionModel().selectedItemProperty().addListener(selectedTaskListener);
    }

    public void setSelected(boolean enabled) {
        if (enabled) {
            this.setStroke(Color.valueOf("#03A9F4"));
            this.setFill(Color.valueOf("#e4f6ff"));
        } else {
            this.setStroke(Color.TRANSPARENT);
            this.setFill(Color.TRANSPARENT);
        }
    }

    public TaskBar getTaskBar() {
        return taskBar;
    }
}
