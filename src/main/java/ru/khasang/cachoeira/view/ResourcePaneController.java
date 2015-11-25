package ru.khasang.cachoeira.view;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
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
    private UIControl UIControl;
    private IController controller;

    public ResourcePaneController() {
        resourceGanttChart = new GanttChart(controller, UIControl, 70);
        resourceSplitPane.getItems().add(resourceGanttChart);
        resourceSplitPane.setDividerPosition(0, 0.3);

        controller.getProject().getResourceList().addListener(new ListChangeListener<IResource>() {
            @Override
            public void onChanged(Change<? extends IResource> c) {
                resourceGanttChart.getObjectsLayer().refreshResourceDiagram();
                while (c.next()) {
                    if (c.wasAdded()) {
                        System.out.println("Main Window Resource Added!");
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
            }
        });

        resourceTableView.setItems(controller.getProject().getResourceList());

        resourceNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());                     //столбец ресурсов Наименование
        resourceTypeColumn.setCellValueFactory(param -> param.getValue().resourceTypeProperty());                   //Тип
        resourceEmailColumn.setCellValueFactory(param -> param.getValue().emailProperty());                   //Почта

        ContextMenuColumn contextMenuColumnResource = new ContextMenuColumn(resourceTableView);
        contextMenuColumnResource.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                contextMenuColumnResource.updateContextMenuColumnTV(resourceTableView);
            }
        });
        for (int i = 0; i < resourceTableView.getColumns().size(); i++) {
            resourceTableView.getColumns().get(i).setContextMenu(contextMenuColumnResource);
        }

        ContextMenu resourceTableMenu = new ContextMenu();
        MenuItem addNewResource = new MenuItem("Новый ресурс");
        addNewResource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                openNewResourceWindow();
            }
        });
        resourceTableMenu.getItems().addAll(addNewResource);   //заполняем меню
        resourceTableView.setContextMenu(resourceTableMenu);

        resourceTableView.setRowFactory(new ResourceTableViewRowFactory(this, controller)); //вешаем драг и дроп, и контекстное меню
    }

    @FXML
    private void addNewResourceHandle(ActionEvent actionEvent) {
        //открытие окошка добавления нового ресурса с помощью кнопки +
//        openNewResourceWindow();
    }

    public TableView<IResource> getResourceTableView() {
        return resourceTableView;
    }
}
