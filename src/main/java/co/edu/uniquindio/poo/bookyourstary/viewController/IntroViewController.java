package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class IntroViewController {

    @FXML // fx:id="img_intro"
    private ImageView img_intro;

    @FXML
    public void initialize() {
        // Hilo para esperar y luego cambiar a Home
        new Thread(() -> {
            try {
                Thread.sleep(5000); // 5 segundos de splash
                Platform.runLater(() -> {
                    MainController.loadScene("home", 900, 600); // Cambia a la pantalla principal
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
