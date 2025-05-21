package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import co.edu.uniquindio.poo.bookyourstary.App;
import co.edu.uniquindio.poo.bookyourstary.config.EmailConfig;
import co.edu.uniquindio.poo.bookyourstary.controller.CreationAndEditingFormController;
import co.edu.uniquindio.poo.bookyourstary.repository.*;
import co.edu.uniquindio.poo.bookyourstary.service.*;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;

public class MainController {

    private static MainController instance;
    private static Scene scene;

    private MainController() {
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public static void setScene(Scene mainScene) {
        scene = mainScene;
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
            Parent root = loadFXML(fxml);
            if (scene != null) {
                scene.setRoot(root);
                scene.getWindow().setWidth(width);
                scene.getWindow().setHeight(height);
            }
            // Si usas App.getStage(), puedes agregar aquí la lógica para cambiar la escena global
        } catch (IOException e) {
            showAlert("Error al cambiar la vista", "No se pudo cargar el archivo FXML: " + e.getMessage(),
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



}
