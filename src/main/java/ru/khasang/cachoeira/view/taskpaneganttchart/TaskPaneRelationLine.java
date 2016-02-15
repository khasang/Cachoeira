package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TaskPaneRelationLine extends Group {
    public TaskPaneRelationLine(TaskPaneTaskBar parentTaskBar,
                                TaskPaneTaskBar childTaskBar) {

        BoundLine firstLine = new BoundLine(
                parentTaskBar.layoutXProperty().add(parentTaskBar.widthProperty()),
                parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2)),
                childTaskBar.layoutXProperty(),
                parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2))
        );
        BoundLine secondLine = new BoundLine(
                childTaskBar.layoutXProperty().add(0),
                parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2)),
                childTaskBar.layoutXProperty(),
                childTaskBar.layoutYProperty().add(0)
        );

        this.getChildren().addAll(firstLine, secondLine);
    }

    class BoundLine extends Line {
        BoundLine(DoubleBinding startX, DoubleBinding startY, DoubleProperty endX, DoubleBinding endY) {
            startXProperty().bind(startX);
            startYProperty().bind(startY);
            endXProperty().bind(endX);
            endYProperty().bind(endY);
            setStrokeWidth(2);
            setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
//        setStrokeLineCap(StrokeLineCap.BUTT);
//        getStrokeDashArray().setAll(10.0, 5.0);
            setMouseTransparent(true);
        }
    }

}
