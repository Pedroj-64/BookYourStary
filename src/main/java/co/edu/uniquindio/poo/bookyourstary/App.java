package co.edu.uniquindio.poo.bookyourstary;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.bookyourstary.config.mapping.DataMapping;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;

public class App extends Application {
    @Override
    public void init() {
        // Ensure MainController singleton is initialized, though getInstance() calls should handle it.
        MainController.getInstance(); 

        DataMapping.createTestAdmin();
        DataMapping.createAllHostings(); // This now also handles city creation and persistence
        DataMapping.createTestClient();
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = MainController.loadFXML("home");
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("BookYourStary");
            stage.setScene(scene);
            stage.show();
            MainController.setScene(scene);
        } catch (Exception e) {
            MainController.showAlert(
                    "Error crítico",
                    "No se pudo iniciar la aplicación: " + e.getMessage(),
                   AlertType.ERROR);
            if (stage != null) {
                stage.close();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
