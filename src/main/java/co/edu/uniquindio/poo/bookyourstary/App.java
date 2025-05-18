package co.edu.uniquindio.poo.bookyourstary;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        try {
            scene = new Scene(MainController.loadFXML("home"), 600, 340);
            stage.setScene(scene);
            stage.show();
            MainController.setScene(scene);
        } catch (IOException e) {
            MainController.showAlert("Error al cargar la interfaz", "No se pudo cargar el archivo FXML: " + e.getMessage(),
                    AlertType.ERROR);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}