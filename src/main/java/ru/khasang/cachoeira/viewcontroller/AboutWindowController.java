package ru.khasang.cachoeira.viewcontroller;

import javafx.scene.input.MouseEvent;
import ru.khasang.cachoeira.view.AboutWindowView;
import ru.khasang.cachoeira.view.IView;

public class AboutWindowController {
    private IView view;

    public AboutWindowController() {
        view = new AboutWindowView();
    }

    public void launch() {
        view.createView();
        view.getStage().getScene().setOnMouseClicked(this::closeWindow);
    }

    private void closeWindow(MouseEvent event) {
        view.getStage().close();
    }
}
