package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public class ObjectsChartLayer extends Pane implements AbstractChartLayer {
    public ObjectsChartLayer() {
    }

    @Override
    public void createLayer() {

    }

    /**
     * Adds task bar to chart.
     *
     * @param taskBar Task bar.
     */
    public void addTaskBar(TaskBar taskBar) {
        this.getChildren().add(taskBar);
    }

    /**
     * Removes task bar from chart.
     *
     * @param task Task.
     */
    public void removeTaskBar(ITask task) {
        for (Node node : this.getChildren()) {
            TaskBar bar = (TaskBar) node;
            if (bar.getTask().equals(task)) {
                this.getChildren().remove(node);
                break;
            }
        }
    }
}
