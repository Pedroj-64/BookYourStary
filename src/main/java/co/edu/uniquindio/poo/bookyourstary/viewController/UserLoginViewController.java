package co.edu.uniquindio.poo.bookyourstary.viewController;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.bookyourstary.controller.LoginController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserLoginViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_Ingresar;

    @FXML
    private TextField txt_emailField;

    @FXML
    private PasswordField txt_passwordField;

    LoginController loginController = new LoginController();

    @FXML
    void handleLogin(ActionEvent event) {
        String email = txt_emailField.getText();
        String password = txt_passwordField.getText();
        loginController.handleLogin(email, password);
    }

    @FXML
    void handlePasswordRecovery(ActionEvent event) {

        String email = txt_emailField.getText();
        if (email.isEmpty()) {
            MainController.showAlert("Error", "Por favor, ingrese su correo electrónico.", AlertType.ERROR);
        } else {
            loginController.handlePasswordRecovery(email);
        }

    }

    @FXML
    void showRegister(ActionEvent event) {

        MainController.loadScene("UserRegister", 900, 600);

    }

    @FXML
    void initialize() {

    }

}
