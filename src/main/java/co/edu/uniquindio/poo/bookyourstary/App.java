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
        // Cargar el controlador principal
        MainController.getInstance();
        
        try {
            XmlSerializationManager xmlManager = XmlSerializationManager.getInstance();
            
            // Si hay datos almacenados, intentar cargarlos
            if (xmlManager.hasStoredData()) {
                try {
                    xmlManager.loadAllData();
                    System.out.println("Datos cargados correctamente desde XML.");
                    
                    // Verificar/actualizar datos esenciales
                    DataMapping.createTestAdmin();
                    DataMapping.createTestClient();
                    
                    // Guardar cambios si se hicieron actualizaciones
                    xmlManager.saveAllData();
                    System.out.println("Cuentas de prueba verificadas/actualizadas y guardadas en XML.");
                } catch (Exception e) {
                    System.err.println("Error cargando datos existentes, recreando datos de prueba: " + e.getMessage());
                    recreateTestData(xmlManager);
                }
            } else {
                // No hay datos previos, crear datos de prueba
                System.out.println("No se encontraron archivos XML previos, creando datos de prueba...");
                recreateTestData(xmlManager);
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }    private void recreateTestData(XmlSerializationManager xmlManager) {
        try {
            // Limpiar repositorios antes de recrear los datos
            MainController.getInstance().getHostingService().clearAll();
            MainController.getInstance().getClientRepository().clearAll();
            MainController.getInstance().getAdminRepository().clearAll();
            MainController.getInstance().getBookingRepository().clearAll();
            MainController.getInstance().getCityService().clearAll();
            
            // Crear datos de prueba
            DataMapping.createTestAdmin();
            DataMapping.createAllHostings();
            DataMapping.createTestClient();
            
            // Guardar los nuevos datos
            xmlManager.saveAllData();
            System.out.println("Datos de prueba creados y guardados en XML.");
        } catch (Exception e) {
            System.err.println("Error al recrear datos de prueba: " + e.getMessage());
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
