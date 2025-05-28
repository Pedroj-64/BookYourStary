package co.edu.uniquindio.poo.bookyourstary;

import javafx.application.Application;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.bookyourstary.config.mapping.DataMapping;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.util.serializacionSeria.DataManager;

public class App extends Application {
    @Override
    public void init() {
        try {
            // Inicializar MainController
            MainController.getInstance();
           
            // Intentar cargar datos serializados primero
            DataManager.getInstance().loadAllData();

            // Si no hay datos, crear datos de prueba

        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
            // Si hay error al cargar datos serializados, crear datos de prueba
            DataMapping.createAllHostings();
        }
    }

    @Override
    public void start(Stage stage) {
        MainController.getInstance().showSplashAndThenHome(stage);
    }

    @Override
    public void stop() {
        // Guardar datos antes de cerrar la aplicación
        DataManager.getInstance().saveAllData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
