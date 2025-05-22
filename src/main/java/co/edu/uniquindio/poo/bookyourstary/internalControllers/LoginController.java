package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.service.AuthService;

public class LoginController {
    public void handleLogin(String email, String password) {

        boolean loginSuccessful = true; // Simula un inicio de sesión exitoso

        if (loginSuccessful) {
            // Establecer el correo del usuario en AuthService
            AuthService.setCurrentUserEmail(email);

            System.out.println("Usuario logueado: " + AuthService.getCurrentUserEmail());
        } else {
            System.out.println("Inicio de sesión fallido.");
        }
    }
}
