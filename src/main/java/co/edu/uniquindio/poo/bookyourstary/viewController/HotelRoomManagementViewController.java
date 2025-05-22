package co.edu.uniquindio.poo.bookyourstary.viewController;

import java.util.List;

import co.edu.uniquindio.poo.bookyourstary.controller.HotelRoomManagementController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Hotel;
import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.ViewLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * Controlador para la vista de gestión de habitaciones de hotel.
 * Esta vista permite a los administradores gestionar las habitaciones de los hoteles.
 */
public class HotelRoomManagementViewController {

    @FXML private AnchorPane UserHeader0;
    @FXML private ComboBox<Hotel> combo_Hotel;
    @FXML private ComboBox<String> combo_TipoHabitacion;
    @FXML private TextField txt_Buscar;
    @FXML private TextField txt_Capacidad;
    @FXML private TextField txt_PrecioMin;
    @FXML private TextField txt_PrecioMax;
    @FXML private CheckBox check_Wifi;
    @FXML private CheckBox check_MiniBar;
    @FXML private CheckBox check_AireAcondicionado;
    @FXML private CheckBox check_TV;
    @FXML private CheckBox check_Desayuno;
    @FXML private TableView<Room> table_Habitaciones;
    @FXML private TableColumn<Room, String> col_ID;
    @FXML private TableColumn<Room, String> col_Numero;
    @FXML private TableColumn<Room, String> col_TipoHabitacion;
    @FXML private TableColumn<Room, Integer> col_Capacidad;
    @FXML private TableColumn<Room, Double> col_Precio;
    @FXML private TableColumn<Room, String> col_Estado;
    @FXML private TableColumn<Room, String> col_Servicios;
    @FXML private Button btn_Buscar;
    @FXML private Button btn_FiltrarHabitaciones;
    @FXML private Button btn_Regresar;
    @FXML private Button btn_AgregarHabitacion;
    @FXML private Button btn_EditarHabitacion;
    @FXML private Button btn_EliminarHabitacion;

    // Controlador de lógica de negocio
    private final HotelRoomManagementController controller = new HotelRoomManagementController();
    
    // Lista observable para la tabla de habitaciones
    private ObservableList<Room> roomsList = FXCollections.observableArrayList();
    // Hotel seleccionado actualmente
    private Hotel selectedHotel;

    /**
     * Método de inicialización llamado automáticamente por JavaFX
     */
    @FXML
    void initialize() {
        try {
            System.out.println("Inicializando HotelRoomManagementController...");
            
            // Cargar el encabezado del administrador
            loadAdminHeader();
            
            // Configurar los tipos de habitación
            setupRoomTypes();
            
            // Configurar la tabla de habitaciones
            setupRoomTable();
            
            // Cargar hoteles disponibles
            loadAvailableHotels();
            
            // Configurar listeners
            setupListeners();
            
            System.out.println("HotelRoomManagementController inicializado completamente");
        } catch (Exception e) {
            handleInitializationError(e);
        }
    }
    
    /**
     * Carga el encabezado del administrador en el AnchorPane correspondiente
     */
    private void loadAdminHeader() {
        if (UserHeader0 == null) {
            System.err.println("UserHeader0 es nulo en HotelRoomManagementController");
            showAlert("Error de UI", 
                "No se pudo cargar el cabezal del administrador. Esto puede afectar la funcionalidad.", 
                AlertType.WARNING);
            return;
        }
        
        System.out.println("Cargando AdminHeader en UserHeader0...");
        
        try {
            // Cargar el FXML del encabezado de administrador
            ViewLoader.setContent(UserHeader0, "AdminHeader");
            System.out.println("AdminHeader cargado exitosamente");
        } catch (Exception e) {
            System.err.println("Error al cargar AdminHeader: " + e.getMessage());
            
            try {
                // Segundo intento con ruta completa
                System.out.println("Intentando ruta alternativa para AdminHeader...");
                ViewLoader.setContent(UserHeader0, "co/edu/uniquindio/poo/bookyourstary/AdminHeader");
                System.out.println("AdminHeader cargado exitosamente con ruta alternativa");
            } catch (Exception ex) {
                System.err.println("Error en carga alternativa de AdminHeader: " + ex.getMessage());
                ex.printStackTrace();
                showAlert("Error de UI", 
                    "No se pudo cargar el cabezal del administrador. Detalles: " + ex.getMessage(), 
                    AlertType.WARNING);
            }
        }
    }
      /**
     * Configura los tipos de habitación disponibles
     */
    private void setupRoomTypes() {
        combo_TipoHabitacion.getItems().clear();
        combo_TipoHabitacion.getItems().addAll(controller.getAvailableRoomTypes());
    }
    
