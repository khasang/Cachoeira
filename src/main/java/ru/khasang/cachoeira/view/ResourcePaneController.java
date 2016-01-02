package ru.khasang.cachoeira.view;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.view.contextmenus.ContextMenuColumn;
import ru.khasang.cachoeira.view.rowfactories.ResourceTableViewRowFactory;


/**
 * Класс в котором описывается все что находится на вкладке Ресурсы.
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
    @FXML
    private Button addNewResourceButton;
    @FXML
    private Button removeResourceButton;
    @FXML
    private Slider zoomSlider;

    private ResourceGanttChart resourceGanttChart;
    private IController controller;
    private UIControl uiControl;

    public ResourcePaneController() {
    }

    /**
     * метод initialize исполняется после загрузки fxml файла
     */
    @FXML
    private void initialize() {
        // Если элемент в таблице не выбран, то кнопка не активна
        removeResourceButton.disableProperty().bind(resourceTableView.getSelectionModel().selectedItemProperty().isNull());
        // Вешаем иконки на кнопки
        addNewResourceButton.setGraphic(new ImageView(getClass().getResource("/img/ic_add.png").toExternalForm()));
        removeResourceButton.setGraphic(new ImageView(getClass().getResource("/img/ic_remove.png").toExternalForm()));
        // Уменьшаем толщину разделителя
        resourceSplitPane.getStylesheets().add(this.getClass().getResource("/css/split-pane.css").toExternalForm());
        // Привязываем столбцы к полям в модели
        resourceNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());                     //столбец ресурсов Наименование
        resourceTypeColumn.setCellValueFactory(param -> param.getValue().resourceTypeProperty());                   //Тип
        resourceEmailColumn.setCellValueFactory(param -> param.getValue().emailProperty());                 //Почта
        // Делаем поля таблицы редактируемыми
        resourceTableView.setEditable(true);
        resourceNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        resourceTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(ResourceType.values()));
        resourceEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // Высота строк и выравнивание
        resourceTableView.setFixedCellSize(31);
        resourceNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceTypeColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceEmailColumn.setStyle("-fx-alignment: CENTER-LEFT");
    }

    @FXML
    private void addNewResourceHandle(ActionEvent actionEvent) {
        controller.handleAddResource(new Resource());
    }

    @FXML
    private void removeResourceHandle(ActionEvent actionEvent) {
        controller.handleRemoveResource(resourceTableView.getSelectionModel().getSelectedItem());
    }

    public void initResourceTable() {
        resourceTableView.setItems(controller.getProject().getResourceList());
        resourceTableView.setRowFactory(new ResourceTableViewRowFactory(this, controller)); //вешаем драг и дроп, и контекстное меню
        resourceTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            controller.selectedResourceProperty().setValue(newValue);
        });
        /** Следим за изменениями в модели задач **/
        controller.getProject().getResourceList().addListener((ListChangeListener<IResource>) c -> {
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
        resourceGanttChart = new ResourceGanttChart();
        resourceGanttChart.initGanttDiagram(uiControl);
        resourceSplitPane.getItems().add(resourceGanttChart);
        resourceSplitPane.setDividerPosition(0, 0.3);
        //Связываем разделитель таблицы и диаграммы на вкладке Задачи с разделителем на вкладке Ресурсы
        resourceSplitPane.getDividers().get(0).positionProperty().bindBidirectional(uiControl.splitPaneDividerValueProperty());

        controller.getProject().getTaskList().addListener((ListChangeListener<ITask>) change -> {
            while (change.next()) {
                for (ITask task : change.getRemoved()) {
                    resourceGanttChart.getResourcePaneObjectsLayer().removeTaskBar(task);
                }
            }
        });
    }

    public void initContextMenus() {
        // Контекстное меню для выбора нужных столбцов
        ContextMenuColumn contextMenuColumnResource = new ContextMenuColumn(resourceTableView);
        contextMenuColumnResource.setOnShowing(event -> contextMenuColumnResource.updateContextMenuColumnTV(resourceTableView));
        for (int i = 0; i < resourceTableView.getColumns().size(); i++) {
            resourceTableView.getColumns().get(i).setContextMenu(contextMenuColumnResource);
        }

        // Контекстное меню для таблицы
        ContextMenu resourceTableMenu = new ContextMenu();
        MenuItem addNewResource = new MenuItem("Новый ресурс");
        addNewResource.setOnAction(event -> controller.handleAddResource(new Resource()));
        resourceTableMenu.getItems().addAll(addNewResource);   //заполняем меню
        resourceTableView.setContextMenu(resourceTableMenu);
    }

    public void initZoom() {
        zoomSlider.valueProperty().bindBidirectional(uiControl.zoomMultiplierProperty());
    }

    public TableView<IResource> getResourceTableView() {
        return resourceTableView;
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }

    public ResourceGanttChart getResourceGanttChart() {
        return resourceGanttChart;
    }
}