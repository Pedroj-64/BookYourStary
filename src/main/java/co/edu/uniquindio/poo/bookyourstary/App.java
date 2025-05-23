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
        // Delegar el guardado de datos al controlador principal
        MainController.getInstance().handleApplicationStop();
    }

    @Override
    public void init() {
        // Delegar la inicialización al controlador principal
        MainController.getInstance().handleApplicationInit();
    }

    @Override
    public void start(Stage stage) {
        // Delegar el inicio de la interfaz al controlador principal
        MainController.getInstance().handleApplicationStart(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
