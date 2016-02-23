package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.TaskDependencyType;

public class TaskPaneRelationLine extends Group {
    @SuppressWarnings("FieldCanBeLocal")
    private final double[] ARROW_SHAPE = {3, 0, -3, -3, -3, 3};
    private final Color LINE_COLOR = Color.valueOf("03bdf4");
    private final Color HOVER_LINE_COLOR = Color.valueOf("#03A9F4");

    private ITask parentTask;
    private ITask childTask;

    public TaskPaneRelationLine(TaskPaneTaskBar parentTaskBar,
                                TaskPaneTaskBar childTaskBar,
                                TaskDependencyType dependenceType) {
        this.parentTask = parentTaskBar.getTask();
        this.childTask = childTaskBar.getTask();

        if (dependenceType.equals(TaskDependencyType.FINISHSTART)) {
            DoubleBinding startXProperty = parentTaskBar.layoutXProperty().add(parentTaskBar.widthProperty()).add(12);
            DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

            DoubleBinding endXProperty = childTaskBar.layoutXProperty().subtract(12);
            DoubleBinding endYProperty = childTaskBar.layoutYProperty().add(childTaskBar.heightProperty().divide(2));

            BoundLine startLine = new BoundLine(
                    startXProperty.subtract(12),
                    startYProperty,
                    startXProperty,
                    startYProperty
            );
            BoundLine line1 = new BoundLine(
                    startXProperty,
                    startYProperty,
                    (DoubleBinding) Bindings
                            .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                            .then(endXProperty)
                            .otherwise(startXProperty),
                    startYProperty
            );
            BoundLine line2 = new BoundLine(
                    (DoubleBinding) Bindings
                            .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                            .then(endXProperty)
                            .otherwise(startXProperty),
                    startYProperty,
                    (DoubleBinding) Bindings
                            .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                            .then(endXProperty)
                            .otherwise(startXProperty),
                    startYProperty.add(endYProperty).divide(2)
            );
            BoundLine line3 = new BoundLine(
                    (DoubleBinding) Bindings
                            .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                            .then(endXProperty)
                            .otherwise(startXProperty),
                    startYProperty.add(endYProperty).divide(2),
                    endXProperty,
                    startYProperty.add(endYProperty).divide(2)
            );
            BoundLine line4 = new BoundLine(
                    endXProperty,
                    startYProperty.add(endYProperty).divide(2),
                    endXProperty,
                    endYProperty
            );
            BoundLine endLine = new BoundLine(
                    endXProperty,
                    endYProperty,
                    endXProperty.add(12),
                    endYProperty
            );
            Arrow arrow = new Arrow(
                    endXProperty.add(12).subtract(3),
                    endYProperty.add(0),
                    ARROW_SHAPE
            );
            this.getChildren().addAll(startLine, line1, line2, line3, line4, endLine, arrow);
        }
    }

    class BoundLine extends Line {
        BoundLine(DoubleBinding startX, DoubleBinding startY, DoubleBinding endX, DoubleBinding endY) {
            startXProperty().bind(startX);
            startYProperty().bind(startY);
            endXProperty().bind(endX);
            endYProperty().bind(endY);
            setStrokeWidth(1);
            setStroke(LINE_COLOR);
//            setMouseTransparent(true);
        }
    }

    class Arrow extends Polygon {
        Arrow(DoubleBinding x, DoubleBinding y, double... points) {
            super(points);
            setFill(LINE_COLOR);
            this.layoutXProperty().bind(x);
            this.layoutYProperty().bind(y);
        }
    }

    public ITask getParentTask() {
        return parentTask;
    }

    public ITask getChildTask() {
        return childTask;
    }
}
