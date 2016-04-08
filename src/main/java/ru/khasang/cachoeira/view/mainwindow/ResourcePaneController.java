package ru.khasang.cachoeira.view.mainwindow;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Resource;
import ru.khasang.cachoeira.model.ResourceType;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.contextmenus.ColumnContextMenu;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.GanttPlan;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.ResourceGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.rowfactories.ResourceTableViewRowFactory;
import ru.khasang.cachoeira.view.mainwindow.tables.ResourceTableView;


/**
 * Класс в котором описывается все что находится на вкладке Ресурсы.
 */
public class ResourcePaneController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcePaneController.class.getName());

    @FXML
    private SplitPane resourceSplitPane;
    @FXML
    private ResourceTableView<IResource> resourceTableView;         //таблица ресурсов <Resource>
    @FXML
    private TableColumn<IResource, String> resourceNameColumn;      //стоблец с наименованием ресурса <Resource, String>
    @FXML
    private TableColumn<IResource, ResourceType> resourceTypeColumn;      //столбец с типом ресурса <Resource, String>
    @FXML
    private TableColumn<IResource, String> resourceEmailColumn;
    @FXML
    private ScrollBar resourceTableViewHorizontalScrollBar;
    @FXML
    private Button addNewResourceButton;
    @FXML
    private Button removeResourceButton;
    @FXML
    private Slider zoomSlider;

    private GanttPlan ganttPlan;
    private UIControl uiControl;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<IResource> selectedItemChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IResource> resourceListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;

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
        resourceNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());                     //столбец ресурсов Наименование
        resourceTypeColumn.setCellValueFactory(cellData -> cellData.getValue().resourceTypeProperty());                   //Тип
        resourceEmailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());                 //Почта
        // Высота строк и выравнивание
        resourceTableView.setFixedCellSize(31);
        resourceNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceTypeColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceEmailColumn.setStyle("-fx-alignment: CENTER-LEFT");

    }

    @FXML
    private void addNewResourceHandle(ActionEvent actionEvent) {
        uiControl.getController().handleAddResource(new Resource());
    }

    @FXML
    private void removeResourceHandle(ActionEvent actionEvent) {
        uiControl.getController().handleRemoveResource(resourceTableView.getSelectionModel().getSelectedItem());
    }

    public void initResourceTable(UIControl uiControl) {
        resourceTableView.bindScrollsToController(uiControl);
        resourceTableView.setItems(uiControl.getController().getProject().getResourceList());
        resourceTableView.setRowFactory(new ResourceTableViewRowFactory(this, uiControl.getController())); //вешаем драг и дроп, и контекстное меню
        // Временное решение для синхронизации таблицы и диаграммы.
        // Добавил собственный горизонтальный скролл за вместо скролла таблицы (который скрыл, см. ResourceTableView),
        // чтобы он был всегда видимый, пока не придумаю более изящное решение.
        resourceTableViewHorizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
        resourceTableViewHorizontalScrollBar.visibleAmountProperty().bind(resourceTableView.widthProperty());
        resourceTableViewHorizontalScrollBar.valueProperty().bindBidirectional(uiControl.resourceHorizontalScrollValueProperty());
        LOGGER.debug("Таблица ресурсов проинициализирована.");
    }

    public void setListeners(UIControl uiControl) {
        selectedItemChangeListener = (observable, oldValue, newValue) -> uiControl.getController().setSelectedResource(newValue);
        resourceListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(resource -> resourceTableView.getSelectionModel().select(resource));
                }
            }
        };
        taskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    ganttPlan.getObjectsLayer().refreshPlan(uiControl);
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(task -> ganttPlan.getObjectsLayer().removeTaskBar(task));
                }
            }
        };

        // Следим за выделенным элементом в таблице задач
        resourceTableView.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<>(selectedItemChangeListener));
        uiControl.getController().getProject().getResourceList().addListener(new WeakListChangeListener<>(resourceListChangeListener));
        // Следим за изменениями в модели задач
        uiControl.getController().getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));
    }

    public void initGanttChart(UIControl uiControl) {
        ganttPlan = new ResourceGanttPlan();
        ganttPlan.initGanttDiagram(uiControl);
        resourceSplitPane.getItems().add(ganttPlan);
        resourceSplitPane.setDividerPosition(0, 0.3);
        //Связываем разделитель таблицы и диаграммы на вкладке Задачи с разделителем на вкладке Ресурсы
        resourceSplitPane.getDividers().get(0).positionProperty().bindBidirectional(uiControl.splitPaneDividerValueProperty());
        LOGGER.debug("Диаграмма Ганта проинициализирована.");
    }

    public void initContextMenus(UIControl uiControl) {
        // Контекстное меню для выбора нужных столбцов
        ColumnContextMenu columnContextMenuResource = new ColumnContextMenu(resourceTableView);
        columnContextMenuResource.setOnShowing(event -> columnContextMenuResource.updateContextMenuColumnTV(resourceTableView));
        resourceTableView.getColumns().forEach(column -> column.setContextMenu(columnContextMenuResource));

        // Контекстное меню для таблицы
        ContextMenu resourceTableMenu = new ContextMenu();
        MenuItem addNewResource = new MenuItem("Новый ресурс");
        addNewResource.setOnAction(event -> uiControl.getController().handleAddResource(new Resource()));
        resourceTableMenu.getItems().addAll(addNewResource);   //заполняем меню
        resourceTableView.setContextMenu(resourceTableMenu);
    }

    public void initZoom(UIControl uiControl) {
        zoomSlider.valueProperty().bindBidirectional(uiControl.zoomMultiplierProperty());
    }

    public TableView<IResource> getResourceTableView() {
        return resourceTableView;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }

    public GanttPlan getGanttPlan() {
        return ganttPlan;
    }
}