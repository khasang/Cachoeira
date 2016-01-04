package ru.khasang.cachoeira.view.tables;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.view.UIControl;

/**
 * Пришлось сделать свою таблицу, чтобы вытащить вертикальный скролл и синхронизировать его с диаграммой Ганта.
 */
public class ResourceTableView<S> extends TableView<S> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceTableView.class.getName());

    private final UIControl uiControl;

    public ResourceTableView(ObservableList<S> items, UIControl uiControl) {
        super(items);
        this.uiControl = uiControl;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ResourceTableViewSkin<>(this, uiControl);
    }

    private class ResourceTableViewSkin<T> extends TableViewSkin<T> {
        public ResourceTableViewSkin(TableView<T> tableView, UIControl uiControl) {
            super(tableView);
            // Выцепляем скроллы
            VirtualScrollBar verticalScrollBar = null;
            VirtualScrollBar horizontalScrollBar = null;
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
            if (verticalScrollBar == null) {
                return;
            }
            verticalScrollBar.valueProperty().bindBidirectional(uiControl.resourceVerticalScrollValueProperty());
            LOGGER.debug("Вертикальный скролл таблицы привязан к {} вью контроллера.", uiControl.resourceVerticalScrollValueProperty());

            if (horizontalScrollBar == null) {
                return;
            }
            horizontalScrollBar.valueProperty().bindBidirectional(uiControl.resourceHorizontalScrollValueProperty());
            horizontalScrollBar.setVisible(false);
            final VirtualScrollBar finalHorizontalScrollBar = horizontalScrollBar;
            horizontalScrollBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                finalHorizontalScrollBar.setVisible(false);
            });
            LOGGER.debug("Горизонтальный скролл таблицы привязан к {} вью контроллера.", uiControl.resourceHorizontalScrollValueProperty());
        }
    }
}
