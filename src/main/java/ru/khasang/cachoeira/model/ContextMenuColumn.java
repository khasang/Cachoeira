package ru.khasang.cachoeira.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.util.ArrayList;

/**
 * Created by admin on 03.11.2015.
 */
public class ContextMenuColumn extends ContextMenu {

    public ContextMenuColumn(TreeTableView<?> tTV) {
        ArrayList<CheckMenuItem> contextItems=new ArrayList<>();
        for (int i = 1; i < tTV.getColumns().size(); i++) {
            contextItems.add(new CheckMenuItem(tTV.getColumns().get(i).getText()));
            contextItems.get(i - 1).setSelected(tTV.getColumns().get(i).isVisible());
            int j = i;
            contextItems.get(i-1).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tTV.getColumns().get(j).setVisible(((CheckMenuItem) event.getSource()).isSelected());
                }
            });
        }
      //  contextItems.get(0).setDisable(true);
        super.getItems().addAll(contextItems);
    }


    public ContextMenuColumn(TableView<?> tV) {
        ArrayList<CheckMenuItem> contextItems=new ArrayList<>();
        for (int i = 1; i < tV.getColumns().size(); i++) {
            contextItems.add(new CheckMenuItem(tV.getColumns().get(i).getText()));
            contextItems.get(i - 1).setSelected(tV.getColumns().get(i).isVisible());
            int j = i;
            contextItems.get(i-1).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tV.getColumns().get(j).setVisible(((CheckMenuItem) event.getSource()).isSelected());
                }
            });
        }
        //  contextItems.get(0).setDisable(true);
        super.getItems().addAll(contextItems);
    }


}
