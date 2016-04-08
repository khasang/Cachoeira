package ru.khasang.cachoeira.view.mainwindow;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.data.ISettingsManager;
import ru.khasang.cachoeira.data.SettingsManager;
import ru.khasang.cachoeira.view.*;

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
            stage.setScene(new Scene(rootLayout, uiControl.getWidthOfWindow(), uiControl.getHeightOfWindow()));

            rootLayoutController = loader.getController();
            rootLayoutController.setController(uiControl.getController());
            rootLayoutController.setUIControl(uiControl);
            stage.show();
            stage.setMaximized(uiControl.getIsMaximized());
            stage.setOnCloseRequest(event -> {
                // Сохранение значений окна
                ISettingsManager settingsManager = SettingsManager.getInstance();
                settingsManager.writeUIValues(
                        uiControl.getSplitPaneDividerValue(),
                        uiControl.getZoomMultiplier(),
                        stage.getWidth(),
                        stage.getHeight(),
                        stage.isMaximized());

                // Сохранение проекта в файл
                DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
                storeInterface.saveProjectToFile(uiControl.getFile(), uiControl.getController().getProject());
                storeInterface.saveTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
                storeInterface.saveResourcesToFile(uiControl.getFile(), uiControl.getController().getProject());
                storeInterface.saveParentTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
                storeInterface.saveChildTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
                storeInterface.saveResourcesByTask(uiControl.getFile(), uiControl.getController().getProject());
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
