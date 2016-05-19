package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.buttonsbar;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;

public abstract class ButtonsBoxController {
    protected final AbstractButtonsBox buttonsBox;
    protected final MainWindowController controller;

    public ButtonsBoxController(AbstractButtonsBox buttonsBox, MainWindowController controller) {
        this.buttonsBox = buttonsBox;
        this.controller = controller;
    }
    public void attachButtonsEvents() {
        buttonsBox.getAddButton().setOnAction(this::addHandler);
        buttonsBox.getRemoveButton().setOnAction(this::removeHandler);
        buttonsBox.getRemoveButton().disableProperty().bind(bindToSelectedItem());
        buttonsBox.getZoomSlider().valueProperty().bindBidirectional(controller.zoomMultiplierProperty());
    }

    protected abstract void addHandler(ActionEvent event);

    protected abstract void removeHandler(ActionEvent event);

    protected abstract ObservableValue<? extends Boolean> bindToSelectedItem();
}
