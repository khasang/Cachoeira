package ru.khasang.cachoeira;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by truesik on 28.09.2015.
 */
public class MainWindow {
    @FXML
    private TreeTableView taskTreeTableView;     //таблица задач <Task>
    @FXML
    private TreeTableColumn taskNameColumn;      //столбец с наименованием задачи <Task, String>
    @FXML
    private TreeTableColumn finishDateColumn;    //столбец с датой окончания задачи <Task, Date>
    @FXML
    private TreeTableColumn startDateColumn;     //столбец с датой начала задачи <Task, Date>
    @FXML
    private ScrollPane taskGanttScrollPane;      //здесь должен быть канвас, также возможна с помощью этого скролла получится синхронизировать вертикальные скроллы таблицы задач и ганта
    @FXML
    private TableView resourceTableView;         //таблица ресурсов <Resource>
    @FXML
    private TableColumn resourceNameColumn;      //стоблец с наименованием ресурса <Resource, String>
    @FXML
    private TableColumn resourceTypeColumn;      //столбец с типом ресурса <Resource, String>
    @FXML
    private ScrollPane resourceGanttScrollPane;  //здесь должен быть канвас, также возможна с помощью этого скролла получится синхронизировать вертикальные скроллы таблицы ресурсов и ганта

    private Parent root = null;
    private Stage stage;

//    ObservableList taskTableModel = FXCollections.observableArrayList();        //<Task> модель для задач
//    ObservableList resourceTableModel = FXCollections.observableArrayList();    //<Resource> модель для ресурсов

    public MainWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));  //грузим макет окна
        fxmlLoader.setController(this);                                                     //говорим макету, что этот класс является его контроллером
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launch() {
        stage = new Stage();
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Cachoeira");
        stage.show();

        //при нажатии на крестик в тайтле
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
//                onClose();
//              if (произошли изменения в проекте) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cachoeira");
                alert.setHeaderText("Вы хотите сохранить изменения в Имя проекта?");

                ButtonType saveProjectButtonType = new ButtonType("Сохранить");
                ButtonType dontSaveProjectButtonType = new ButtonType("Не сохранять");
                ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(saveProjectButtonType, dontSaveProjectButtonType, cancelButtonType);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == saveProjectButtonType) {
                    //сохранение
                } else if (result.get() == dontSaveProjectButtonType) {
                    //закрываем программу без сохранения
                    System.exit(0);
                } else if (result.get() == cancelButtonType) {
                    event.consume(); //отмена закрытия окна
                }
                //}
            }
        });

        //нужно заполнить таблицы элементами
//        resourceTableView.setItems(resourceTableModel);
//        taskTreeTableView...

        //контекстные меню в списках задач и ресурсов
//        taskTreeTableView.setRowFactory(new Callback<TreeTableView, TreeTableRow>() {
//            @Override
//            public TreeTableRow call(TreeTableView param) {
//                TreeTableRow row = new TreeTableRow();
//                ContextMenu rowMenu = new ContextMenu();
//                MenuItem addNewResource = new MenuItem("Новый ресурс");
//                addNewResource.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        //open add resource dialog
//                        TaskWindow taskWindow = new TaskWindow();
//                        taskWindow.launch();
//                    }
//                });
//                rowMenu.getItems().addAll(addNewResource);
//                row.contextMenuProperty().bind(Bindings.when(Bindings.isNull(row.itemProperty()))
//                        .then(rowMenu)
//                        .otherwise((ContextMenu) null));
//                return row;
//            }
//        });
//        resourceTableView.setRowFactory(new Callback<TableView, TableRow>() {
//            @Override
//            public TableRow call(TableView param) {
//                TableRow row = new TableRow();
//                ContextMenu rowMenu = new ContextMenu();
//                MenuItem addNewResource = new MenuItem("Новый ресурс");
//                addNewResource.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        //open add resource dialog
//                    }
//                });
//                rowMenu.getItems().addAll(addNewResource);
//                row.contextMenuProperty().bind(Bindings.when(Bindings.isNull(row.itemProperty()))
//                        .then(rowMenu)
//                        .otherwise((ContextMenu) null));
//                return row;
//            }
//        });
    }

    private void onClose() {
        //минимум JDK 8u40
        //if (произошли изменения в проекте) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cachoeira");
        alert.setHeaderText("Вы хотите сохранить изменения в Имя проекта?");

        ButtonType saveProjectButtonType = new ButtonType("Сохранить");
        ButtonType dontSaveProjectButtonType = new ButtonType("Не сохранять");
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveProjectButtonType, dontSaveProjectButtonType, cancelButtonType);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == saveProjectButtonType) {
            //сохранение
        } else if (result.get() == dontSaveProjectButtonType) {
            //закрываем программу без сохранения
            System.exit(0);
        }
        //}
    }

    //ивенты при нажании на пункты основного меню
    public void newProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка создания нового проекта
    }

    public void openProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка выбора файла проекта (см. доки по FileChooser'у)
    }

    public void saveProjectMenuItemHandle(ActionEvent actionEvent) {
        //сохранение проекта
    }

    public void exitMenuItemHandle(ActionEvent actionEvent) {
        //если произошли изменения в проекте: открытие диалогового окошка "Сохранить проект? Да Нет Отмена"
        onClose();
    }

    public void addNewResourceHandle(ActionEvent actionEvent) {
        //отрытие окошка добавления нового ресурса
    }

    public void addNewTaskHandle(ActionEvent actionEvent) {
        TaskWindow taskWindow = new TaskWindow(stage); //todo переделать на контроллер
        taskWindow.launch();
    }
}
