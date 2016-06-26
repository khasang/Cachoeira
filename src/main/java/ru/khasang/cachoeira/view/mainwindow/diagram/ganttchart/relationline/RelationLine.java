package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.relationline;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.TaskDependencyType;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

public abstract class RelationLine extends Group {
    private static final double[] ARROW_SHAPE = {3, 0, -3, -3, -3, 3};
    private static final Color LINE_COLOR = Color.valueOf("03bdf4");
    private static final double WIDTH = 12;
    private static final double HEIGHT = 15.5;

    private final TaskBar parentTaskBar;
    private final TaskBar childTaskBar;
    private ObjectProperty<TaskDependencyType> dependenceType;
    private ITask parentTask;
    private ITask childTask;

    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener dependentTypeChangeListener;

    /**
     *
     * @param parentTaskBar
     * @param childTaskBar
     * @param dependenceType
     */
    public RelationLine(TaskBar parentTaskBar,
                        TaskBar childTaskBar,
                        ObjectProperty<TaskDependencyType> dependenceType) {
        this.parentTaskBar = parentTaskBar;
        this.childTaskBar = childTaskBar;
        this.dependenceType = dependenceType;
        this.parentTask = parentTaskBar.getTask();
        this.childTask = childTaskBar.getTask();

        attachDependencyListener();

        if (dependenceType.getValue().equals(TaskDependencyType.FINISHSTART)) {
            this.getChildren().addAll(initFinishStartRelationLine());
        }
    }

    private void attachDependencyListener() {
        dependentTypeChangeListener = this::dependencyTypeChangeHandler;
        dependenceType.addListener(new WeakInvalidationListener(dependentTypeChangeListener));
    }

    private void dependencyTypeChangeHandler(Observable observable) {
        this.getChildren().clear();
        if (dependenceType.getValue().equals(TaskDependencyType.FINISHSTART)) {
            this.getChildren().addAll(initFinishStartRelationLine());
        }
        if (dependenceType.getValue().equals(TaskDependencyType.STARTFINISH)) {
            this.getChildren().addAll(iniStartFinishRelationLine());
        }
        if (dependenceType.getValue().equals(TaskDependencyType.FINISHFINISH)) {
            this.getChildren().addAll(initFinishFinishRelationLine());
        }
        if (dependenceType.getValue().equals(TaskDependencyType.STARTSTART)) {
            this.getChildren().addAll(initStartStartRelationLine());
        }
    }

    private ObservableList<Node> initFinishStartRelationLine() {
        DoubleBinding startXProperty = parentTaskBar.layoutXProperty().add(parentTaskBar.widthProperty()).add(WIDTH);
        DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

        DoubleBinding endXProperty = childTaskBar.layoutXProperty().subtract(WIDTH);
        DoubleBinding endYProperty = childTaskBar.layoutYProperty().add(childTaskBar.heightProperty().divide(2));

        BoundLine startLine = new BoundLine(
                startXProperty.subtract(WIDTH),
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
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT))
        );
        BoundLine line3 = new BoundLine(
                (DoubleBinding) Bindings
                        .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT)),
                endXProperty,
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT))
        );
        BoundLine line4 = new BoundLine(
                endXProperty,
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT)),
                endXProperty,
                endYProperty
        );
        BoundLine endLine = new BoundLine(
                endXProperty,
                endYProperty,
                endXProperty.add(WIDTH),
                endYProperty
        );
        Arrow arrow = new Arrow(
                endXProperty.add(WIDTH).subtract(2),
                endYProperty,
                ARROW_SHAPE
        );
        ObservableList<Node> relationLine = FXCollections.observableArrayList();
        relationLine.addAll(startLine, line1, line2, line3, line4, endLine, arrow);
        return relationLine;
    }

    private ObservableList<Node> initStartStartRelationLine() {
        DoubleBinding startXProperty = parentTaskBar.layoutXProperty().subtract(WIDTH);
        DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

        DoubleBinding endXProperty = childTaskBar.layoutXProperty().subtract(WIDTH);
        DoubleBinding endYProperty = childTaskBar.layoutYProperty().add(childTaskBar.heightProperty().divide(2));

        BoundLine startLine = new BoundLine(
                startXProperty.add(WIDTH),
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
                endXProperty.add(WIDTH),
                endYProperty
        );
        Arrow arrow = new Arrow(
                endXProperty.add(WIDTH).subtract(2),
                endYProperty,
                ARROW_SHAPE
        );
        ObservableList<Node> relationLine = FXCollections.observableArrayList();
        relationLine.addAll(startLine, line1, line2, line3, endLine, arrow);
        return relationLine;
    }

    private ObservableList<Node> initFinishFinishRelationLine() {
        DoubleBinding startXProperty = parentTaskBar.layoutXProperty().add(parentTaskBar.widthProperty()).add(WIDTH);
        DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

        DoubleBinding endXProperty = childTaskBar.layoutXProperty().add(childTaskBar.widthProperty()).add(WIDTH);
        DoubleBinding endYProperty = childTaskBar.layoutYProperty().add(childTaskBar.heightProperty().divide(2));

        BoundLine startLine = new BoundLine(
                startXProperty.subtract(WIDTH),
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
                endXProperty.subtract(WIDTH),
                endYProperty
        );
        Arrow arrow = new Arrow(
                endXProperty.subtract(WIDTH).add(2),
                endYProperty,
                ARROW_SHAPE
        );
        arrow.setRotate(180);
        ObservableList<Node> relationLine = FXCollections.observableArrayList();
        relationLine.addAll(startLine, line1, line2, line3, endLine, arrow);
        return relationLine;
    }

    private ObservableList<Node> iniStartFinishRelationLine() {
        DoubleBinding startXProperty = parentTaskBar.layoutXProperty().subtract(WIDTH);
        DoubleBinding startYProperty = parentTaskBar.layoutYProperty().add(parentTaskBar.heightProperty().divide(2));

        DoubleBinding endXProperty = childTaskBar.layoutXProperty().add(childTaskBar.widthProperty()).add(WIDTH);
        DoubleBinding endYProperty = childTaskBar.layoutYProperty().add(childTaskBar.heightProperty().divide(2));

        BoundLine startLine = new BoundLine(
                startXProperty.add(WIDTH),
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
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT))
        );
        BoundLine line2 = new BoundLine(
                startXProperty,
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT)),
                (DoubleBinding) Bindings
                        .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT))
        );
        BoundLine line3 = new BoundLine(
                (DoubleBinding) Bindings
                        .when(endXProperty.greaterThanOrEqualTo(startXProperty))
                        .then(endXProperty)
                        .otherwise(startXProperty),
                (DoubleBinding) Bindings
                        .when(endYProperty.greaterThan(startYProperty))
                        .then(startYProperty.add(HEIGHT))
                        .otherwise(startYProperty.subtract(HEIGHT)),
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
                endXProperty.subtract(WIDTH),
                endYProperty
        );
        Arrow arrow = new Arrow(
                endXProperty.subtract(WIDTH).add(2),
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

    /**
     * Getter.
     * @return Task.
     */
    public ITask getParentTask() {
        return parentTask;
    }

    /**
     * Getter.
     * @return Task.
     */
    public ITask getChildTask() {
        return childTask;
    }
}