    /**
     * Configura la tabla de habitaciones
     */
    private void setupRoomTable() {        // Configurar columnas de la tabla
        col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_Numero.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        col_TipoHabitacion.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        col_Capacidad.setCellValueFactory(new PropertyValueFactory<>("maxGuests")); // Usando maxGuests en lugar de capacity
        col_Precio.setCellValueFactory(new PropertyValueFactory<>("priceForNight")); // Usando priceForNight en lugar de pricePerNight
        col_Estado.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Para la columna de servicios, necesitamos un tratamiento especial
        col_Servicios.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            StringBuilder services = new StringBuilder();
            
            if (room.hasWifi()) services.append("WiFi, ");
            if (room.hasMinibar()) services.append("Mini bar, ");
            if (room.hasAirConditioning()) services.append("A/C, ");
            if (room.hasTV()) services.append("TV, ");
            if (room.includesBreakfast()) services.append("Desayuno");
            
            String result = services.toString();
            if (result.endsWith(", ")) {
                result = result.substring(0, result.length() - 2);
            }
            
            return new SimpleStringProperty(result);
        });
        
        // Asignar la lista observable a la tabla
        table_Habitaciones.setItems(roomsList);
    }
      /**
     * Carga los hoteles disponibles en el sistema
     */
    private void loadAvailableHotels() {
        try {
            // Verificar que el usuario actual es un administrador
            if (!controller.isCurrentUserAdmin()) {
                showAlert("Acceso denegado", 
                    "Solo los administradores pueden acceder a esta funcionalidad.", 
                    AlertType.ERROR);
                
                // Regresar a la página principal
                MainController.loadScene("home", 1024, 768);
                return;
            }
            
            // Obtener todos los hoteles usando el controlador
            List<Hotel> hotels = controller.getAllHotels();
            
            if (hotels.isEmpty()) {
                showAlert("Sin hoteles", 
                    "No hay hoteles registrados en el sistema.", 
                    AlertType.INFORMATION);
                return;
            }
            
            // Cargar los hoteles en el combo box
            combo_Hotel.setItems(FXCollections.observableArrayList(hotels));
            
            // Configurar el toString para mostrar el nombre del hotel
            combo_Hotel.setCellFactory(param -> new javafx.scene.control.ListCell<Hotel>() {
                @Override
                protected void updateItem(Hotel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });
            
            combo_Hotel.setButtonCell(new javafx.scene.control.ListCell<Hotel>() {
                @Override
                protected void updateItem(Hotel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });
            
            // Seleccionar el primer hotel si hay disponibles
            if (!combo_Hotel.getItems().isEmpty()) {
                combo_Hotel.getSelectionModel().selectFirst();
                selectedHotel = combo_Hotel.getValue();
                loadHotelRooms(selectedHotel);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar hoteles disponibles: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudieron cargar los hoteles: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Configura los listeners para los componentes
     */
    private void setupListeners() {
        // Listener para cuando se selecciona un hotel
        combo_Hotel.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                selectedHotel = newValue;
                loadHotelRooms(selectedHotel);
            }
        });
    }
      /**
     * Carga las habitaciones de un hotel específico
     * @param hotel Hotel del cual cargar las habitaciones
     */
    private void loadHotelRooms(Hotel hotel) {
        if (hotel == null) {
            return;
        }
        
        try {
            // Obtener las habitaciones del hotel usando el controlador
            List<Room> rooms = controller.getRoomsFromHotel(hotel);
            
            // Actualizar la lista observable
            roomsList.clear();
            if (!rooms.isEmpty()) {
                roomsList.addAll(rooms);
            }
            
            System.out.println("Cargadas " + roomsList.size() + " habitaciones para el hotel " + hotel.getName());
        } catch (Exception e) {
            System.err.println("Error al cargar habitaciones: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudieron cargar las habitaciones: " + e.getMessage(), AlertType.ERROR);
        }
    }
      /**
     * Busca habitaciones según el texto ingresado
     */
    @FXML
    void buscarHabitaciones(ActionEvent event) {
        if (selectedHotel == null) {
            showAlert("Hotel no seleccionado", "Por favor seleccione un hotel primero.", AlertType.WARNING);
            return;
        }
        
        String searchText = txt_Buscar.getText().trim();
        if (searchText.isEmpty()) {
            // Si no hay texto de búsqueda, mostrar todas las habitaciones
            loadHotelRooms(selectedHotel);
            return;
        }
        
        try {
            // Usar el controlador para buscar habitaciones
            List<Room> rooms = controller.searchRooms(selectedHotel, searchText);
            
            // Actualizar la lista observable
            roomsList.clear();
            roomsList.addAll(rooms);
            
            System.out.println("Encontradas " + rooms.size() + " habitaciones que coinciden con la búsqueda");
        } catch (Exception e) {
            System.err.println("Error al buscar habitaciones: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudieron buscar las habitaciones: " + e.getMessage(), AlertType.ERROR);
        }
    }
      /**
     * Filtra habitaciones según los criterios especificados
     */
    @FXML
    void filtrarHabitaciones(ActionEvent event) {
        if (selectedHotel == null) {
            showAlert("Hotel no seleccionado", "Por favor seleccione un hotel primero.", AlertType.WARNING);
            return;
        }
        
        try {
            // Obtener los filtros seleccionados
            String tipoHabitacion = combo_TipoHabitacion.getValue();
            
            // Capacidad
            Integer capacidad = null;
            if (txt_Capacidad.getText() != null && !txt_Capacidad.getText().trim().isEmpty()) {
                try {
                    capacidad = Integer.valueOf(txt_Capacidad.getText().trim());
                } catch (NumberFormatException e) {
                    // Si no es un número válido, ignoramos este filtro
                }
            }
            
            // Rango de precios
            Double precioMin = null;
            Double precioMax = null;
            
            if (txt_PrecioMin.getText() != null && !txt_PrecioMin.getText().trim().isEmpty()) {
                try {
                    precioMin = Double.valueOf(txt_PrecioMin.getText().trim());
                } catch (NumberFormatException e) {
                    // Si no es un número válido, ignoramos este filtro
                }
            }
            
            if (txt_PrecioMax.getText() != null && !txt_PrecioMax.getText().trim().isEmpty()) {
                try {
                    precioMax = Double.valueOf(txt_PrecioMax.getText().trim());
                } catch (NumberFormatException e) {
                    // Si no es un número válido, ignoramos este filtro
                }
            }
            
            // Servicios
            Boolean wifi = check_Wifi.isSelected() ? true : null;
            Boolean minibar = check_MiniBar.isSelected() ? true : null;
            Boolean ac = check_AireAcondicionado.isSelected() ? true : null;
            Boolean tv = check_TV.isSelected() ? true : null;
            Boolean desayuno = check_Desayuno.isSelected() ? true : null;
            
            // Usar el controlador para filtrar habitaciones
            List<Room> rooms = controller.filterRooms(
                selectedHotel, tipoHabitacion, capacidad, precioMin, precioMax,
                wifi, minibar, ac, tv, desayuno
            );
            
            // Actualizar la lista observable
            roomsList.clear();
            roomsList.addAll(rooms);
            
            System.out.println("Encontradas " + rooms.size() + " habitaciones después del filtrado");
        } catch (Exception e) {
            System.err.println("Error al filtrar habitaciones: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudieron filtrar las habitaciones: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Regresa a la pantalla anterior
     */
    @FXML
    void regresarMenu(ActionEvent event) {
        try {
            // Volver al menú de administrador
            MainController.loadScene("MenuAdmin", 1280, 720);
        } catch (Exception e) {
            System.err.println("Error al regresar al menú: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudo regresar al menú: " + e.getMessage(), AlertType.ERROR);
        }
    }
      /**
     * Abre el formulario para agregar una nueva habitación
     */
    @FXML
    void agregarHabitacion(ActionEvent event) {
        if (selectedHotel == null) {
            showAlert("Hotel no seleccionado", "Por favor seleccione un hotel primero.", AlertType.WARNING);
            return;
        }
        
        try {
            // Aquí se abriría un formulario para agregar una nueva habitación
            showAlert("Funcionalidad en desarrollo", 
                "La funcionalidad para agregar habitaciones está en desarrollo.", 
                AlertType.INFORMATION);
            
            // En una implementación real, aquí se abriría un nuevo formulario o diálogo
            // y luego se usaría el controlador para agregar la habitación:
            // boolean success = controller.addRoom(selectedHotel, newRoom);
            
            // Después de añadir, actualizar la lista
            // if (success) {
            //     loadHotelRooms(selectedHotel);
            // }
        } catch (Exception e) {
            System.err.println("Error al abrir formulario de agregar habitación: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el formulario: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Abre el formulario para editar la habitación seleccionada
     */
    @FXML
    void editarHabitacion(ActionEvent event) {
        Room selectedRoom = table_Habitaciones.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Habitación no seleccionada", 
                "Por favor seleccione una habitación para editar.", 
                AlertType.WARNING);
            return;
        }
        
        try {
            // Aquí se abriría un formulario para editar la habitación seleccionada
            showAlert("Funcionalidad en desarrollo", 
                "La funcionalidad para editar habitaciones está en desarrollo.", 
                AlertType.INFORMATION);
            
            // En una implementación real, aquí se abriría un nuevo formulario o diálogo
            // y luego se usaría el controlador para actualizar la habitación:
            // boolean success = controller.updateRoom(selectedHotel, updatedRoom);
            
            // Después de actualizar, recargar la lista
            // if (success) {
            //     loadHotelRooms(selectedHotel);
            // }
        } catch (Exception e) {
            System.err.println("Error al abrir formulario de editar habitación: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el formulario: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Elimina la habitación seleccionada
     */
    @FXML
    void eliminarHabitacion(ActionEvent event) {
        Room selectedRoom = table_Habitaciones.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Habitación no seleccionada", 
                "Por favor seleccione una habitación para eliminar.", 
                AlertType.WARNING);
            return;
        }
        
        try {
            // Aquí se mostraría un diálogo de confirmación y se eliminaría la habitación
            showAlert("Funcionalidad en desarrollo", 
                "La funcionalidad para eliminar habitaciones está en desarrollo.", 
                AlertType.INFORMATION);
            
            // En una implementación real, aquí se mostraría un diálogo de confirmación
            // y luego se usaría el controlador para eliminar la habitación:
            // boolean success = controller.deleteRoom(selectedHotel, selectedRoom);
            
            // Después de eliminar, actualizar la lista
            // if (success) {
            //     roomsList.remove(selectedRoom);
            //     table_Habitaciones.getSelectionModel().clearSelection();
            // }
        } catch (Exception e) {
            System.err.println("Error al eliminar habitación: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudo eliminar la habitación: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja errores durante la inicialización
     * @param e Excepción capturada
     */
    private void handleInitializationError(Exception e) {
        System.err.println("Error durante la inicialización: " + e.getMessage());
        e.printStackTrace();
        showAlert("Error de inicialización", 
            "No se pudo inicializar correctamente la interfaz: " + e.getMessage(), 
            AlertType.ERROR);
    }
      /**
     * Muestra una alerta con el título y mensaje especificados
     * @param title Título de la alerta
     * @param message Mensaje a mostrar
     * @param type Tipo de alerta (ERROR, WARNING, INFORMATION, etc.)
     */
    private void showAlert(String title, String message, AlertType type) {
        MainController.showAlert(title, message, type);
    }
}
