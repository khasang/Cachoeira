package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

public class TaskPaneBackgroundTaskBar extends Rectangle {
    private final TaskPaneTaskBar taskBar;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<ITask> selectedTaskListener;

    public TaskPaneBackgroundTaskBar(TaskPaneTaskBar taskBar,
                                     TaskPaneLabel taskLabel,
                                     UIControl uiControl) {
        this.taskBar = taskBar;

        this.setArcHeight(10);
        this.setArcWidth(10);

        this.layoutXProperty().bind(taskBar.layoutXProperty().subtract(10));
        this.widthProperty().bind(taskBar.widthProperty().add(20).add(taskLabel.widthProperty()));
        this.layoutYProperty().bind(taskBar.layoutYProperty());
        this.heightProperty().bind(taskBar.heightProperty());

        this.setFill(Color.TRANSPARENT);

        setListener(uiControl);
    }

    public void setListener(UIControl uiControl) {
        selectedTaskListener = (observable, oldValue, newValue) -> {
            if (newValue != null && taskBar.getTask().equals(newValue)) {
                this.setStroke(Color.valueOf("#03A9F4"));
                this.setFill(Color.valueOf("#e4f6ff"));
            }
            if (newValue == null || !taskBar.getTask().equals(newValue)) {
                this.setStroke(Color.TRANSPARENT);
                this.setFill(Color.TRANSPARENT);
            }
        };
        uiControl.getController().selectedTaskProperty().addListener(selectedTaskListener);
    }

    public TaskPaneTaskBar getTaskBar() {
        return taskBar;
    }
}
