package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.TaskDependencyType;

public class TaskPaneRelationLine extends Group {
    @SuppressWarnings("FieldCanBeLocal")
    private final double[] ARROW_SHAPE = {3, 0, -3, -3, -3, 3};
    private final Color LINE_COLOR = Color.valueOf("03bdf4");

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
            this.getChildren().addAll(initFinishStartRelationLine(parentTaskBar, childTaskBar));
        }
    }

    public void setListener() {
        dependentTypeChangeListener = (observable, oldValue, newValue) -> {
            this.getChildren().clear();
            if (dependenceType.getValue().equals(TaskDependencyType.FINISHSTART)) {
                this.getChildren().addAll(initFinishStartRelationLine(parentTaskBar, childTaskBar));
            }
            if (dependenceType.getValue().equals(TaskDependencyType.STARTFINISH)) {
                this.getChildren().addAll(iniStartFinishRelationLine(parentTaskBar, childTaskBar));
            }
            if (dependenceType.getValue().equals(TaskDependencyType.FINISHFINISH)) {
                this.getChildren().addAll(initFinishFinishRelationLine(parentTaskBar, childTaskBar));
            }
            if (dependenceType.getValue().equals(TaskDependencyType.STARTSTART)) {
                this.getChildren().addAll(initStartStartRelationLine(parentTaskBar, childTaskBar));
            }
        };
        dependenceType.addListener(new WeakChangeListener<>(dependentTypeChangeListener));
    }

    private ObservableList<Node> initFinishStartRelationLine(TaskPaneTaskBar parentTaskBar,
                                                             TaskPaneTaskBar childTaskBar) {
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
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5))
        );
        BoundLine line3 = new BoundLine(
                (DoubleBinding) Bindings
                        .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5)),
                endXProperty,
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5))
        );
        BoundLine line4 = new BoundLine(
                endXProperty,
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5)),
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
        ObservableList<Node> relationLine = FXCollections.observableArrayList();
        relationLine.addAll(startLine, line1, line2, line3, line4, endLine, arrow);
        return relationLine;
    }

    private ObservableList<Node> initStartStartRelationLine(TaskPaneTaskBar parentTaskBar,
                                                            TaskPaneTaskBar childTaskBar) {
        DoubleBinding startXProperty = parentTaskBar.layoutXProperty().subtract(12);
        DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

        DoubleBinding endXProperty = childTaskBar.layoutXProperty().subtract(12);
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
                (DoubleBinding) Bindings
                        .when(endXProperty.lessThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                startYProperty
        );
        BoundLine line2 = new BoundLine(
                (DoubleBinding) Bindings
                        .when(endXProperty.lessThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                startYProperty,
                (DoubleBinding) Bindings
                        .when(endXProperty.lessThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                endYProperty
        );
        BoundLine line3 = new BoundLine(
                (DoubleBinding) Bindings
                        .when(endXProperty.lessThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                endYProperty,
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
        ObservableList<Node> relationLine = FXCollections.observableArrayList();
        relationLine.addAll(startLine, line1, line2, line3, endLine, arrow);
        return relationLine;
    }

    private ObservableList<Node> initFinishFinishRelationLine(TaskPaneTaskBar parentTaskBar,
                                                              TaskPaneTaskBar childTaskBar) {
        DoubleBinding startXProperty = parentTaskBar.layoutXProperty().add(parentTaskBar.widthProperty()).add(12);
        DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

        DoubleBinding endXProperty = childTaskBar.layoutXProperty().add(childTaskBar.widthProperty()).add(12);
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
                endYProperty
        );
        BoundLine line3 = new BoundLine(
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
        ObservableList<Node> relationLine = FXCollections.observableArrayList();
        relationLine.addAll(startLine, line1, line2, line3, endLine, arrow);
        return relationLine;
    }

    private ObservableList<Node> iniStartFinishRelationLine(TaskPaneTaskBar parentTaskBar,
                                                            TaskPaneTaskBar childTaskBar) {
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
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5))
        );
        BoundLine line2 = new BoundLine(
                startXProperty,
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5)),
                (DoubleBinding) Bindings
                        .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5))
        );
        BoundLine line3 = new BoundLine(
                (DoubleBinding) Bindings
                        .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(15.5))
                        .otherwise(startYProperty.subtract(15.5)),
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
        ObservableList<Node> relationLine = FXCollections.observableArrayList();
        relationLine.addAll(startLine, line1, line2, line3, line4, endLine, arrow);
        return relationLine;
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
