package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.ProfileController;
import co.edu.uniquindio.poo.bookyourstary.model.Booking;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class ProfileViewController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> idColumn;
    @FXML private TableColumn<Booking, String> hostingColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> stateColumn;
    @FXML private TextArea bookingDetailsArea;
    @FXML private Button cancelBookingButton;
    @FXML private Button updateProfileButton;
    @FXML private AnchorPane mainPane;

    private Client currentClient;
    private final ProfileController profileController;

    public ProfileViewController() {
        this.profileController = ProfileController.getInstance();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCurrentClient();
        setupSelectionListener();
        loadUserData();
        loadReservations();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBookingId()));
        hostingColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHosting().getName()));
        dateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStartDate().toString()));
        stateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBookingState().toString()));
    }

    private void loadCurrentClient() {
        currentClient = profileController.getCurrentClient();
        if (currentClient == null) {
            showError("Error", "No se pudo cargar el perfil del cliente");
            return;
        }
    }

    private void setupSelectionListener() {
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                bookingDetailsArea.setText(profileController.buildBookingDetails(newSelection));
                cancelBookingButton.setDisable(!profileController.canCancelBooking(newSelection));
            } else {
                bookingDetailsArea.clear();
                cancelBookingButton.setDisable(true);
            }
        });
    }

    @FXML
    private void loadUserData() {
        if (currentClient != null) {
            nameField.setText(currentClient.getName());
            emailField.setText(currentClient.getEmail());
            phoneField.setText(currentClient.getPhoneNumber());
        }
    }

    @FXML
    private void loadReservations() {
        if (currentClient != null) {
            List<Booking> bookings = profileController.getClientBookings(currentClient.getId());
            bookingsTable.setItems(FXCollections.observableArrayList(bookings));
        }
    }

    @FXML
    private void handleUpdateProfile() {
        try {
            profileController.updateClientInfo(
                currentClient,
                nameField.getText(),
                emailField.getText(),
                phoneField.getText()
            );
            showInfo("Éxito", "Perfil actualizado correctamente");
        } catch (Exception e) {
            showError("Error", "No se pudo actualizar el perfil: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) return;

        if (showConfirmation("Confirmar cancelación", 
            "¿Está seguro que desea cancelar esta reserva?")) {
            try {
                profileController.cancelBooking(selectedBooking.getBookingId());
                loadReservations();
                showInfo("Éxito", "Reserva cancelada correctamente");
            } catch (Exception e) {
                showError("Error", "No se pudo cancelar la reserva: " + e.getMessage());
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
