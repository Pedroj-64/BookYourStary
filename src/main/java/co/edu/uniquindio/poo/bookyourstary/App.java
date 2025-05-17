package co.edu.uniquindio.poo.bookyourstary;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.PrincipalController;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        try {
            scene = new Scene(PrincipalController.loadFXML("home"), 600, 340);
            stage.setScene(scene);
            stage.show();
            PrincipalController.setScene(scene);
        } catch (IOException e) {
            PrincipalController.showAlert("Error al cargar la interfaz", "No se pudo cargar el archivo FXML: " + e.getMessage(),
                    javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}