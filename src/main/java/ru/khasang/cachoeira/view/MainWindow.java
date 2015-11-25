package ru.khasang.cachoeira.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;

/**
 * Created by truesik on 28.09.2015.
 */
public class MainWindow implements IWindow {
    private final IController controller;
    private final UIControl uiControl;
    private Stage stage;
    private BorderPane rootLayout;

    public MainWindow(IController controller, UIControl uiControl) {
        this.controller = controller;
        this.uiControl = uiControl;

        initRootLayout();
        initDiagramPane();
        initPropertiesPanel();

        stage.titleProperty().bind(controller.getProject().nameProperty());
    }

    private void initPropertiesPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PropertiesPanel.fxml"));
            TabPane propertiesPanel = loader.load();
            rootLayout.setRight(propertiesPanel);

            PropertiesPanelController propertiesPanelController = loader.getController();
            propertiesPanelController.setController(controller);
            propertiesPanelController.initTabs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDiagramPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiagramPane.fxml"));
            TabPane diagramPane = loader.load();
            rootLayout.setCenter(diagramPane);

            DiagramPaneController diagramPaneController = loader.getController();
            diagramPaneController.setController(controller);
            diagramPaneController.setUIControl(uiControl);
            diagramPaneController.initTaskPane();
            diagramPaneController.initResourcePane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootLayout.fxml"));
            rootLayout = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(rootLayout));

            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setController(controller);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launch() {

    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
