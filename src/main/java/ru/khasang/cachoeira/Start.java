package ru.khasang.cachoeira;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.view.UIControl;


/**
 * Этот класс является точкой входа
 */
public class Start extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Start.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Программа запущена.");
        UIControl UIControl = new UIControl();
        UIControl.launchStartWindow();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
