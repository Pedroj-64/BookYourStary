package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.RegisterController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterViewController {

    @FXML
    private TextField txt_ActivationCode;

    @FXML
    private TextField txt_Cedula;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_name;

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextField txt_phone;

    private final RegisterController registerController = new RegisterController();
    private boolean isRegistered = false;

    @FXML
    void handleRegister(ActionEvent event) {
        String id = txt_Cedula.getText();
        String name = txt_name.getText();
        String phone = txt_phone.getText();
        String email = txt_email.getText();
        String password = txt_password.getText();
        String code = txt_ActivationCode.getText();

        if (!isRegistered) {
            registerController.registerUser(
                new Client(id, name, phone, email, password),
                password
            );
            isRegistered = true;
        } else {
            boolean activated = registerController.activateUser(code);
            
        }
    }



}
