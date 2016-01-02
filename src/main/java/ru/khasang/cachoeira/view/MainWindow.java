package ru.khasang.cachoeira.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;

/**
 * Класс в котором "собирается" главное окно
 */
public class MainWindow implements IWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class.getName());

    private final IController controller;
    private final UIControl uiControl;
    private Stage stage;
    private BorderPane rootLayout;
    private PropertiesPanelController propertiesPanelController;
    private DiagramPaneController diagramPaneController;
    private RootLayoutController rootLayoutController;


    public MainWindow(IController controller, UIControl uiControl) {
        this.controller = controller;
        this.uiControl = uiControl;

        initRootLayout();
        initDiagramPane();
        initPropertiesPanel();

        // Заголовок окна меняется автоматически при изменении имени проекта
        stage.titleProperty().bind(controller.getProject().nameProperty());
    }

    private void initPropertiesPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PropertiesPanel.fxml"));
            TabPane propertiesPanel = loader.load();
            rootLayout.setRight(propertiesPanel);

            propertiesPanelController = loader.getController();
            propertiesPanelController.setController(controller);
            propertiesPanelController.setUIControl(uiControl);
            propertiesPanelController.initTabs();
        } catch (IOException e) {
            LOGGER.debug("Ошибка загрузки: {}", e);
        }
    }

    private void initDiagramPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiagramPane.fxml"));
            TabPane diagramPane = loader.load();
            rootLayout.setCenter(diagramPane);

            diagramPaneController = loader.getController();
            diagramPaneController.setController(controller);
            diagramPaneController.setUIControl(uiControl);
            diagramPaneController.initTaskPane();
            diagramPaneController.initResourcePane();
        } catch (IOException e) {
            LOGGER.debug("Ошибка загрузки: {}", e);
        }
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootLayout.fxml"));
            rootLayout = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(rootLayout));

            rootLayoutController = loader.getController();
            rootLayoutController.setController(controller);
            stage.show();
        } catch (IOException e) {
            LOGGER.debug("Ошибка загрузки: {}", e);
        }
    }

    @Override
    public void launch() {
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public PropertiesPanelController getPropertiesPanelController() {
        return propertiesPanelController;
    }

    public DiagramPaneController getDiagramPaneController() {
        return diagramPaneController;
    }

    public RootLayoutController getRootLayoutController() {
        return rootLayoutController;
    }
}
