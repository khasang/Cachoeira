package ru.khasang.cachoeira;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.data.DataService;
import ru.khasang.cachoeira.viewcontroller.StartWindowController;

import java.io.File;
import java.util.List;


/**
 * Этот класс является точкой входа
 */
public class Starter extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Программа запущена.");
        List<String> projectsFilePath = getParameters().getRaw();
        if (!projectsFilePath.isEmpty()) {
            for (String projectFilePath : projectsFilePath) {
                File file = new File(projectFilePath);
                DataService.getInstance().loadProject(file);
            }
        } else {
            StartWindowController startWindowController = new StartWindowController();
            startWindowController.launch();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
