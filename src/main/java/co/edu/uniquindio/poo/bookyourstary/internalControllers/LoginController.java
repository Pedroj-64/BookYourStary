package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.service.AdminService;
import co.edu.uniquindio.poo.bookyourstary.service.ClientService;
import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import co.edu.uniquindio.poo.bookyourstary.model.Client;

public class LoginController {

    private final AdminService adminService;
    private final ClientService clientService;

    public LoginController(AdminService adminService, ClientService clientService) {
        this.adminService = adminService;
        this.clientService = clientService;
    }

    public String login(String id, String password) {
        if (adminService.getAdmin().getId().equals(id)) {  // Verifica si es admin
            Admin admin = adminService.getAdmin();
            if (adminService.verifyPassword(admin, password)) {
                // Aquí podríamos cargar la interfaz de Admin
                return "Admin logged in successfully.";
            } else {
                return "Incorrect password for admin.";
            }
        } else {
            Client client = clientService.getClient(id);
            if (client != null && clientService.verifyPassword(client, password)) {
                // Aquí podríamos cargar la interfaz de Client
                return "Client logged in successfully.";
            } else {
                return "Incorrect password for client.";
            }
        }
    }
}
