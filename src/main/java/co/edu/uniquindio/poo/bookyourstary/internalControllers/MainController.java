package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class MainController {

    private static MainController instance;

   
    private MainController() {}

    
    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    


    public Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

   
    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

   
    public void loadScene(String fxml, double width, double height) {
        try {
            Parent root = loadFXML(fxml);
            // Aquí puedes usar el Stage principal para cambiar la escena, ejemplo:
            // App.getStage().setScene(new Scene(root, width, height));
        } catch (IOException e) {
            showAlert("Error al cambiar la vista", "No se pudo cargar el archivo FXML: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

   
    public void showAlertAndRedirect(String title, String message, Alert.AlertType type, String fxml,
            double width, double height) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(evt -> loadScene(fxml, width, height));
        alert.show();
    }
}
