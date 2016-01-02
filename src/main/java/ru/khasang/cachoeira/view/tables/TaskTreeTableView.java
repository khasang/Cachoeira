package ru.khasang.cachoeira.view.tables;

import com.sun.javafx.scene.control.skin.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.view.UIControl;

/**
 * Пришлось сделать свою таблицу, чтобы вытащить вертикальный скролл и синхронизировать его с диаграммой Ганта.
 */
public class TaskTreeTableView<S> extends TreeTableView<S> {
    private static final Logger logger = LoggerFactory.getLogger(TaskTreeTableView.class.getName());

    private final UIControl uiControl;

    public TaskTreeTableView(TreeItem<S> root, UIControl uiControl) {
        super(root);
        this.uiControl = uiControl;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TaskTreeTableViewSkin<>(this, uiControl);
    }

    private class TaskTreeTableViewSkin<T> extends TreeTableViewSkin<T> {
        public TaskTreeTableViewSkin(TreeTableView<T> tableView, UIControl uiControl) {
            super(tableView);
            // Выцепляем скроллы
            VirtualScrollBar verticalScrollBar = null;
            VirtualScrollBar horizontalScrollBar = null;
            for (Node child : flow.getChildrenUnmodifiable()) {
                if (child instanceof VirtualScrollBar) {
                    if (((VirtualScrollBar) child).getOrientation() == Orientation.VERTICAL) {
                        verticalScrollBar = (VirtualScrollBar) child;
                        logger.debug("Найден вертикальный скролл таблицы.");
                    }
                    if (((VirtualScrollBar) child).getOrientation() == Orientation.HORIZONTAL) {
                        horizontalScrollBar = (VirtualScrollBar) child;
                        logger.debug("Найден горизонтальный скролл таблицы.");
                    }
                }
            }
            if (verticalScrollBar == null) return;
            // Синхронизируем вертикальный скролл таблицы и диаграммы
            verticalScrollBar.valueProperty().bindBidirectional(uiControl.taskVerticalScrollValueProperty());
            logger.debug("Вертикальный скролл таблицы привязан к {} вью контроллера.", uiControl.taskVerticalScrollValueProperty().getName());

            if (horizontalScrollBar == null) return;
            horizontalScrollBar.valueProperty().bindBidirectional(uiControl.taskHorizontalScrollValueProperty());
            horizontalScrollBar.setVisible(false);
            final VirtualScrollBar finalHorizontalScrollBar = horizontalScrollBar;
            horizontalScrollBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                finalHorizontalScrollBar.setVisible(false);
            });
            logger.debug("Горизонтальный скродл таблицы привязан к {} вью контроллера.", uiControl.taskHorizontalScrollValueProperty().getName());
        }
    }
}
