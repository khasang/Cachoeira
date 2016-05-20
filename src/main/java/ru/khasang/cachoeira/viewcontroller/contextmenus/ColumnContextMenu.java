package ru.khasang.cachoeira.viewcontroller.contextmenus;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeTableView;

import java.util.ArrayList;

/**
 * Контекстное меню для выбора видимых столбцов в таблицах
 */
public class ColumnContextMenu extends ContextMenu {
    public ColumnContextMenu(TreeTableView<?> treeTableView) {
        updateContextMenuColumnTTV(treeTableView);
    }

    public void updateContextMenuColumnTTV(TreeTableView<?> treeTableView) {
        super.getItems().clear();
        super.getItems().addAll(getCheckMenuItems(treeTableView));
    }

    private ArrayList<CheckMenuItem> getCheckMenuItems(TreeTableView<?> treeTableView) {
        ArrayList<CheckMenuItem> contextItems = new ArrayList<>();
        for (int i = 1; i < treeTableView.getColumns().size(); i++) {
            contextItems.add(new CheckMenuItem(treeTableView.getColumns().get(i).getText()));
            contextItems.get(i - 1).setSelected(treeTableView.getColumns().get(i).isVisible());
            int j = i;
            contextItems.get(i - 1).setOnAction(event -> treeTableView.getColumns().get(j).setVisible(((CheckMenuItem) event.getSource()).isSelected()));
        }
        return contextItems;
    }
}
