package co.edu.uniquindio.poo.bookyourstary.controller;

import java.util.UUID;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.SingUpController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import javafx.scene.control.Alert;

public class RegisterController {
    private final SingUpController singUpController = new SingUpController(
            MainController.getInstance().getCodeActivationService(),
            MainController.getInstance().getClientService());

    public boolean registerUser(Client client, String password) {
        try {
            singUpController.initiateSignUp(client, password);
            MainController.showAlert("Registro exitoso",
                    "Revise su correo electrónico para el código de activación y escríbalo en el campo correspondiente.",
                    Alert.AlertType.INFORMATION);
            return true;
        } catch (Exception e) {
            MainController.showAlert("Error de registro", e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public boolean activateUser(String code) {
        try {
            UUID uuid = UUID.fromString(code);
            singUpController.completeSignUp(uuid);
            MainController.showAlert("Activación exitosa", "Usuario activado correctamente. Puede iniciar sesión.",
                    Alert.AlertType.INFORMATION);
            MainController.loadScene("userLogin", 900, 600); // Navigate to login screen
            return true;
        } catch (Exception e) {
            MainController.showAlert("Código inválido", "El código de activación es incorrecto o ya fue usado.",
                    Alert.AlertType.ERROR);
            return false;
        }
    }
}
