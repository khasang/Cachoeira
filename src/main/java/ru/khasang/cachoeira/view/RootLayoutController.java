package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import ru.khasang.cachoeira.controller.IController;

import java.util.Optional;

/**
 * Created by truesik on 25.11.2015.
 */
public class RootLayoutController {
    IController controller;

    public RootLayoutController() {

    }

    @FXML
    private void newProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка создания нового проекта
    }

    @FXML
    private void openProjectMenuItemHandle(ActionEvent actionEvent) {
        //открытие окошка выбора файла проекта (см. доки по FileChooser'у)
    }

    @FXML
    private void saveProjectMenuItemHandle(ActionEvent actionEvent) {
        //сохранение проекта
    }

    @FXML
    private void exitMenuItemHandle(ActionEvent actionEvent) {
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
        } else if (result.get() == dontSaveProjectButtonType) {
            //закрываем программу без сохранения
            System.exit(0);
        }
        //}
    }

    public void setController(IController controller) {
        this.controller = controller;
    }
}
