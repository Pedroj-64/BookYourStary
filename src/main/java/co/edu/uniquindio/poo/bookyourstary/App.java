package co.edu.uniquindio.poo.bookyourstary;

import co.edu.uniquindio.poo.bookyourstary.util.TemplateLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.bookyourstary.config.mapping.DataMapping;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;

public class App extends Application {

    @Override   
    public void stop() {
        // Guardar todos los datos al cerrar la aplicación
        try {
            XmlSerializationManager.getInstance().saveAllData();
            System.out.println("Datos guardados correctamente en XML.");
        } catch (Exception e) {
            System.err.println("Error al guardar datos en XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        TemplateLoader.listAvailableResources();
        // Ensure MainController singleton is initialized
        MainController.getInstance();
        
        try {
            XmlSerializationManager xmlManager = XmlSerializationManager.getInstance();

            if (xmlManager.hasStoredData()) {
                xmlManager.loadAllData();
                System.out.println("Datos cargados correctamente desde XML.");
            } else {
                System.out.println("No se encontraron archivos XML previos, creando datos de prueba...");
                DataMapping.createTestAdmin();
                DataMapping.createAllHostings(); // This handles all types of hostings
                DataMapping.createTestClient();
                
                xmlManager.saveAllData();
                System.out.println("Datos de prueba creados y guardados en XML.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar datos desde XML: " + e.getMessage());
            e.printStackTrace();
        }
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
