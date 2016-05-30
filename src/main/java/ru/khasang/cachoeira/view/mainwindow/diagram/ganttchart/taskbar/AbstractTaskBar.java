package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.model.ITask;

public abstract class AbstractTaskBar extends Pane {
    protected static final String BAR_COLOR = "#03A9F4";
    protected static final String HOVER_BAR_COLOR = "#0381f4";
    protected static final String BORDER_BAR_COLOR = "#03bdf4";
    protected static final String DONE_PERCENT_BAR_COLOR = "#0381f4";
    protected static final double BAR_HEIGHT = 18;
    protected static final double ROW_HEIGHT = 31;
    protected static final double BAR_ARC = 5;

    private final ITask task;

    protected Rectangle backgroundRectangle;
    protected Rectangle donePercentRectangle;
    protected Rectangle leftResizableRectangle;
    protected Rectangle rightResizableRectangle;

    public AbstractTaskBar(ITask task) {
        this.task = task;
        this.setPadding(new Insets(0, 0, 5, 0));
    }

    public void createBar() {
        backgroundRectangle = createBackgroundRectangle();
        donePercentRectangle = createDonePercentRectangle(backgroundRectangle);
        leftResizableRectangle = createLeftResizableRectangle(backgroundRectangle);
        rightResizableRectangle = createRightResizableRectangle(backgroundRectangle);

        this.getChildren().addAll(
                backgroundRectangle,
                donePercentRectangle,
                leftResizableRectangle,
                rightResizableRectangle);
    }

    private Rectangle createBackgroundRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(BAR_COLOR));
        rectangle.setStroke(Color.valueOf(BORDER_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);
        rectangle.setHeight(BAR_HEIGHT);
        return rectangle;
    }

    private Rectangle createDonePercentRectangle(Rectangle backgroundRectangle) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.valueOf(DONE_PERCENT_BAR_COLOR));
        rectangle.setArcHeight(BAR_ARC);
        rectangle.setArcWidth(BAR_ARC);

        rectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty().add(1.25));
        rectangle.heightProperty().bind(backgroundRectangle.heightProperty().subtract(2.5));

        rectangle.widthProperty().bind(
                backgroundRectangle.widthProperty().divide(100).multiply(task.donePercentProperty()));

        donePercentRectangle.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        donePercentRectangle.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());
        return rectangle;
    }

    private Rectangle createLeftResizableRectangle(Rectangle backgroundRectangle) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.widthProperty().bind(backgroundRectangle.widthProperty().divide(100).multiply(5));
        rectangle.xProperty().bind(backgroundRectangle.xProperty());
        rectangle.heightProperty().bind(backgroundRectangle.heightProperty());
        rectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty());
        return rectangle;
    }

    private Rectangle createRightResizableRectangle(Rectangle backgroundRectangle) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.widthProperty().bind(backgroundRectangle.widthProperty().divide(100).multiply(5));
        rectangle.xProperty().bind(backgroundRectangle.xProperty()
                .add(backgroundRectangle.widthProperty())
                .subtract(rectangle.widthProperty()));
        rectangle.heightProperty().bind(backgroundRectangle.heightProperty());
        rectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty());
        return rectangle;
    }

    public ITask getTask() {
        return task;
    }

    public Rectangle getBackgroundRectangle() {
        return backgroundRectangle;
    }

    public Rectangle getDonePercentRectangle() {
        return donePercentRectangle;
    }

    public Rectangle getLeftResizableRectangle() {
        return leftResizableRectangle;
    }

    public Rectangle getRightResizableRectangle() {
        return rightResizableRectangle;
    }
}
