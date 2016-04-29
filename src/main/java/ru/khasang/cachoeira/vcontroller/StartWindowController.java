package ru.khasang.cachoeira.vcontroller;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.khasang.cachoeira.view.StartWindowView;

public class StartWindowController extends Application{
    private final StartWindowView startWindowView;

    public StartWindowController() {
        startWindowView = new StartWindowView(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StartWindowController controller = new StartWindowController();
        controller.launch();
    }

    public void launch() {
        startWindowView.createView();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
