package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.RegisterController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController; // Added for showAlert
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert; // Added for AlertType
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
    private boolean isRegistered = false; // This flag indicates if the user has attempted initial registration

    @FXML
    void handleRegister(ActionEvent event) {
        if (!isRegistered) {
            // Attempt initial registration
            String id = txt_Cedula.getText().trim();
            String name = txt_name.getText().trim();
            String phone = txt_phone.getText().trim();
            String email = txt_email.getText().trim();
            String password = txt_password.getText(); // Password can have leading/trailing spaces if intended

            // Basic input validation
            if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                MainController.showAlert("Error de validación", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
                return;
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$")) {
                MainController.showAlert("Error de validación", "Formato de correo electrónico inválido.", Alert.AlertType.ERROR);
                return;
            }
            // Add more password complexity rules if needed

            boolean registrationAttempted = registerController.registerUser(
                new Client(id, name, phone, email, password), // Client constructor takes plain password
                password // registerUser will handle hashing
            );
            if (registrationAttempted) {
                // UI can be updated here to clearly show that now activation code is expected
                // For example, disable registration fields and enable/highlight activation code field.
                // The alert from RegisterController already informs the user.
                isRegistered = true; // Set flag to indicate next action is activation
                txt_ActivationCode.setPromptText("Ingrese el código de activación aquí");
                // Optionally disable other fields:
                // txt_Cedula.setDisable(true);
                // txt_name.setDisable(true);
                // txt_phone.setDisable(true);
                // txt_email.setDisable(true);
                // txt_password.setDisable(true);
            }
            // If registrationAttempted is false, RegisterController already showed an error alert.
        } else {
            // Attempt account activation
            String code = txt_ActivationCode.getText().trim();
            if (code.isEmpty()) {
                MainController.showAlert("Error de validación", "El código de activación no puede estar vacío.", Alert.AlertType.ERROR);
                return;
            }
            // activateUser in RegisterController handles alerts and navigation
            registerController.activateUser(code);
            // If activation fails, the user remains on this screen to try again.
            // If successful, RegisterController navigates away.
        }
    }

    @FXML
    void salir(ActionEvent event) {
        MainController.loadScene("home", 600, 400);
    }

}
