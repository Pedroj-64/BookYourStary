package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.repository.RoomRepository;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.RoomService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageRoomsViewController implements Initializable {

    @FXML
    private TableView<Room> roomsTable;

    private RoomService roomService;
    private ObservableList<Room> roomList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar el servicio de habitaciones
        roomService = new RoomService(RoomRepository.getInstance());

        // Configurar las columnas de la tabla
        setupTableColumns();

        // Cargar los datos
        loadRooms();
    }

    private void setupTableColumns() {
        // Verificar que la tabla tiene las columnas necesarias
        if (roomsTable.getColumns().size() < 4) {
            System.err.println("Error: La tabla debe tener al menos 4 columnas definidas en el FXML");
            return;
        }

        // Configurar cada columna con su correspondiente propiedad
        TableColumn<Room, String> numberColumn = (TableColumn<Room, String>) roomsTable.getColumns().get(0);
        numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomNumber()));

        TableColumn<Room, String> typeColumn = (TableColumn<Room, String>) roomsTable.getColumns().get(1);
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty("Estándar")); // Puedes añadir tipos si los hay

        TableColumn<Room, Number> capacityColumn = (TableColumn<Room, Number>) roomsTable.getColumns().get(2);
        capacityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getMaxGuests()));

        TableColumn<Room, Number> priceColumn = (TableColumn<Room, Number>) roomsTable.getColumns().get(3);
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPriceForNight()));

        // Configurar formato para la columna de precio
        priceColumn.setCellFactory(column -> new TableCell<Room, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item.doubleValue()));
                }
            }
        });
    }

    private void loadRooms() {
        // Get all rooms
        roomList = FXCollections.observableArrayList(roomService.findAllRooms());
        roomsTable.setItems(roomList);
    }

    @FXML
    void showAddRoomDialog() {
        // add new Room
        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Agregar Nueva Habitación");
        dialog.setHeaderText("Introduce los datos de la nueva habitación");

        ButtonType saveButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("Número de habitación");

        TextField priceField = new TextField();
        priceField.setPromptText("Precio por noche");

        TextField maxGuestsField = new TextField();
        maxGuestsField.setPromptText("Capacidad máxima");

        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText("URL de la imagen");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Descripción");

        grid.add(new Label("Número:"), 0, 0);
        grid.add(roomNumberField, 1, 0);
        grid.add(new Label("Precio por noche:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Capacidad máxima:"), 0, 2);
        grid.add(maxGuestsField, 1, 2);
        grid.add(new Label("URL de imagen:"), 0, 3);
        grid.add(imageUrlField, 1, 3);
        grid.add(new Label("Descripción:"), 0, 4);
        grid.add(descriptionArea, 1, 4);

        dialog.getDialogPane().setContent(grid);

        roomNumberField.requestFocus();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String roomNumber = roomNumberField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int maxGuests = Integer.parseInt(maxGuestsField.getText());
                    String imageUrl = imageUrlField.getText();
                    String description = descriptionArea.getText();

                    // Validaciones básicas
                    if (roomNumber.trim().isEmpty()) {
                        throw new IllegalArgumentException("El número de habitación no puede estar vacío");
                    }
                    if (price <= 0) {
                        throw new IllegalArgumentException("El precio debe ser mayor que cero");
                    }
                    if (maxGuests <= 0) {
                        throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
                    }

                    return new Room(roomNumber, price, maxGuests, imageUrl, description);
                } catch (NumberFormatException e) {
                    MainController.showAlert("Error de formato",
                            "Por favor, introduce valores numéricos válidos para precio y capacidad",
                            AlertType.ERROR);
                    return null;
                } catch (IllegalArgumentException e) {
                    MainController.showAlert("Error de validación", e.getMessage(), AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Room> result = dialog.showAndWait();
        result.ifPresent(room -> {
            try {
                roomService.saveRoom(
                        room.getRoomNumber(),
                        room.getPriceForNight(),
                        room.getMaxGuests(),
                        room.getImageUrl(),
                        room.getDescription());
                loadRooms(); // Recargar la lista de habitaciones
                MainController.showAlert("Habitación Agregada",
                        "La habitación ha sido agregada exitosamente",
                        AlertType.INFORMATION);
            } catch (Exception e) {
                MainController.showAlert("Error",
                        "No se pudo guardar la habitación: " + e.getMessage(),
                        AlertType.ERROR);
            }
        });
    }

    @FXML
    void handleEditRoom() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            MainController.showAlert("Selección requerida",
                    "Por favor, selecciona una habitación para editar",
                    AlertType.WARNING);
            return;
        }

        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Editar Habitación");
        dialog.setHeaderText("Modifica los datos de la habitación");

        ButtonType saveButtonType = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField roomNumberField = new TextField(selectedRoom.getRoomNumber());
        roomNumberField.setDisable(true); // No permitir cambiar el número/ID de la habitación

        TextField priceField = new TextField(String.valueOf(selectedRoom.getPriceForNight()));
        TextField maxGuestsField = new TextField(String.valueOf(selectedRoom.getMaxGuests()));
        TextField imageUrlField = new TextField(selectedRoom.getImageUrl());
        TextArea descriptionArea = new TextArea(selectedRoom.getDescription());

        grid.add(new Label("Número:"), 0, 0);
        grid.add(roomNumberField, 1, 0);
        grid.add(new Label("Precio por noche:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Capacidad máxima:"), 0, 2);
        grid.add(maxGuestsField, 1, 2);
        grid.add(new Label("URL de imagen:"), 0, 3);
        grid.add(imageUrlField, 1, 3);
        grid.add(new Label("Descripción:"), 0, 4);
        grid.add(descriptionArea, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir el resultado cuando se pulsa el botón actualizar
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String roomNumber = roomNumberField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int maxGuests = Integer.parseInt(maxGuestsField.getText());
                    String imageUrl = imageUrlField.getText();
                    String description = descriptionArea.getText();

                    // Validaciones básicas
                    if (price <= 0) {
                        throw new IllegalArgumentException("El precio debe ser mayor que cero");
                    }
                    if (maxGuests <= 0) {
                        throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
                    }

                    return new Room(roomNumber, price, maxGuests, imageUrl, description);
                } catch (NumberFormatException e) {
                    MainController.showAlert("Error de formato",
                            "Por favor, introduce valores numéricos válidos para precio y capacidad",
                            AlertType.ERROR);
                    return null;
                } catch (IllegalArgumentException e) {
                    MainController.showAlert("Error de validación", e.getMessage(), AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Room> result = dialog.showAndWait();
        result.ifPresent(updatedRoom -> {
            try {

                roomService.deleteRoom(updatedRoom.getRoomNumber());

                roomService.saveRoom(
                        updatedRoom.getRoomNumber(),
                        updatedRoom.getPriceForNight(),
                        updatedRoom.getMaxGuests(),
                        updatedRoom.getImageUrl(),
                        updatedRoom.getDescription());

                loadRooms();
                MainController.showAlert("Habitación Actualizada",
                        "La habitación ha sido actualizada exitosamente",
                        AlertType.INFORMATION);
            } catch (Exception e) {
                MainController.showAlert("Error",
                        "No se pudo actualizar la habitación: " + e.getMessage(),
                        AlertType.ERROR);
            }
        });
    }

    @FXML
    void handleDeleteRoom() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            MainController.showAlert("Selección requerida",
                    "Por favor, selecciona una habitación para eliminar",
                    AlertType.WARNING);
            return;
        }

        // Confirm to delete
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar eliminación");
        confirmDialog.setHeaderText("¿Estás seguro que deseas eliminar esta habitación?");
        confirmDialog.setContentText("Número: " + selectedRoom.getRoomNumber() +
                "\nPrecio: $" + selectedRoom.getPriceForNight() +
                "\nCapacidad: " + selectedRoom.getMaxGuests() + " personas");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                roomService.deleteRoom(selectedRoom.getRoomNumber());
                loadRooms(); // Recargar la lista de habitaciones
                MainController.showAlert("Habitación Eliminada",
                        "La habitación ha sido eliminada exitosamente",
                        AlertType.INFORMATION);
            } catch (Exception e) {
                MainController.showAlert("Error",
                        "No se pudo eliminar la habitación: " + e.getMessage(),
                        AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleReturn() {

        MainController.loadScene("AdminOptions", 600, 400);
    }
}