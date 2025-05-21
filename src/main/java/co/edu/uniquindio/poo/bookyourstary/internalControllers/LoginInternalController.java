package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.service.AdminService;
import co.edu.uniquindio.poo.bookyourstary.service.ClientService;
import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import javafx.scene.control.Alert; // Added import


public class LoginInternalController {

    private final AdminService adminService;
    private final ClientService clientService;

    public LoginInternalController(AdminService adminService, ClientService clientService) {
        this.adminService = adminService;
        this.clientService = clientService;
    }

    /**
     * Verifica el inicio de sesión y retorna el SessionManager con el usuario autenticado si es exitoso, null si no.
     * Si la contraseña es incorrecta o el usuario no existe, retorna null.
     */
    public SessionManager login(String email, String password) { // Renamed id to email for clarity
        Admin admin = adminService.getAdmin();
        // Admin login: check email (case-insensitive) and password
        if (admin != null && admin.getEmail().equalsIgnoreCase(email) && adminService.verifyPassword(admin, password)) {
            SessionManager session = MainController.getInstance().getSessionManager();
            session.setUsuarioActual(admin);
            return session;
        }

        // Client login: check email, password, and activation status
        Client client = null;
        try {
            client = clientService.getClientByEmail(email);
        } catch (IllegalArgumentException e) {
            // Client not found by email, handled by the null check below
        }
        
        if (client != null && clientService.verifyPassword(client, password)) {
            if (client.isActive()) {
                SessionManager session = MainController.getInstance().getSessionManager();
                session.setUsuarioActual(client);
                return session;
            } else {
                // Client exists, password is correct, but account is not active
                MainController.showAlert("Inicio de sesión fallido",
                        "Su cuenta aún no ha sido activada. Por favor, revise su correo electrónico para el código de activación o active su cuenta.",
                        Alert.AlertType.WARNING);
                return null; // Indicate login failure due to inactive account
            }
        }
        // If neither admin nor client login was successful (or client was inactive)
        return null;
    }
}
