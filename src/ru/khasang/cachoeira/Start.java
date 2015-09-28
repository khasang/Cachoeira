package ru.khasang.cachoeira;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.khasang.cachoeira.view.MainWindow;

/**
 * Created by truesik on 28.09.2015.
 */
public class Start extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindow mainWindow = new MainWindow();
        mainWindow.launch();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
