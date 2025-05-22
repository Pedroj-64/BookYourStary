package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.LoginInternalController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.SessionManager;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import javafx.scene.control.Alert;

public class LoginController {    public void handleLogin(String id, String password) {
        try {
            System.out.println("Intentando iniciar sesión con email: " + id);
            
            // Verificación especial para el administrador por defecto
            if ("pepito@gmail.com".equalsIgnoreCase(id) && "adminpass".equals(password)) {
                System.out.println("Detectadas credenciales de administrador por defecto");
                co.edu.uniquindio.poo.bookyourstary.util.LoginHelper.ensureAdminExists();
            }
            
            LoginInternalController loginController = new LoginInternalController(
                MainController.getInstance().getAdminService(),
                MainController.getInstance().getClientService()
            );
            SessionManager session = loginController.login(id, password);
            
            if (session != null) {
                Object usuario = session.getUsuarioActual();
                if (usuario instanceof Client) {
                    System.out.println("Cliente autenticado exitosamente: " + ((Client)usuario).getName());
                    MainController.showAlert("Inicio de sesión", "Bienvenido cliente", Alert.AlertType.INFORMATION);
                    MainController.loadScene("home", 900, 600); 
                } else if (usuario instanceof Admin) {
                    System.out.println("Administrador autenticado exitosamente: " + ((Admin)usuario).getName());
                    MainController.showAlert("Inicio de sesión", "Bienvenido administrador", Alert.AlertType.INFORMATION);
                    MainController.loadScene("MenuAdmin", 900, 600);
                }
            } else {
                System.err.println("Error de autenticación: credenciales inválidas");
                MainController.showAlert("Error de inicio de sesión", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            System.err.println("Error en handleLogin: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert("Error de inicio de sesión", "Ocurrió un error al procesar la solicitud: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void handlePasswordRecovery(String email) {
        // Validar formato básico de email
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            MainController.showAlert("Recuperación de contraseña", "Por favor ingrese un correo válido.", Alert.AlertType.WARNING);
            return;
        }
        try {
            MainController.getInstance().getCodeRecoveryService().recoverPassword(email);
            MainController.showAlert("Recuperación de contraseña", "Revise su correo electrónico para el código de recuperación.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            MainController.showAlert("Error", "No se pudo enviar el correo de recuperación. Intente más tarde.", Alert.AlertType.ERROR);
        }
    }

}
