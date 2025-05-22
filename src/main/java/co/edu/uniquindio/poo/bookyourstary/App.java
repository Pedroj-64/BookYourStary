package co.edu.uniquindio.poo.bookyourstary;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.bookyourstary.config.mapping.DataMapping;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;
import co.edu.uniquindio.poo.bookyourstary.util.ApartmentCreator;

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
        // Ensure MainController singleton is initialized, though getInstance() calls should handle it.
        MainController.getInstance(); 
        
        // Always create test data first to ensure app has basic data to work with
        System.out.println("Creando datos de prueba iniciales...");
        DataMapping.createTestAdmin();
        DataMapping.createAllHostings(); // This now also handles city creation and persistence
        ApartmentCreator.createApartments(); // Crear apartamentos usando nuestra utilidad independiente
        DataMapping.createTestClient();
        
        // Then try to load data from XML, which might override the test data
        try {
            XmlSerializationManager xmlManager = XmlSerializationManager.getInstance();
            
            // Only try to load if data files already exist
            if (xmlManager.hasStoredData()) {
                xmlManager.loadAllData();
                System.out.println("Datos cargados correctamente desde XML.");
            } else {
                System.out.println("No se encontraron archivos XML previos, se usarán los datos de prueba.");
                // Save initial data to XML
                xmlManager.saveAllData();
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
