package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.AdminService;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.ClientService;
import javafx.scene.control.Alert; 

public class LoginInternalController {

    private final AdminService adminService;
    private final ClientService clientService;

    // Singleton pattern
    private static LoginInternalController instance;

    public static LoginInternalController getInstance() {
        if (instance == null) {
            instance = new LoginInternalController(
                    AdminService.getInstance(),
                    ClientService.getInstance());
        }
        return instance;
    }

    public LoginInternalController(AdminService adminService, ClientService clientService) {
        this.adminService = adminService;
        this.clientService = clientService;
    }

    /**
     * Verifica el inicio de sesión y retorna el SessionManager con el usuario
     * autenticado si es exitoso, null si no.
     * Si la contraseña es incorrecta o el usuario no existe, retorna null.
     */
    public SessionManager login(String email, String password) { // Renamed id to email for clarity
        // Credenciales hardcodeadas para el admin principal
        if (email.equalsIgnoreCase("pepito@gmail.com") && password.equals("adminpass")) {
            Admin admin = adminService.getAdmin();
            if (admin == null) {
                // Si no existe, crear el admin por defecto
                admin = new Admin("1", "admin", "1234567890",
                        "pepito@gmail.com", "adminpass");
                admin.setActive(true);
                adminService.registerAdmin(admin);
            }
            SessionManager session = MainController.getInstance().getSessionManager();
            session.setUsuarioActual(admin);
            System.out.println("Admin logueado exitosamente: " + admin.getName());
            return session;
        }

        // Intentar login normal de admin
        Admin admin = adminService.getAdmin();
        if (admin != null && admin.getEmail().equalsIgnoreCase(email) && adminService.verifyPassword(admin, password)) {
            SessionManager session = MainController.getInstance().getSessionManager();
            session.setUsuarioActual(admin);
            return session;
        } // Client login: check email, password, and activation status
        Client client = null;
        try {
            client = clientService.getClientByEmail(email);
            System.out.println("Cliente encontrado: " + client.getName() + " (" + client.getEmail() + "), activo: "
                    + client.isActive());
        } catch (IllegalArgumentException e) {
            // Client not found by email
            System.out.println("Cliente no encontrado con email: " + email + " - " + e.getMessage());
        }

        if (client != null) {
            // Check password verification
            boolean passwordCorrect = clientService.verifyPassword(client, password);
            System.out.println(
                    "Verificación de contraseña para " + email + ": " + (passwordCorrect ? "correcta" : "incorrecta"));

            if (passwordCorrect) {
                if (client.isActive()) {
                    SessionManager session = MainController.getInstance().getSessionManager();
                    session.setUsuarioActual(client);
                    System.out.println("Inicio de sesión exitoso para cliente: " + client.getName());
                    return session;
                } else {
                    // Client exists, password is correct, but account is not active
                    System.out.println("Cliente existe pero no está activo: " + client.getName());
                    MainController.showAlert("Inicio de sesión fallido",
                            "Su cuenta aún no ha sido activada. Por favor, revise su correo electrónico para el código de activación o active su cuenta.",
                            Alert.AlertType.WARNING);
                    return null; // Indicate login failure due to inactive account
                }
            }
        }
        // If neither admin nor client login was successful (or client was inactive)
        System.out.println("Inicio de sesión fallido para: " + email);
        return null;
    }
}
