package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.khasang.cachoeira.model.IResource;

public class TaskPaneLabel extends HBox {
    private TaskPaneTaskBar taskBar;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IResource> resourceListChangeListener;

    public TaskPaneLabel(TaskPaneTaskBar taskBar) {
        this.taskBar = taskBar;
        this.layoutXProperty().bind(taskBar.layoutXProperty().add(taskBar.widthProperty().add(10)));
        this.layoutYProperty().bind(taskBar.layoutYProperty().add(6.5));
        this.setHeight(taskBar.getHeight());
        setListeners();
    }

    public void addResourceLabel(IResource resource) {
        Label resourceLabel = new Label(resource.getName() + "\t");
        this.getChildren().add(resourceLabel);
    }

    public void removeResourceLabel(IResource resource) {
        this.getChildren().removeIf(node -> {
            Label resourceLabel = (Label) node;
            return resourceLabel.getText().equals(resource.getName());
        });
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

    public TaskPaneTaskBar getTaskBar() {
        return taskBar;
    }
}
