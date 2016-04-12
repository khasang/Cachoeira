package ru.khasang.cachoeira.view.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.view.UIControl;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Класс-контроллер для {@link /fxml/RootLayout.fxml}
 */
public class RootLayoutController {
    private static final Logger logger = LoggerFactory.getLogger(RootLayoutController.class.getName());
    private IController controller;
    private UIControl uiControl;

    public RootLayoutController() {
    }

    @FXML
    private void newProjectMenuItemHandle(ActionEvent actionEvent) {
        logger.info("Нажата кнопка меню \"Создать\".");
        //открытие окошка создания нового проекта
    }

    @FXML
    private void openProjectMenuItemHandle(ActionEvent actionEvent) {
        logger.info("Нажата кнопка меню \"Открыть\".");
        //открытие окошка выбора файла проекта (см. доки по FileChooser'у)
    }

    @FXML
    private void saveProjectMenuItemHandle(ActionEvent actionEvent) {
        logger.info("Нажата кнопка меню \"Сохранить\".");
        //сохранение проекта
        DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
        storeInterface.saveProjectToFile(uiControl.getFile(), uiControl.getController().getProject());
        storeInterface.saveTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
        storeInterface.saveResourcesToFile(uiControl.getFile(), uiControl.getController().getProject());
        storeInterface.saveParentTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
        storeInterface.saveChildTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
        storeInterface.saveResourcesByTask(uiControl.getFile(), uiControl.getController().getProject());
    }

    @FXML
    public void saveProjectAsMenuItemHandle(ActionEvent actionEvent) {
        DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CACH", "*.cach"));
        File file = fileChooser.showSaveDialog(uiControl.getMainWindow().getStage());
        if (file != null) {
            uiControl.setFile(file);
            storeInterface.createProjectFile(file.getPath(), uiControl.getController().getProject());
            storeInterface.saveProjectToFile(uiControl.getFile(), uiControl.getController().getProject());
            storeInterface.saveTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
            storeInterface.saveResourcesToFile(uiControl.getFile(), uiControl.getController().getProject());
            storeInterface.saveParentTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
            storeInterface.saveChildTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
            storeInterface.saveResourcesByTask(uiControl.getFile(), uiControl.getController().getProject());
        }
    }

    @FXML
    private void resourceExportHandle(ActionEvent actionEvent) {
        DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CRES", "*.cres"));
        File file = fileChooser.showSaveDialog(uiControl.getMainWindow().getStage());
        if (file != null) {
            storeInterface.createResourceExportFile(file);
            storeInterface.saveResourcesToFile(file, uiControl.getController().getProject());
        }
    }

    @FXML
    private void resourceImportHandle(ActionEvent actionEvent) {
        DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CRES", "*.cres"));
        File file = fileChooser.showOpenDialog(uiControl.getMainWindow().getStage());
        if (file != null) {
            List<IResource> resourceListFromFile = storeInterface.getResourceListFromFile(file);
            uiControl.getController().getProject().getResourceList().addAll(resourceListFromFile);
        }
    }

    @FXML
    private void exitMenuItemHandle(ActionEvent actionEvent) {
        logger.info("Нажата кнопка меню \"Выход\".");
        //если произошли изменения в проекте: открытие диалогового окошка "Сохранить проект? Да Нет Отмена"
        onClose();
    }

    @FXML
    private void undoMenuItemHandle(ActionEvent actionEvent) {
        CommandControl.getInstance().undo();
    }

    @FXML
    private void redoMenuItemHandle(ActionEvent actionEvent) {
        CommandControl.getInstance().redo();
    }

    private void onClose() {
        //минимум JDK 8u40
        //if (произошли изменения в проекте) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cachoeira");
        alert.setHeaderText("Вы хотите сохранить изменения в " + controller.getProject().getName() + "?");

        ButtonType saveProjectButtonType = new ButtonType("Сохранить");
        ButtonType dontSaveProjectButtonType = new ButtonType("Не сохранять");
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveProjectButtonType, dontSaveProjectButtonType, cancelButtonType);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == saveProjectButtonType) {
            //сохранение
            saveUIValues();
        } else if (result.get() == dontSaveProjectButtonType) {
            //закрываем программу без сохранения
            saveUIValues();
            System.exit(0);
        }
        //}
    }

    private void saveUIValues() {
//        ISettingsManager settingsDAO = SettingsManager.getInstance();
//        settingsDAO.writeUIValues(uiControl.getSplitPaneDividerValue(), uiControl.getZoomMultiplier());
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
