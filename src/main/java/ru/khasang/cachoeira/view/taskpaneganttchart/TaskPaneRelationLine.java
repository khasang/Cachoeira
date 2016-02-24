package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
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

    private final TaskPaneTaskBar parentTaskBar;
    private final TaskPaneTaskBar childTaskBar;
    private ObjectProperty<TaskDependencyType> dependenceType;
    private ITask parentTask;
    private ITask childTask;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TaskDependencyType> dependentTypeChangeListener;

    public TaskPaneRelationLine(TaskPaneTaskBar parentTaskBar,
                                TaskPaneTaskBar childTaskBar,
                                ObjectProperty<TaskDependencyType> dependenceType) {
        this.parentTaskBar = parentTaskBar;
        this.childTaskBar = childTaskBar;
        this.dependenceType = dependenceType;
        this.parentTask = parentTaskBar.getTask();
        this.childTask = childTaskBar.getTask();

        setListener();

        if (dependenceType.getValue().equals(TaskDependencyType.FINISHSTART)) {
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
                    startYProperty.add(15.5)
            );
            BoundLine line3 = new BoundLine(
                    (DoubleBinding) Bindings
                            .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                            .then(endXProperty)
                            .otherwise(startXProperty),
                    startYProperty.add(15.5),
                    endXProperty,
                    startYProperty.add(15.5)
            );
            BoundLine line4 = new BoundLine(
                    endXProperty,
                    startYProperty.add(15.5),
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
                    endXProperty.add(12).subtract(2),
                    endYProperty,
                    ARROW_SHAPE
            );
            this.getChildren().addAll(startLine, line1, line2, line3, line4, endLine, arrow);
        }
    }

    public void setListener() {
        dependentTypeChangeListener = (observable, oldValue, newValue) -> {
            this.getChildren().clear();
            if (dependenceType.getValue().equals(TaskDependencyType.FINISHSTART)) {
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
                        startYProperty.add(15.5)
                );
                BoundLine line3 = new BoundLine(
                        (DoubleBinding) Bindings
                                .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                                .then(endXProperty)
                                .otherwise(startXProperty),
                        startYProperty.add(15.5),
                        endXProperty,
                        startYProperty.add(15.5)
                );
                BoundLine line4 = new BoundLine(
                        endXProperty,
                        startYProperty.add(15.5),
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
                        endXProperty.add(12).subtract(2),
                        endYProperty,
                        ARROW_SHAPE
                );
                this.getChildren().addAll(startLine, line1, line2, line3, line4, endLine, arrow);
            }
            if (dependenceType.getValue().equals(TaskDependencyType.STARTFINISH)) {
                DoubleBinding startXProperty = parentTaskBar.layoutXProperty().subtract(12);
                DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

                DoubleBinding endXProperty = childTaskBar.layoutXProperty().add(childTaskBar.widthProperty()).add(12);
                DoubleBinding endYProperty = childTaskBar.layoutYProperty().add(childTaskBar.heightProperty().divide(2));

                BoundLine startLine = new BoundLine(
                        startXProperty.add(12),
                        startYProperty,
                        startXProperty,
                        startYProperty
                );
                BoundLine line1 = new BoundLine(
                        startXProperty,
                        startYProperty,
                        startXProperty,
                        startYProperty.add(15.5)
                );
                BoundLine line2 = new BoundLine(
                        startXProperty,
                        startYProperty.add(15.5),
                        (DoubleBinding) Bindings
                                .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                                .then(endXProperty)
                                .otherwise(startXProperty),
                        startYProperty.add(15.5)
                );
                BoundLine line3 = new BoundLine(
                        (DoubleBinding) Bindings
                                .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                                .then(endXProperty)
                                .otherwise(startXProperty),
                        startYProperty.add(15.5),
                        (DoubleBinding) Bindings
                                .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                                .then(endXProperty)
                                .otherwise(startXProperty),
                        endYProperty
                );
                BoundLine line4 = new BoundLine(
                        (DoubleBinding) Bindings
                                .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                                .then(endXProperty)
                                .otherwise(startXProperty),
                        endYProperty,
                        endXProperty,
                        endYProperty
                );
                BoundLine endLine = new BoundLine(
                        endXProperty,
                        endYProperty,
                        endXProperty.subtract(12),
                        endYProperty
                );
                Arrow arrow = new Arrow(
                        endXProperty.subtract(12).add(2),
                        endYProperty,
                        ARROW_SHAPE
                );
                arrow.setRotate(180);
                this.getChildren().addAll(startLine, line1, line2, line3, line4, endLine, arrow);
            }
        };
        dependenceType.addListener(new WeakChangeListener<>(dependentTypeChangeListener));
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
