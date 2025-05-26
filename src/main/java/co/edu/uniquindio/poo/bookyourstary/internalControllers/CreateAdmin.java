package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.AdminService;

public class CreateAdmin {
    /**
     * Crea y registra un administrador completamente funcional usando singletons de
     * repositorio y servicio.
     * 
     * @param id          ID del admin
     * @param name        Nombre del admin
     * @param phoneNumber Teléfono
     * @param email       Correo electrónico
     * @param password    Contraseña en texto plano
     * @return Admin registrado y listo para usarse
     */
    public static Admin createAndRegisterAdmin(String id, String name, String phoneNumber, String email,
            String password) {
        AdminService adminService = MainController.getInstance().getAdminService();
        Admin admin = new Admin(id, name, phoneNumber, email, password);
        adminService.registerAdmin(admin);
        return admin;
    }
}
