package ru.khasang.cachoeira.vcontroller;

import ru.khasang.cachoeira.view.CreateNewProjectWindowView;

public class CreateNewProjectWindowController {
    private final CreateNewProjectWindowView newProjectWindowView;

    public CreateNewProjectWindowController() {
        newProjectWindowView = new CreateNewProjectWindowView(this);
    }

    public void launch() {
        newProjectWindowView.createView();
    }
}
