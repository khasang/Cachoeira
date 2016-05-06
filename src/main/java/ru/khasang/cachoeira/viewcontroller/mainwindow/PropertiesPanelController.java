package ru.khasang.cachoeira.viewcontroller.mainwindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.viewcontroller.UIControl;

import java.io.IOException;

/**
 * Класс-контроллер для PropertiesPanel.fxml
 */
public class PropertiesPanelController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesPanelController.class.getName());
    @FXML
    private ScrollPane taskPropertiesScrollPane;
    @FXML
    private ScrollPane projectPropertiesScrollPane;
    @FXML
    private ScrollPane resourcePropertiesScrollPane;

    @FXML
    private Tab projectPropertiesTab;
    @FXML
    private Tab taskPropertiesTab;
    @FXML
    private Tab resourcePropertiesTab;

    private UIControl uiControl;

    public PropertiesPanelController() {
    }

    @FXML
    private void initialize() {
        projectPropertiesTab.setGraphic(new ImageView(getClass().getResource("/img/ic_project.png").toExternalForm()));
        taskPropertiesTab.setGraphic(new ImageView(getClass().getResource("/img/ic_task.png").toExternalForm()));
        resourcePropertiesTab.setGraphic(new ImageView(getClass().getResource("/img/ic_resource.png").toExternalForm()));
    }

    /**
     * Метод инициализирует вкладки "Проект", "Задача", "Ресурс".
     */
    public void initTabs() {
        initProjectPropertiesPane();
        initTaskPropertiesPane();
        initResourcePropertiesPane();
    }

    public void initResourcePropertiesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResourcePropertiesPane.fxml"));
            loader.setResources(UIControl.bundle);
            VBox resourceProperties = loader.load();
//            resourcePropertiesTab.setContent(resourceProperties);
            resourcePropertiesScrollPane.setContent(resourceProperties);

            ResourcePropertiesPaneController resourcePropertiesPaneController = loader.getController();
            resourcePropertiesPaneController.initFields(uiControl);
            resourcePropertiesPaneController.initAssignmentTaskTable(uiControl);
            resourcePropertiesPaneController.setListeners(uiControl);
            LOGGER.debug("Вкладка \"Ресурс\" загружена.");
        } catch (IOException e) {
            LOGGER.debug("IOException ", e);
        }
    }

    public void initTaskPropertiesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskPropertiesPane.fxml"));
            loader.setResources(UIControl.bundle);
            VBox taskProperties = loader.load();
//            taskPropertiesTab.setContent(taskProperties);
            taskPropertiesScrollPane.setContent(taskProperties);

            TaskPropertiesPaneController taskPropertiesPaneController = loader.getController();
            taskPropertiesPaneController.initFields(uiControl);
            taskPropertiesPaneController.initAssignmentResourceTable(uiControl);
            taskPropertiesPaneController.setListeners(uiControl);
            LOGGER.debug("Вкладка \"Задача\" загружена.");
        } catch (IOException e) {
            LOGGER.debug("IOException ", e);
        }
    }

    public void initProjectPropertiesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectPropertiesPane.fxml"));
            loader.setResources(UIControl.bundle);
            VBox projectProperties = loader.load();
//            projectPropertiesTab.setContent(projectProperties);
            projectPropertiesScrollPane.setContent(projectProperties);

            ProjectPropertiesPaneController projectPropertiesController = loader.getController();
            projectPropertiesController.initFields(uiControl);
            LOGGER.debug("Вкладка \"Проект\" загружена.");
        } catch (IOException e) {
            LOGGER.debug("IOException ", e);
        }
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
