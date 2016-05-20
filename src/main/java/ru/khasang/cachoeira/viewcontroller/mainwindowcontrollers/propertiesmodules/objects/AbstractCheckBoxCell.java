package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules.objects;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

public abstract class AbstractCheckBoxCell<S, T> extends TableCell<S, T> {
    protected S currentRow;

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        this.setAlignment(Pos.CENTER);
        currentRow = (S) this.getTableRow().getItem();
        if (empty) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            CheckBox checkBox = createCheckBox();
            // Расставляем галочки на нужных строках
            this.setSelectedCheckBox(checkBox);
            // Заполняем столбец чек-боксами
            this.setGraphic(checkBox);
        }
    }

    protected abstract CheckBox createCheckBox();

    protected abstract void setSelectedCheckBox(CheckBox checkBox);
}
