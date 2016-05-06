package ru.khasang.cachoeira.viewcontroller.mainwindow.ganttplan.labelslayer.label;

import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.viewcontroller.mainwindow.ganttplan.objectslayer.taskbar.TaskBar;

public abstract class TaskBarLabel extends HBox {
    private TaskBar taskBar;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IResource> resourceListChangeListener;

    public TaskBarLabel(TaskBar taskBar) {
        this.taskBar = taskBar;
        this.layoutXProperty().bind(taskBar.layoutXProperty().add(taskBar.widthProperty().add(12)));
        this.layoutYProperty().bind(taskBar.layoutYProperty().add(6));
        this.setHeight(taskBar.getHeight());
        setListeners();
    }

    public void addResourceLabel(IResource resource) {
        Label resourceLabel = new Label(resource.getName() + "\t");
        this.getChildren().add(resourceLabel);
    }

    public void removeResourceLabel(IResource resource) {
        this.getChildren().removeIf(node -> {
            javafx.scene.control.Label resourceLabel = (javafx.scene.control.Label) node;
            return resourceLabel.getText().equals(resource.getName());
        });
    }

    public void refreshResourceLabels() {
        this.getChildren().clear();
        taskBar.getTask().getResourceList().forEach(this::addResourceLabel);
    }

    public void setListeners() {
        resourceListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::addResourceLabel);
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(this::removeResourceLabel);
                }
            }
        };
        taskBar.getTask().getResourceList().addListener(new WeakListChangeListener<>(resourceListChangeListener));
    }

    public TaskBar getTaskBar() {
        return taskBar;
    }
}
