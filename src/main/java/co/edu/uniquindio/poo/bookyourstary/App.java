package co.edu.uniquindio.poo.bookyourstary;

import javafx.application.Application;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;

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
        // Delegar la carga del splash y la navegación a MainController para mantener App limpia
        MainController.getInstance().showSplashAndThenHome(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
