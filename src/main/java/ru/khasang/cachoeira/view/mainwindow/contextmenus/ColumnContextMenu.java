package ru.khasang.cachoeira.view.mainwindow.contextmenus;

import javafx.scene.control.*;

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


    public ColumnContextMenu(TableView<?> tableView) {
        updateContextMenuColumnTV(tableView);
    }

    public void updateContextMenuColumnTV(TableView<?> tableView) {
        super.getItems().clear();
        super.getItems().addAll(getCheckMenuItems(tableView));
    }

    private ArrayList<CheckMenuItem> getCheckMenuItems(TableView<?> tableView) {
        ArrayList<CheckMenuItem> contextItems = new ArrayList<>();
        for (int i = 1; i < tableView.getColumns().size(); i++) {
            contextItems.add(new CheckMenuItem(tableView.getColumns().get(i).getText()));
            contextItems.get(i - 1).setSelected(tableView.getColumns().get(i).isVisible());
            int j = i;
            contextItems.get(i - 1).setOnAction(event -> tableView.getColumns().get(j).setVisible(((CheckMenuItem) event.getSource()).isSelected()));
        }
        return contextItems;
    }

}
