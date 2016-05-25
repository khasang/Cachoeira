package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.AbstractPropertiesTab;

public class PropertiesSidePanel extends AbstractPropertiesSidePanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesSidePanel.class.getName());

    private final AbstractPropertiesTab projectPropertiesTab;
    private final AbstractPropertiesTab taskPropertiesTab;
    private final AbstractPropertiesTab resourcePropertiesTab;

    public PropertiesSidePanel(AbstractPropertiesTab projectPropertiesTab,
                               AbstractPropertiesTab taskPropertiesTab,
                               AbstractPropertiesTab resourcePropertiesTab) {
        this.projectPropertiesTab = projectPropertiesTab;
        this.taskPropertiesTab = taskPropertiesTab;
        this.resourcePropertiesTab = resourcePropertiesTab;
    }

    @Override
    public void createPanes() {
        this.getTabs().addAll(projectPropertiesTab, taskPropertiesTab, resourcePropertiesTab);
        this.setPrefWidth(310);
        this.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        LOGGER.debug("Created.");
    }

    public AbstractPropertiesTab getProjectPropertiesTab() {
        return projectPropertiesTab;
    }

    public AbstractPropertiesTab getTaskPropertiesTab() {
        return taskPropertiesTab;
    }

    public AbstractPropertiesTab getResourcePropertiesTab() {
        return resourcePropertiesTab;
    }
}
