package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import co.edu.uniquindio.poo.bookyourstary.App;
import co.edu.uniquindio.poo.bookyourstary.config.EmailConfig;
import co.edu.uniquindio.poo.bookyourstary.controller.AdminHeaderController;
import co.edu.uniquindio.poo.bookyourstary.controller.CreationAndEditingFormController;
import co.edu.uniquindio.poo.bookyourstary.controller.MenuAdminController;
import co.edu.uniquindio.poo.bookyourstary.repository.*;
import co.edu.uniquindio.poo.bookyourstary.service.*;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;
import co.edu.uniquindio.poo.bookyourstary.viewController.MenuAdminViewController;
import lombok.Setter;

public class MainController {

    private static MainController instance;
    // @Setter // Temporarily commenting out to manually add the method
    private static Scene scene;

    // Manually added static setter for the scene
    public static void setScene(Scene newScene) {
        MainController.scene = newScene;
    }

    private MainController() {
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
            // Inicializar el sistema de logging
            try {
                co.edu.uniquindio.poo.bookyourstary.util.LoggerConfig.configureLogger();
                System.out.println("Sistema de logging inicializado correctamente");
            } catch (Exception e) {
                System.err.println("Error al inicializar el sistema de logging: " + e.getMessage());
            }
        }
        return instance;
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showAlertAndRedirect(String title, String message, Alert.AlertType type, String fxml,
            double width, double height) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(evt -> loadScene(fxml, width, height));
        alert.show();
    }

    public static void loadScene(String fxml, double width, double height) {
        try {
            // Normalizar el nombre del archivo (remover extensión .fxml si está presente)
            String normalizedFxml = fxml.replace(".fxml", "");

            // Registrar la acción para depuración
            System.out.println("Cargando escena: " + normalizedFxml);

            Parent root = loadFXML(normalizedFxml);
            if (scene != null) {
                scene.setRoot(root);
                scene.getWindow().setWidth(width);
                scene.getWindow().setHeight(height);
                System.out.println("Escena cargada exitosamente: " + normalizedFxml);
            } else {
                System.err.println("Error: Scene es null, no se puede cambiar la raíz");
                showAlert("Error al cambiar la vista",
                        "La escena principal no está inicializada correctamente.",
                        Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar FXML: " + fxml + " - " + e.getMessage());
            e.printStackTrace();
            showAlert("Error al cambiar la vista",
                    "No se pudo cargar el archivo FXML: " + fxml + "\n" + e.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar escena: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error inesperado",
                    "Ocurrió un error al cambiar la vista: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // Accesos centralizados a los Singletons del proyecto
    public EmailConfig getEmailConfig() {
        return EmailConfig.getInstance();
    }

    public EmailNotifier getEmailNotifier() {
        return EmailNotifier.getInstance();
    }

    public OfferController getOfferController() {
        return OfferController.getInstance();
    }

    public ClientRepository getClientRepository() {
        return ClientRepository.getInstance();
    }

    public VirtualWalletRepository getVirtualWalletRepository() {
        return VirtualWalletRepository.getInstance();
    }

    public BookingRepository getBookingRepository() {
        return BookingRepository.getInstance();
    }

    public BillRepository getBillRepository() {
        return BillRepository.getInstance();
    }

    public ReviewRepository getReviewRepository() {
        return ReviewRepository.getInstance();
    }

    public WalletTransactionRepository getWalletTransactionRepository() {
        return WalletTransactionRepository.getInstance();
    }

    public ServiceIncludedRepository getServiceIncludedRepository() {
        return ServiceIncludedRepository.getInstance();
    }

    public AdminRepository getAdminRepository() {
        return AdminRepository.getInstance();
    }

    public OfferRepository getOfferRepository() {
        return OfferRepository.getInstance();
    }

    public OfferService getOfferService() {
        return OfferService.getInstance();
    }

    public HostingRepository getHostingRepository() {
        return HostingRepository.getInstance();
    }

    public ApartamentService getApartamentService() {
        return ApartamentService.getInstance();
    }

    public BillService getBillService() {
        return BillService.getInstance();
    }

    public HouseService getHouseService() {
        return HouseService.getInstance();
    }

    public HotelService getHotelService() {
        return HotelService.getInstance();
    }

    public ClientService getClientService() {
        return ClientService.getInstance();
    }

    public VirtualWalletService getVirtualWalletService() {
        return VirtualWalletService.getInstance();
    }

    public WalletTransactionService getWalletTransactionService() {
        return WalletTransactionService.getInstance();
    }

    public BookingService getBookingService() {
        return BookingService.getInstance();
    }

    public ReviewService getReviewService() {
        return ReviewService.getInstance();
    }

    public HostingService getHostingService() {
        return HostingService.getInstance();
    }

    public ServiceIncludedService getServiceIncludedService() {
        return ServiceIncludedService.getInstance();
    }

    /**
     * Obtiene la instancia del controlador de login
     * 
     * @return Instancia única de LoginInternalController
     */
    public LoginInternalController getLoginInternalController() {
        return LoginInternalController.getInstance();
    }

    public HostingFilterService getHostingFilterService() {
        return HostingFilterService.getInstance();
    }

    public AdminService getAdminService() {
        return AdminService.getInstance();
    }

    public SessionManager getSessionManager() {
        return SessionManager.getInstance();
    }

    public CodeRecoveryService getCodeRecoveryService() {
        return CodeRecoveryService.getInstance();
    }

    public EmailTemplateService getEmailTemplateService() {
        return EmailTemplateService.getInstance();
    }

    public CodeRecoveryRepository getCodeRecoveryRepository() {
        return CodeRecoveryRepository.getInstance();
    }

    public CodeActivationService getCodeActivationService() {
        return CodeActivationService.getInstance();
    }

    public CodeActivationRepository getCodeActivationRepository() {
        return CodeActivationRepository.getInstance();
    }

    public CreationAndEditingFormController getCreationAndEditingFormController() {
        return CreationAndEditingFormController.getInstance();
    }

    public CartManager getCartManager() {
        return CartManager.getInstance();
    }

    public CityService getCityService() {
        // Assuming CityService is not a singleton yet, if it becomes one, adjust to
        return CityService.getInstance();
    } // Variable para almacenar referencia al último MenuAdminViewController activo

    private static MenuAdminViewController activeMenuAdminController;

    /**
     * Registra una instancia activa del MenuAdminViewController para poder
     * refrescarlo más tarde
     * 
     * @param controller La instancia del controlador de vista
     */
    public static void registerActiveMenuAdminController(MenuAdminViewController controller) {
        activeMenuAdminController = controller;
        System.out.println("MenuAdminViewController registrado para actualizaciones");
    }

    /**
     * Actualiza la vista actual si es posible. Útil después de operaciones que
     * modifican los datos.
     * Si hay un MenuAdminViewController activo, llamará a su método refresh.
     */
    public void refreshCurrentView() {
        try {
            if (scene == null || scene.getRoot() == null) {
                System.err.println("No hay una escena activa para refrescar");
                return;
            }

            System.out.println("Intentando refrescar la vista actual...");

            // 1. Verificar si tenemos una referencia directa al MenuAdminViewController
            if (activeMenuAdminController != null) {
                System.out.println("Refrescando MenuAdminViewController registrado");
                try {
                    // Llamamos directamente a refresh() que es público
                    javafx.application.Platform.runLater(() -> {
                        activeMenuAdminController.refresh(new javafx.event.ActionEvent());
                        System.out.println("Refresh ejecutado en el hilo de UI");
                    });
                    System.out.println("Solicitud de refresh enviada al hilo de UI");
                    return;
                } catch (Exception e) {
                    System.err.println("Error al refrescar MenuAdminViewController: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("No hay un MenuAdminViewController registrado actualmente");
            }

            // 2. Si llegamos aquí, intentar recargar toda la escena actual (más costoso
            // pero más seguro)
            String currentFxml = scene.getRoot().getId();
            if (currentFxml != null && !currentFxml.isEmpty()) {
                System.out.println("Intentando recargar toda la escena: " + currentFxml);
                double width = scene.getWindow().getWidth();
                double height = scene.getWindow().getHeight();
                loadScene(currentFxml, width, height);
                System.out.println("Escena recargada completamente");
            } else {
                System.err.println("No se pudo determinar la vista actual");
            }
        } catch (Exception e) {
            System.err.println("Error al intentar refrescar la vista actual: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una instancia del MenuAdminController
     * 
     * @return Instancia de MenuAdminController
     */
    public MenuAdminController getMenuAdminController() {
        return new MenuAdminController();
    }

    /**
     * Obtiene una instancia del AdminHeaderController
     * 
     * @return Instancia de AdminHeaderController
     */
    public AdminHeaderController getAdminHeaderController() {
        return AdminHeaderController.getInstance();
    }
}
