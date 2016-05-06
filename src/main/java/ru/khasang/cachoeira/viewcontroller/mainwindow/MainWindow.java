package ru.khasang.cachoeira.viewcontroller.mainwindow;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.viewcontroller.*;
import ru.khasang.cachoeira.viewcontroller.mainwindow.exit.OnClose;
import ru.khasang.cachoeira.viewcontroller.mainwindow.exit.OnCloseMainWindow;

import java.io.IOException;

/**
 * Класс в котором "собирается" главное окно
 */
public class MainWindow implements IWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class.getName());

    private final UIControl uiControl;
    private Stage stage;
    private BorderPane rootLayout;
    private PropertiesPanelController propertiesPanelController;
    private DiagramPaneController diagramPaneController;
    private RootLayoutController rootLayoutController;


    public MainWindow(UIControl uiControl) {
        this.uiControl = uiControl;

        initRootLayout();
        initDiagramPane();
        initPropertiesPanel();

        Platform.runLater(this::refreshDiagrams);

        // Заголовок окна меняется автоматически при изменении имени проекта
        stage.titleProperty().bind(uiControl.getController().getProject().nameProperty());
    }

    private void initPropertiesPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PropertiesPanel.fxml"));
            loader.setResources(UIControl.bundle);
            TabPane propertiesPanel = loader.load();
            rootLayout.setRight(propertiesPanel);

            propertiesPanelController = loader.getController();
            propertiesPanelController.setUIControl(uiControl);
            propertiesPanelController.initTabs();
        } catch (IOException e) {
            LOGGER.debug("Ошибка загрузки: {}", e);
        }
    }

    private void initDiagramPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiagramPane.fxml"));
            loader.setResources(UIControl.bundle);
            TabPane diagramPane = loader.load();
            rootLayout.setCenter(diagramPane);

            diagramPaneController = loader.getController();
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
            loader.setResources(UIControl.bundle);
            rootLayout = loader.load();
            stage = new Stage();
            stage.setHeight(uiControl.getHeightOfWindow());
            stage.setWidth(uiControl.getWidthOfWindow());
            stage.setScene(new Scene(rootLayout));

            rootLayoutController = loader.getController();
            rootLayoutController.setUIControl(uiControl);
            stage.show();
            stage.setMaximized(uiControl.getIsMaximized());
            stage.setOnCloseRequest(event -> {
                OnClose onClose = new OnCloseMainWindow(uiControl);
                // Сохранение значений окна
                onClose.saveProperties();
                // Сохранение проекта в файл
                onClose.saveProject(event);
            });
        } catch (IOException e) {
            LOGGER.debug("Ошибка загрузки: {}", e);
        }
    }

    private void refreshDiagrams() {
        diagramPaneController.getTaskPaneController().refreshTableView(uiControl);
        diagramPaneController.getTaskPaneController().getGanttPlan().getObjectsLayer().refreshPlan(uiControl);
        diagramPaneController.getTaskPaneController().getGanttPlan().getRelationsLayer().refreshRelationsDiagram(uiControl);

        diagramPaneController.getResourcePaneController().getGanttPlan().getObjectsLayer().refreshPlan(uiControl);
    }

    @Override
    public void launch() {
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    public DiagramPaneController getDiagramPaneController() {
        return diagramPaneController;
    }
}
