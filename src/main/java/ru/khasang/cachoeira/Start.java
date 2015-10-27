package ru.khasang.cachoeira;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.khasang.cachoeira.view.UIControl;

/**
 * Created by truesik on 28.09.2015.
 */
public class Start extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
//        MainWindow mainWindow = new MainWindow();
//        mainWindow.launch();
        UIControl UIControl = new UIControl();
        UIControl.launchMainWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
