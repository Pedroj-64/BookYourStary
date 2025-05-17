package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import co.edu.uniquindio.poo.bookyourstary.config.EmailConfig;
import co.edu.uniquindio.poo.bookyourstary.repository.BillRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.BookingRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.ClientRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.ReviewRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.VirtualWalletRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.WalletTransactionRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.ServiceIncludedRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.AdminRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.OfferRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.HostingRepository;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;
import co.edu.uniquindio.poo.bookyourstary.service.OfferService;

public class MainController {

    private static MainController instance;

    private MainController() {}

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadScene(String fxml, double width, double height) {
        try {
            Parent root = loadFXML(fxml);
            // Aquí puedes usar el Stage principal para cambiar la escena, ejemplo:
            // App.getStage().setScene(new Scene(root, width, height));
        } catch (IOException e) {
            showAlert("Error al cambiar la vista", "No se pudo cargar el archivo FXML: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    public void showAlertAndRedirect(String title, String message, Alert.AlertType type, String fxml,
            double width, double height) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(evt -> loadScene(fxml, width, height));
        alert.show();
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
}
