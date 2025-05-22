package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.repository.RoomRepository;
import co.edu.uniquindio.poo.bookyourstary.service.RoomService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class ManageRoomsViewController {

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private TableColumn<Room, String> RoomNumberColumn;

    @FXML
    private TableColumn<Room, Double> priceForNightColumn;

    @FXML
    private TableColumn<Room, Integer> maxGuestsColumn;

    @FXML
    private TableColumn<Room, String> descriptionColumn;

    private final RoomService roomService;

    private final ObservableList<Room> roomsObservableList;

    public ManageRoomsViewController() {
        RoomRepository roomRepository = RoomRepository.getInstance();
        this.roomService = new RoomService(roomRepository);
        this.roomsObservableList = FXCollections.observableArrayList(roomService.findAllRooms());
    }

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        RoomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("RoomNumber"));
        priceForNightColumn.setCellValueFactory(new PropertyValueFactory<>("priceForNight"));
        maxGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("maxGuests"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Cargar datos en la tabla
        roomsTable.setItems(roomsObservableList);
    }

    @FXML
    private void showAddRoomDialog() {
        // Implementar lógica para mostrar un diálogo para agregar una habitación
        Room newRoom = new Room("101", 100.0, 2, "imageUrl", "Descripción de prueba");
        roomService.saveRoom(newRoom.getRoomNumber(), newRoom.getPriceForNight(), newRoom.getMaxGuests(), newRoom.getImageUrl(), newRoom.getDescription());
        roomsObservableList.add(newRoom);
        refreshTable();
    }

    @FXML
    private void handleEditRoom() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            selectedRoom.setDescription("Descripción actualizada");
            roomService.saveRoom(selectedRoom.getRoomNumber(), selectedRoom.getPriceForNight(), selectedRoom.getMaxGuests(), selectedRoom.getImageUrl(), selectedRoom.getDescription());
            refreshTable();
        } else {
            showAlert("Error", "Por favor selecciona una habitación para editar.");
        }
    }

    @FXML
    private void handleDeleteRoom() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            Optional<ButtonType> result = showConfirmationDialog("Eliminar habitación", "¿Estás seguro de eliminar la habitación?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                roomService.deleteRoom(selectedRoom.getRoomNumber());
                roomsObservableList.remove(selectedRoom);
                refreshTable();
            }
        } else {
            showAlert("Error", "Por favor selecciona una habitación para eliminar.");
        }
    }

    private void refreshTable() {
        roomsObservableList.setAll(roomService.findAllRooms());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
