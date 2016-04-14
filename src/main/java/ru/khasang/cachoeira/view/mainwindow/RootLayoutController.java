package ru.khasang.cachoeira.view.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.exit.OnClose;
import ru.khasang.cachoeira.view.mainwindow.exit.OnCloseMainWindow;

import java.io.File;
import java.util.List;

/**
 * Класс-контроллер для {@link /fxml/RootLayout.fxml}
 */
public class RootLayoutController {
    private static final Logger logger = LoggerFactory.getLogger(RootLayoutController.class.getName());
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
        // После сохранения очищаем списки анду и реду
        CommandControl.getInstance().clearLists();
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
            // После сохранения очищаем списки анду и реду
            CommandControl.getInstance().clearLists();
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
        OnClose onClose = new OnCloseMainWindow(uiControl);
        onClose.saveProperties();
        onClose.saveProject();
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
