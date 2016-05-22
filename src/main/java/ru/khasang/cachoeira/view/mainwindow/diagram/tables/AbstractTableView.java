package ru.khasang.cachoeira.view.mainwindow.diagram.tables;

import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Skin;
import javafx.scene.control.TreeTableView;

public abstract class AbstractTableView<S> extends TreeTableView<S> {
    protected DoubleProperty horizontalScrollValue;
    protected DoubleProperty verticalScrollValue;
    private VirtualScrollBar verticalScrollBar;
    private VirtualScrollBar horizontalScrollBar;

    public abstract void createTable();

    /**
     * Метод для связывания горизонтального и вертикального скролла с переменными контроллера.
     */
    public void bindScrollsToController() {
        Platform.runLater(() -> {
            if (verticalScrollBar != null) {
                // Синхронизируем вертикальный скролл таблицы и диаграммы
                verticalScrollBar.valueProperty().bindBidirectional(verticalScrollValue);
//                verticalScrollBar.visibleProperty().addListener(observable -> {
//                    verticalScrollBar.setVisible(false);
//                });
            }
            if (horizontalScrollBar != null) {
                horizontalScrollBar.valueProperty().bindBidirectional(horizontalScrollValue);
                horizontalScrollBar.setVisible(false);
                horizontalScrollBar.visibleProperty().addListener(observable -> {
                    horizontalScrollBar.setVisible(false);
                });
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AbstractTableViewSkin<>(this);
    }

    private class AbstractTableViewSkin<T> extends TreeTableViewSkin<T> {
        public AbstractTableViewSkin(TreeTableView<T> tableView) {
            super(tableView);
            // Выцепляем скроллы
            flow.getChildrenUnmodifiable()
                    .stream()
                    .filter(child -> child instanceof VirtualScrollBar)
                    .forEach(child -> {
                        if (((VirtualScrollBar) child).getOrientation() == Orientation.VERTICAL) {
                            verticalScrollBar = (VirtualScrollBar) child;
                        }
                        if (((VirtualScrollBar) child).getOrientation() == Orientation.HORIZONTAL) {
                            horizontalScrollBar = (VirtualScrollBar) child;
                        }
                    });
        }
    }
}
