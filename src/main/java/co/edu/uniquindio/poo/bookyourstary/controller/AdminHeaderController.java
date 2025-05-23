package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.scene.control.Alert.AlertType;

/**
 * Controlador para las operaciones lógicas del encabezado de administrador.
 * Sigue el patrón MVC separando la lógica de negocio de la vista.
 */
public class AdminHeaderController {
    
    private static AdminHeaderController instance;
    
    // Constructor privado para patrón Singleton
    private AdminHeaderController() {}
    
    /**
     * Obtiene la instancia única del controlador
     * @return Instancia de AdminHeaderController
     */
    public static AdminHeaderController getInstance() {
        if (instance == null) {
            instance = new AdminHeaderController();
        }
        return instance;
    }
    
    /**
     * Abre el formulario para crear un nuevo alojamiento
     * @return true si se abrió correctamente, false en caso contrario
     */
    public boolean openCreationForm() {
        try {
            System.out.println("Controller: Abriendo formulario para crear nuevo alojamiento");
            
            // Obtener MainController y a través de él el MenuAdminController
            var mainController = MainController.getInstance();
            var menuAdminController = mainController.getMenuAdminController();
            
            // Abrir la ventana de creación
            menuAdminController.openCreationWindow();
            
            // Notificar que se necesita actualizar la lista de alojamientos
            System.out.println("Controller: Formulario de creación cerrado, actualizando vistas");
            
            // Actualizar la vista si es necesario
            mainController.refreshCurrentView();
            
            return true;
        } catch (Exception e) {
            System.err.println("Controller: Error al abrir el formulario de creación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene el nombre de usuario administrador actual
     * @return Nombre de usuario o "Admin" por defecto
     */
    public String getCurrentAdminName() {
        try {
            // Obtener el administrador actual desde SessionManager
            var admin = MainController.getInstance().getSessionManager().getUsuarioActual();
            if (admin != null && admin instanceof co.edu.uniquindio.poo.bookyourstary.model.Admin) {
                return ((co.edu.uniquindio.poo.bookyourstary.model.Admin) admin).getName();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener nombre de admin: " + e.getMessage());
        }
        
        // Si hay algún error o no hay sesión, devolver valor por defecto
        return "Admin";
    }
    
    /**
     * Obtiene el saldo virtual del administrador (si aplica)
     * @return Saldo formateado como String o "0" por defecto
     */
    public String getCurrentAdminBalance() {
        try {
            // En una implementación real, podrías obtener el saldo del administrador
            // Por ahora, devolvemos un valor fijo
            return "100000";
        } catch (Exception e) {
            System.err.println("Error al obtener saldo de admin: " + e.getMessage());
            return "0";
        }
    }
    
    /**
     * Navega al menú de opciones de administrador
     * @return true si la navegación fue exitosa, false en caso contrario
     */    public boolean navigateToAdminOptions() {
        try {
            System.out.println("Controller: Navegando al menú de opciones de administrador");
            MainController.loadScene("AdminOptions", 400, 300);
            return true;
        } catch (Exception e) {
            System.err.println("Controller: Error al navegar al menú de opciones: " + e.getMessage());
            MainController.showAlert(
                "Error",
                "No se pudo abrir el menú de opciones. Consulte los logs para más detalles.",
                AlertType.ERROR);
            return false;
        }
    }
}
