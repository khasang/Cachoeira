package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import ru.khasang.cachoeira.model.TaskDependencyType;

public class TaskPaneRelationLine extends Group {
    @SuppressWarnings("FieldCanBeLocal")
    private final double[] ARROW_SHAPE = {0, 0, 3, -6, -3, -6};
    private final Color LINE_COLOR = Color.valueOf("03bdf4");
    private final Color HOVER_LINE_COLOR = Color.valueOf("#03A9F4");

    public TaskPaneRelationLine(TaskPaneTaskBar parentTaskBar,
                                TaskPaneTaskBar childTaskBar,
                                TaskDependencyType dependenceType) {
        if (dependenceType.equals(TaskDependencyType.FINISHSTART)) {
            BoundLine firstLine = new BoundLine(
                    parentTaskBar.layoutXProperty().add(parentTaskBar.widthProperty()),
                    parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2)),
                    childTaskBar.layoutXProperty().add(5),
                    parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2))
            );
            BoundLine secondLine = new BoundLine(
                    childTaskBar.layoutXProperty().add(5),
                    parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2).add(1)),
                    childTaskBar.layoutXProperty().add(5),
                    childTaskBar.layoutYProperty().add(6.5)
            );
            Arrow arrow = new Arrow(
                    childTaskBar.layoutXProperty().add(5),
                    childTaskBar.layoutYProperty().add(6.5),
                    ARROW_SHAPE);
            this.getChildren().addAll(firstLine, secondLine, arrow);
//            this.hoverProperty().addListener(event -> {
//                if (this.isHover()) {
//                    firstLine.setStroke(HOVER_LINE_COLOR);
//                    secondLine.setStroke(HOVER_LINE_COLOR);
//                    arrow.setFill(HOVER_LINE_COLOR);
//                } else {
//                    firstLine.setStroke(LINE_COLOR);
//                    secondLine.setStroke(LINE_COLOR);
//                    arrow.setFill(LINE_COLOR);
//                }
//            });
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
            setMouseTransparent(true);
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
}
