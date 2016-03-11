package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.data.ISettingsDAO;
import ru.khasang.cachoeira.data.SettingsDAO;

import java.util.Optional;

/**
 * Класс-контроллер для {@link /fxml/RootLayout.fxml}
 */
public class RootLayoutController {
    private static final Logger logger = LoggerFactory.getLogger(RootLayoutController.class.getName());
    IController controller;
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
    }

    @FXML
    private void exitMenuItemHandle(ActionEvent actionEvent) {
        logger.info("Нажата кнопка меню \"Выход\".");
        //если произошли изменения в проекте: открытие диалогового окошка "Сохранить проект? Да Нет Отмена"
        onClose();
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
//        ISettingsDAO settingsDAO = SettingsDAO.getInstance();
//        settingsDAO.writeUIValues(uiControl.getSplitPaneDividerValue(), uiControl.getZoomMultiplier());
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
