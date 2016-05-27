package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers;

import javafx.scene.Node;
import ru.khasang.cachoeira.view.mainwindow.properties.AbstractPropertiesSidePanel;
import ru.khasang.cachoeira.view.mainwindow.properties.PropertiesSidePanel;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.*;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.ProjectPropertiesTab;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.ResourcePropertiesTab;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.TaskPropertiesTab;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules.*;

public class PropertiesSidePanelController {
    private final MainWindowController controller;
    private AbstractPropertiesSidePanel sidePanel;

    public PropertiesSidePanelController(MainWindowController controller) {
        this.controller = controller;
    }

    public void createSidePanel() {
        //
        sidePanel = new PropertiesSidePanel(new ProjectPropertiesTab(), new TaskPropertiesTab(), new ResourcePropertiesTab());
        //
        IModule projectInformation = new ProjectInformation();
        IModule taskInformation = new TaskInformation();
        IModule taskAssignedResources = new TaskAssignedResources();
        IModule taskDependencies = new TaskDependencies();
        IModule resourceInformation = new ResourceInformation();
        IModule resourceAssignedTasks = new ResourceAssignedTasks();
        //
        sidePanel.getProjectPropertiesTab().addNewPropertiesTitledModule((Node) projectInformation, "Information");
        sidePanel.getTaskPropertiesTab().addNewPropertiesTitledModule((Node) taskInformation, "Information");
        sidePanel.getTaskPropertiesTab().addNewPropertiesTitledModule((Node) taskAssignedResources, "Assigned Resources");
        sidePanel.getTaskPropertiesTab().addNewPropertiesTitledModule((Node) taskDependencies, "Dependencies");
        sidePanel.getResourcePropertiesTab().addNewPropertiesTitledModule((Node) resourceInformation, "Information");
        sidePanel.getResourcePropertiesTab().addNewPropertiesTitledModule((Node) resourceAssignedTasks, "Assigned Tasks");
        // init controller on every single module
        createModuleController(new ProjectInformationModuleController(projectInformation, controller));
        createModuleController(new TaskInformationModuleController(taskInformation, controller));
        createModuleController(new TaskAssignedResourcesModuleController(taskAssignedResources, controller));
        createModuleController(new TaskDependenciesModuleController(taskDependencies, controller));
        createModuleController(new ResourceInformationModuleController(resourceInformation, controller));
        createModuleController(new ResourceAssignedTasksModuleController(resourceAssignedTasks, controller));
    }

    private ModuleController createModuleController(ModuleController moduleController) {
        moduleController.initModule();
        return moduleController;
    }

    public AbstractPropertiesSidePanel getSidePanel() {
        return sidePanel;
    }
}
