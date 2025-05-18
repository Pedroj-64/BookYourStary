package co.edu.uniquindio.poo.bookyourstary.util.viewDinamic;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class ViewLoader {

    public static void setContent(AnchorPane container, String fxmlPath) {
        try {
            MainController.getInstance();
            Parent content = (Parent) MainController.loadFXML(fxmlPath.replace(".fxml", ""));
            container.getChildren().setAll(content);
        } catch (Exception e) {
            MainController.getInstance();
            MainController.showAlert(
                "Error al cargar vista",
                "No se pudo cargar el archivo FXML: " + fxmlPath + "\n" + e.getMessage(),
                javafx.scene.control.Alert.AlertType.ERROR
            );
        }
    }

}
