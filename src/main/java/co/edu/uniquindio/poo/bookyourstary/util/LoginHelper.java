package co.edu.uniquindio.poo.bookyourstary.util;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.LoginInternalController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.SessionManager;
import co.edu.uniquindio.poo.bookyourstary.model.Admin;

/**
 * Clase utilitaria para verificar el inicio de sesión
 */
public class LoginHelper {

    /**
     * Verifica las credenciales del administrador
     */
    public static boolean verifyAdminLogin() {
        try {
            // Obtener instancia del controlador de login
            LoginInternalController loginController = MainController.getInstance().getLoginInternalController();

            // Intentar login con credenciales del admin
            SessionManager session = loginController.login("pepito@gmail.com", "adminpass");
            if (session != null) {
                // Verificar resultado
                if (session.getUsuarioActual() instanceof Admin) {
                    Admin user = (Admin) session.getUsuarioActual();
                    System.out.println("✓ Login del admin exitoso: " + user.getName() + " (" + user.getEmail() + ")");
                    return true;
                } else {
                    System.out.println("✗ Error: Se logueó pero no es un admin");
                    return false;
                }
            } else {
                System.out.println("✗ Error: Login fallido para admin");
                return false;
            }
        } catch (Exception e) {
            System.err.println("✗ Error al verificar login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Forzar la creación de un administrador
     */
    public static void ensureAdminExists() {
        try {
            Admin admin = MainController.getInstance().getAdminService().getAdmin();
            if (admin == null) {
                admin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
                admin.setActive(true);
                MainController.getInstance().getAdminRepository().saveAdmin(admin);
                System.out.println("✓ Administrador creado: " + admin.getName() + " (" + admin.getEmail() + ")");
            } else {
                admin.setActive(true);
                MainController.getInstance().getAdminRepository().saveAdmin(admin);
                System.out.println("✓ Administrador actualizado: " + admin.getName() + " (" + admin.getEmail() + ")");
            }
        } catch (Exception e) {
            System.err.println("✗ Error al crear/actualizar admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
