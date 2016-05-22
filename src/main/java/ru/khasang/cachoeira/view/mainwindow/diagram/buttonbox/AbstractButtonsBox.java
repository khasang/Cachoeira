package ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox;

import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import ru.khasang.cachoeira.view.MaterialButton;

public abstract class AbstractButtonsBox extends HBox{
    protected MaterialButton addButton;
    protected MaterialButton removeButton;
    protected Slider zoomSlider;

    public AbstractButtonsBox() {
        addButton = new MaterialButton("", new ImageView(getClass().getResource("/img/ic_add.png").toExternalForm()));
        removeButton = new MaterialButton("", new ImageView(getClass().getResource("/img/ic_remove.png").toExternalForm()));
    }

    public void createButtonsBox() {
        Region separateRegion = new Region();
        HBox.setHgrow(separateRegion, Priority.ALWAYS);
        zoomSlider = new Slider();
        zoomSlider.setMin(2);
        zoomSlider.setMax(130);
        this.getChildren().addAll(addButton, removeButton, separateRegion, zoomSlider);
    }

    public MaterialButton getAddButton() {
        return addButton;
    }

    public MaterialButton getRemoveButton() {
        return removeButton;
    }

    public Slider getZoomSlider() {
        return zoomSlider;
    }
}
