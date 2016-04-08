package ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer.backgroundtaskbar;

import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.TaskBar;

public abstract class BackgroundTaskBar extends Rectangle {
    private final TaskBar taskBar;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<ITask> selectedTaskListener;

    public BackgroundTaskBar(TaskBar taskBar,
                             TaskBarLabel taskLabel,
                             UIControl uiControl) {
        this.taskBar = taskBar;

        this.setArcHeight(10);
        this.setArcWidth(10);

        this.layoutXProperty().bind(taskBar.layoutXProperty().subtract(10));
        this.widthProperty().bind(taskBar.widthProperty().add(20).add(taskLabel.widthProperty()));
        this.layoutYProperty().bind(taskBar.layoutYProperty());
        this.heightProperty().bind(taskBar.heightProperty());

        setSelected(true);
        setListener(uiControl);
    }

    public void setListener(UIControl uiControl) {
        selectedTaskListener = (observable, oldValue, newValue) -> {
            if (newValue != null && taskBar.getTask().equals(newValue)) {
                setSelected(true);
            }
            if (newValue == null || !taskBar.getTask().equals(newValue)) {
                setSelected(false);
            }
        };
        uiControl.getController().selectedTaskProperty().addListener(selectedTaskListener);
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
