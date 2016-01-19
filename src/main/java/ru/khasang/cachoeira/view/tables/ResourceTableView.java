package ru.khasang.cachoeira.view.tables;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import javafx.application.Platform;
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

    VirtualScrollBar verticalScrollBar;
    VirtualScrollBar horizontalScrollBar;

    public ResourceTableView() {
    }

    public ResourceTableView(ObservableList<S> items) {
        super(items);
    }

    /**
     * Метод для связывания горизонтального и вертикального скролла с переменными контроллера.
     *
     * @param uiControl Контроллер вью
     */
    public void bindScrollsToController(final UIControl uiControl) {
        Platform.runLater(() -> {
            if (verticalScrollBar != null) {
                verticalScrollBar.valueProperty().bindBidirectional(uiControl.resourceVerticalScrollValueProperty());
//                verticalScrollBar.visibleProperty().addListener(observable -> {
//                    verticalScrollBar.setVisible(false);
//                });
                LOGGER.debug("Вертикальный скролл таблицы привязан к {} вью контроллера.",
                        uiControl.resourceVerticalScrollValueProperty().getName());
            }
            if (horizontalScrollBar != null) {
                horizontalScrollBar.valueProperty().bindBidirectional(uiControl.resourceHorizontalScrollValueProperty());
                horizontalScrollBar.setVisible(false);
                horizontalScrollBar.visibleProperty().addListener(observable -> {
                    horizontalScrollBar.setVisible(false);
                });
                LOGGER.debug("Горизонтальный скролл таблицы привязан к {} вью контроллера.",
                        uiControl.resourceHorizontalScrollValueProperty().getName());
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ResourceTableViewSkin<>(this);
    }

    private class ResourceTableViewSkin<T> extends TableViewSkin<T> {
        public ResourceTableViewSkin(TableView<T> tableView) {
            super(tableView);
            // Выцепляем скроллы
            flow.getChildrenUnmodifiable()
                    .parallelStream()
                    .filter(child -> child instanceof VirtualScrollBar)
                    .forEach(child -> {
                        if (((VirtualScrollBar) child).getOrientation() == Orientation.VERTICAL) {
                            verticalScrollBar = (VirtualScrollBar) child;
                            LOGGER.debug("Найден вертикальный скролл таблицы.");
                        }
                        if (((VirtualScrollBar) child).getOrientation() == Orientation.HORIZONTAL) {
                            horizontalScrollBar = (VirtualScrollBar) child;
                            LOGGER.debug("Найден горизонтальный скролл таблицы.");
                        }
                    });
        }
    }
}
