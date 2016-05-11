package ru.khasang.cachoeira.vcontroller;

import ru.khasang.cachoeira.view.AboutWindowView;
import ru.khasang.cachoeira.view.IView;

public class AboutWindowController {
    IView view;

    public AboutWindowController() {
        view = new AboutWindowView();
    }

    public void launch() {
        view.createView();
    }
}
