package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.Group;
import javafx.scene.shape.Line;

/**
 * Created by Вячеслав on 12.02.2016.
 */
public class TaskPaneRelationLine extends Group {
    public TaskPaneRelationLine() {
        Line line1 = new Line();
        Line line2 = new Line();

        this.getChildren().addAll(line1, line2);
    }

}
