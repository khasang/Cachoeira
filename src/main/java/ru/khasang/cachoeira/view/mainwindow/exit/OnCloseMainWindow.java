package ru.khasang.cachoeira.view.mainwindow.exit;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.data.ISettingsManager;
import ru.khasang.cachoeira.data.SettingsManager;
import ru.khasang.cachoeira.view.UIControl;

public class OnCloseMainWindow implements OnClose {
    private UIControl uiControl;
    private Stage stage;

    public OnCloseMainWindow(UIControl uiControl) {
        this.uiControl = uiControl;
        this.stage = uiControl.getMainWindow().getStage();
    }

    @Override
    public void saveProperties() {
        ISettingsManager settingsManager = SettingsManager.getInstance();
        settingsManager.writeUIValues(
                uiControl.getSplitPaneDividerValue(),
                uiControl.getZoomMultiplier(),
                stage.getWidth(),
                stage.getHeight(),
                stage.isMaximized());
    }

    @Override
    public void saveProject() {
        //минимум JDK 8u40
        if (CommandControl.getInstance().isChanged()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cachoeira");
            alert.setHeaderText("Вы хотите сохранить изменения в " + uiControl.getController().getProject().getName() + "?");

            ButtonType saveProjectButtonType = new ButtonType("Сохранить");
            ButtonType dontSaveProjectButtonType = new ButtonType("Не сохранять");
            ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveProjectButtonType, dontSaveProjectButtonType, cancelButtonType);

            alert.showAndWait().ifPresent(response -> {
                if (response == saveProjectButtonType) {
                    DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
                    storeInterface.saveProjectToFile(uiControl.getFile(), uiControl.getController().getProject());
                    storeInterface.saveTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
                    storeInterface.saveResourcesToFile(uiControl.getFile(), uiControl.getController().getProject());
                    storeInterface.saveParentTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
                    storeInterface.saveChildTasksToFile(uiControl.getFile(), uiControl.getController().getProject());
                    storeInterface.saveResourcesByTask(uiControl.getFile(), uiControl.getController().getProject());

                    System.exit(0);
                }
                if (response == dontSaveProjectButtonType) {
                    System.exit(0);
                }
            });
        } else {
            System.exit(0);
        }
    }
}
