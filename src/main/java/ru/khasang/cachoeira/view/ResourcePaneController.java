package ru.khasang.cachoeira.view;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.view.rowfactories.ResourceTableViewRowFactory;


/**
 * Created by truesik on 25.11.2015.
 */
public class ResourcePaneController {
    @FXML
    private SplitPane resourceSplitPane;

    @FXML
    private TableView<IResource> resourceTableView;         //таблица ресурсов <Resource>
    @FXML
    private TableColumn<IResource, String> resourceNameColumn;      //стоблец с наименованием ресурса <Resource, String>
    @FXML
    private TableColumn<IResource, ResourceType> resourceTypeColumn;      //столбец с типом ресурса <Resource, String>
    @FXML
    private TableColumn<IResource, String> resourceEmailColumn;

    private GanttChart resourceGanttChart;
    private IController controller;

    public ResourcePaneController() {
    }

    public void initResourceTable() {
        resourceTableView.setItems(controller.getProject().getResourceList());
        resourceTableView.setRowFactory(new ResourceTableViewRowFactory(this, controller)); //вешаем драг и дроп, и контекстное меню
        resourceTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            controller.selectedResourceProperty().setValue(newValue);
        });
        controller.getProject().getResourceList().addListener((ListChangeListener<IResource>) c -> {
            resourceGanttChart.getObjectsLayer().refreshResourceDiagram();
            while (c.next()) {
                if (c.wasAdded()) {
                    System.out.println("Main Window Resource Added!");
                    resourceTableView.getSelectionModel().selectLast();
                }
                if (c.wasRemoved()) {
                    System.out.println("Main Window Resource Removed");
                }
                if (c.wasReplaced()) {
                    System.out.println("Main Window Resource Replaced");
                }
                if (c.wasUpdated()) {
                    System.out.println("Main Window Resource Updated");
                }
            }
        });
    }

    public void initGanttChart() {
        resourceGanttChart = new GanttChart(controller, 70);
        resourceSplitPane.getItems().add(resourceGanttChart);
        resourceSplitPane.setDividerPosition(0, 0.3);
    }

    public void initContextMenus() {
        ContextMenuColumn contextMenuColumnResource = new ContextMenuColumn(resourceTableView);
        contextMenuColumnResource.setOnShowing(event -> contextMenuColumnResource.updateContextMenuColumnTV(resourceTableView));
        for (int i = 0; i < resourceTableView.getColumns().size(); i++) {
            resourceTableView.getColumns().get(i).setContextMenu(contextMenuColumnResource);
        }

        ContextMenu resourceTableMenu = new ContextMenu();
        MenuItem addNewResource = new MenuItem("Новый ресурс");
        addNewResource.setOnAction(event -> controller.handleAddResource(new Resource()));
        resourceTableMenu.getItems().addAll(addNewResource);   //заполняем меню
        resourceTableView.setContextMenu(resourceTableMenu);
    }

    @FXML
    private void initialize() {
        resourceNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());                     //столбец ресурсов Наименование
        resourceTypeColumn.setCellValueFactory(param -> param.getValue().resourceTypeProperty());                   //Тип
        resourceEmailColumn.setCellValueFactory(param -> param.getValue().emailProperty());                 //Почта
    }

    @FXML
    private void addNewResourceHandle(ActionEvent actionEvent) {
        controller.handleAddResource(new Resource());
    }

    public TableView<IResource> getResourceTableView() {
        return resourceTableView;
    }

    public void setController(IController controller) {
        this.controller = controller;
    }
}
