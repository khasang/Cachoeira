package ru.khasang.cachoeira.view.tables;

import com.sun.javafx.scene.control.skin.*;
import javafx.application.Platform;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskTreeTableView.class.getName());

    VirtualScrollBar verticalScrollBar;
    VirtualScrollBar horizontalScrollBar;

    public TaskTreeTableView() {
    }

    public TaskTreeTableView(TreeItem<S> root) {
        super(root);
    }

    /**
     * Метод для связывания горизонтального и вертикального скролла с переменными контроллера.
     *
     * @param uiControl Контроллер вью
     */
    public void bindScrollsToController(UIControl uiControl) {
        Platform.runLater(() -> {
            if (verticalScrollBar != null) {
                // Синхронизируем вертикальный скролл таблицы и диаграммы
                verticalScrollBar.valueProperty().bindBidirectional(uiControl.taskVerticalScrollValueProperty());
//                verticalScrollBar.visibleProperty().addListener(observable -> {
//                    verticalScrollBar.setVisible(false);
//                });
                LOGGER.debug("Вертикальный скролл таблицы привязан к {} вью контроллера.",
                        uiControl.taskVerticalScrollValueProperty().getName());
            }
            if (horizontalScrollBar != null) {
                horizontalScrollBar.valueProperty().bindBidirectional(uiControl.taskHorizontalScrollValueProperty());
                horizontalScrollBar.setVisible(false);
                horizontalScrollBar.visibleProperty().addListener(observable -> {
                    horizontalScrollBar.setVisible(false);
                });
                LOGGER.debug("Горизонтальный скродл таблицы привязан к {} вью контроллера.",
                        uiControl.taskHorizontalScrollValueProperty().getName());
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TaskTreeTableViewSkin<>(this);
    }

    private class TaskTreeTableViewSkin<T> extends TreeTableViewSkin<T> {
        public TaskTreeTableViewSkin(TreeTableView<T> tableView) {
            super(tableView);
            // Выцепляем скроллы
            for (Node child : flow.getChildrenUnmodifiable()) {
                if (child instanceof VirtualScrollBar) {
                    if (((VirtualScrollBar) child).getOrientation() == Orientation.VERTICAL) {
                        verticalScrollBar = (VirtualScrollBar) child;
                        LOGGER.debug("Найден вертикальный скролл таблицы.");
                    }
                    if (((VirtualScrollBar) child).getOrientation() == Orientation.HORIZONTAL) {
                        horizontalScrollBar = (VirtualScrollBar) child;
                        LOGGER.debug("Найден горизонтальный скролл таблицы.");
                    }
                }
            }
        }
    }
}
