package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.service.AdminService;
import co.edu.uniquindio.poo.bookyourstary.service.ClientService;
import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import co.edu.uniquindio.poo.bookyourstary.model.Client;


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
    public SessionManager login(String id, String password) {
        Admin admin = adminService.getAdmin();
        if (admin != null && admin.getId().equals(id) && adminService.verifyPassword(admin, password)) {
            SessionManager session = MainController.getInstance().getSessionManager();
            session.setUsuarioActual(admin);
            return session;
        }
        Client client = clientService.getClient(id);
        if (client != null && clientService.verifyPassword(client, password)) {
            SessionManager session = MainController.getInstance().getSessionManager();
            session.setUsuarioActual(client);
            return session;
        }
        return null;
    }
}
