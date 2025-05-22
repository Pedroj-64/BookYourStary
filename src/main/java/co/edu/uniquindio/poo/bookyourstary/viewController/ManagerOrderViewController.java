package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.ManageOrderController;
import co.edu.uniquindio.poo.bookyourstary.controller.ReviewController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ManagerOrderViewController {

    @FXML
    private DatePicker Date_Fin;

    @FXML
    private DatePicker Date_inicio;

    @FXML
    private Button btn_Reservar;

    @FXML
    private Button btn_Regresar;

    @FXML
    private Button btn_Resena;

    @FXML
    private TableColumn<Hosting, Double> tbc_ValorUnitario;

    @FXML
    private TableColumn<Hosting, String> tbc_nombreAlojamiento;

    @FXML
    private TableColumn<Hosting, Double> tbc_precioAlojamiento;

    @FXML
    private TableColumn<Hosting, String> tbc_tipoAlojamiento;

    @FXML
    private TableView<Hosting> tbl_TablaCompras;

    @FXML
    private TableView<Double> tbl_Valores;

    @FXML
    private TextArea txt_DescuentosValorTotal;
    @FXML
    private TextField txt_numHuespedes; // Campo para número de huéspedes

    private final ManageOrderController manageOrderController = new ManageOrderController();
    private ObservableList<Hosting> hostingsObservable = FXCollections.observableArrayList();
    private ObservableList<Double> pricesObservable = FXCollections.observableArrayList();
    private Hosting selectedHosting = null;

    @FXML
    public void initialize() {
        try {
            System.out.println("Iniciando inicialización de ManagerOrderViewController...");

            // Inicializar las colecciones observables
            hostingsObservable = FXCollections.observableArrayList();
            pricesObservable = FXCollections.observableArrayList();

            // Configurar fecha de inicio con la fecha actual
            Date_inicio.setValue(LocalDate.now());

            // Configurar fecha de fin con el día siguiente por defecto
            Date_Fin.setValue(LocalDate.now().plusDays(1));

            // Establecer valor por defecto para número de huéspedes
            txt_numHuespedes.setText("1");

            // Configurar tablas y listeners en orden adecuado
            setupTableColumns();
            setupListeners();
            setupSelectionHandler();

            // Intentar utilizar el controlador de apoyo si existe
            try {
                co.edu.uniquindio.poo.bookyourstary.controller.ManageOrderViewController.initializeOrderView(this);
                System.out.println("Controlador de apoyo inicializado correctamente");
            } catch (Exception e) {
                System.out.println("No se pudo inicializar el controlador de apoyo: " + e.getMessage());
                // No es crítico, continuamos sin él
            }

            // Inicialmente, el botón de reservar está deshabilitado hasta que se seleccione un alojamiento
            btn_Reservar.setDisable(true);

            // Cargar datos iniciales
            loadData();

            System.out.println("Inicialización de ManagerOrderViewController completada con éxito");
        } catch (Exception e) {
            System.err.println("Error al inicializar ManagerOrderViewController: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error de Inicialización",
                    "Error al inicializar la vista de órdenes: " + e.getMessage(),
                    javafx.scene.control.Alert.AlertType.ERROR
            );
        }
    }

    // Getters para acceso desde el controlador de apoyo
    public DatePicker getDateInicio() {
        return Date_inicio;
    }

    public DatePicker getDateFin() {
        return Date_Fin;
    }

    public TextField getNumHuespedes() {
        return txt_numHuespedes;
    }

    private void setupTableColumns() {
        tbc_nombreAlojamiento.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbc_tipoAlojamiento.setCellValueFactory(cellData -> {
            Hosting hosting = cellData.getValue();
            String typeName = hosting != null ? hosting.getClass().getSimpleName() : "";
            return new SimpleObjectProperty<>(typeName);
        });
        tbc_precioAlojamiento.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));

        // Configuración para la columna de valor unitario en la tabla principal
        tbc_ValorUnitario.setCellValueFactory(cellData -> {
            int index = hostingsObservable.indexOf(cellData.getValue());
            if (index >= 0 && index < pricesObservable.size()) {
                return new SimpleObjectProperty<>(pricesObservable.get(index));
            }
            return new SimpleObjectProperty<>(0.0);
        });

        // Limpiamos y configuramos la columna de valores unitarios para la tabla de resumen
        tbl_Valores.getColumns().clear();

        // Configuración mejorada para la tabla de valores
        TableColumn<Double, Double> valorColumn = new TableColumn<>("Valor");
        valorColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
        valorColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        tbl_Valores.getColumns().add(valorColumn);
    }

    private void setupListeners() {
        Date_inicio.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Establecer fecha mínima para la fecha de fin
            if (newVal != null) {
                // Si la fecha de fin es anterior a la nueva fecha de inicio + 1 día, la actualizamos
                if (Date_Fin.getValue() == null || Date_Fin.getValue().isBefore(newVal.plusDays(1))) {
                    Date_Fin.setValue(newVal.plusDays(1));
                }

                // Establecer restricción para que no se pueda seleccionar una fecha antes de inicio+1
                Date_Fin.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(newVal.plusDays(1)));
                    }
                });
            }
            onDateOrGuestsChanged();
        });

        Date_Fin.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Cuando cambia la fecha de fin, actualizamos los datos pero verificamos que sea válida
            if (newVal != null && Date_inicio.getValue() != null && !newVal.isBefore(Date_inicio.getValue().plusDays(1))) {
                onDateOrGuestsChanged();
            }
        });

        // Asegurar que solo se puedan ingresar números en el campo de huéspedes
        txt_numHuespedes.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                // Si está vacío, permitirlo pero deshabilitar el botón de reservar
                checkEnableReserveButton();
                return;
            }

            // Verificar que solo sean dígitos y limitar a un máximo razonable
            if (!newVal.matches("\\d*")) {
                txt_numHuespedes.setText(newVal.replaceAll("[^\\d]", ""));
                return;
            }

            try {
                int numGuests = Integer.parseInt(newVal);
                if (numGuests <= 0) {
                    // Si es 0, lo dejamos pero el botón de reservar quedará deshabilitado
                    checkEnableReserveButton();
                    return;
                } else if (numGuests > 20) {  // Límite razonable
                    txt_numHuespedes.setText("20"); // Establecer máximo
                }
            } catch (NumberFormatException e) {
                // Si hay error de formato, no hacemos nada (ya validamos que sean solo dígitos)
            }

            onDateOrGuestsChanged();
        });
    }

    private void setupSelectionHandler() {
        // Listener para la selección en la tabla de alojamientos
        tbl_TablaCompras.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedHosting = newSel;
            if (selectedHosting != null) {
                System.out.println("Alojamiento seleccionado: " + selectedHosting.getName());

                // Mantener la fecha de inicio actual
                if (Date_inicio.getValue() == null) {
                    Date_inicio.setValue(LocalDate.now());
                }

                // Solo establecer la fecha de fin si está vacía o es inválida
                if (Date_Fin.getValue() == null || Date_Fin.getValue().isBefore(Date_inicio.getValue().plusDays(1))) {
                    Date_Fin.setValue(Date_inicio.getValue().plusDays(1));
                }

                // Establecer un valor por defecto para el número de huéspedes
                if (txt_numHuespedes.getText().isEmpty()) {
                    txt_numHuespedes.setText("1");
                }

                // Cargar los precios actualizados para este alojamiento
                loadData();

                // Verificar si podemos habilitar el botón de reservar
                checkEnableReserveButton();

                // Actualizar el resumen de descuentos para este alojamiento
                showDiscountsSummary();
            } else {
                System.out.println("Se ha cancelado la selección de alojamiento");
                btn_Reservar.setDisable(true);
            }
        });

        // Verificar el estado del botón cuando cambian las fechas o el número de huéspedes
        Date_inicio.valueProperty().addListener((obs, oldVal, newVal) -> checkEnableReserveButton());
        Date_Fin.valueProperty().addListener((obs, oldVal, newVal) -> checkEnableReserveButton());
        txt_numHuespedes.textProperty().addListener((obs, oldVal, newVal) -> checkEnableReserveButton());
    }

    private void checkEnableReserveButton() {
        boolean validDates = Date_inicio.getValue() != null && Date_Fin.getValue() != null
                && !Date_inicio.getValue().isAfter(Date_Fin.getValue());

        boolean validGuests = false;
        try {
            validGuests = !txt_numHuespedes.getText().isBlank()
                    && Integer.parseInt(txt_numHuespedes.getText()) > 0;
        } catch (NumberFormatException e) {
            validGuests = false;
        }

        boolean enable = selectedHosting != null && validDates && validGuests;
        btn_Reservar.setDisable(!enable);

        if (enable) {
            System.out.println("Botón de reserva habilitado para alojamiento: " + selectedHosting.getName());
        }
    }

    private void loadData() {
        try {
            // Cargar los alojamientos
            List<Hosting> hostings = manageOrderController.getPendingHostings();
            hostingsObservable.setAll(hostings);
            tbl_TablaCompras.setItems(hostingsObservable);

            System.out.println("Cargados " + hostings.size() + " alojamientos pendientes");

            // Calcular y mostrar precios
            LocalDate startDate = Date_inicio.getValue();
            LocalDate endDate = Date_Fin.getValue();

            // Verificar si tenemos fechas válidas para calcular precios
            if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
                try {
                    // Calcular precios basados en fechas
                    List<Double> prices = manageOrderController.calculatePricesForPendingHostings(startDate, endDate);
                    System.out.println("Precios calculados para " + prices.size() + " alojamientos");

                    // Actualizar los precios observables
                    pricesObservable.clear();
                    pricesObservable.addAll(prices);

                    // Actualizar la tabla de valores
                    tbl_Valores.setItems(pricesObservable);

                    // Forzar actualización de las tablas
                    tbl_TablaCompras.refresh();
                    tbl_Valores.refresh();
                } catch (Exception e) {
                    System.err.println("Error al calcular precios: " + e.getMessage());
                    e.printStackTrace();
                    MainController.showAlert("Error", "Error al calcular precios: " + e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
                    pricesObservable.clear();
                }
            } else {
                System.out.println("Fechas no válidas para calcular precios");
                pricesObservable.clear();
            }

            // Actualizar resumen de descuentos
            showDiscountsSummary();
        } catch (Exception e) {
            System.err.println("Error general al cargar datos: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert("Error", "Error al cargar los datos: " + e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void reservar(ActionEvent event) {
        if (selectedHosting == null)
            return;
        LocalDate startDate = Date_inicio.getValue();
        LocalDate endDate = Date_Fin.getValue();
        int numGuests;
        try {
            numGuests = Integer.parseInt(txt_numHuespedes.getText());
            if (numGuests <= 0) {
                MainController.showAlert("Error de validación", "El número de huéspedes debe ser mayor que cero.", AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            MainController.showAlert("Error de validación", "Por favor, ingrese un número válido de huéspedes.", AlertType.ERROR);
            return;
        }

        // Attempt to confirm the booking for the selected hosting
        boolean success = manageOrderController.confirmSingleBooking(selectedHosting, startDate, endDate, numGuests);

        if (success) {
            MainController.showAlert(
                    "Reserva Exitosa",
                    "La reserva para '" + selectedHosting.getName() + "' ha sido confirmada exitosamente.",
                    AlertType.INFORMATION
            );
            // Refresh data and UI state
            hostingsObservable.remove(selectedHosting); // Remove from local list
            tbl_TablaCompras.getSelectionModel().clearSelection();
            selectedHosting = null;
            btn_Reservar.setDisable(true); // Disable button as selection is cleared
            // loadData(); // Reload all data, or just update totals if applicable
        }
        // If success is false, ManageOrderController already showed an error alert.
        // We still reload data to reflect any partial changes or to clear selections.
        loadData();
    }

    @FXML
    void RegresarHome(ActionEvent event) {
        MainController.loadScene("home", 900, 600);
    }

    @FXML
    void onDateOrGuestsChanged() {
        loadData();
        showDiscountsSummary();
    }

    private void showDiscountsSummary() {
        try {
            LocalDate startDate = Date_inicio.getValue();
            LocalDate endDate = Date_Fin.getValue();

            // Verificar que tengamos fechas válidas y al menos un huésped
            if (startDate != null && endDate != null && !startDate.isAfter(endDate) &&
                    !txt_numHuespedes.getText().isEmpty() && Integer.parseInt(txt_numHuespedes.getText()) > 0) {

                // Usar el controlador de apoyo para mostrar los descuentos
                manageOrderController.showAppliedDiscountsSummary(startDate, endDate, txt_DescuentosValorTotal);

                // Alternativamente, usar el controlador de apoyo si existe (para compatibilidad)
                try {
                    co.edu.uniquindio.poo.bookyourstary.controller.ManageOrderViewController
                            .showDiscountSummary(startDate, endDate, txt_DescuentosValorTotal);
                } catch (Exception e) {
                    // Si falla el método estático, ya hemos mostrado los descuentos con nuestro controlador
                    System.out.println("Usando método alternativo para mostrar descuentos");
                }
            } else {
                // Si las fechas no son válidas, mostrar mensaje informativo
                if (startDate == null || endDate == null) {
                    txt_DescuentosValorTotal.setText("Por favor, seleccione fechas válidas para ver descuentos.");
                } else if (startDate.isAfter(endDate)) {
                    txt_DescuentosValorTotal.setText("La fecha de inicio no puede ser posterior a la fecha de fin.");
                } else if (txt_numHuespedes.getText().isEmpty() || Integer.parseInt(txt_numHuespedes.getText()) <= 0) {
                    txt_DescuentosValorTotal.setText("Por favor, ingrese un número válido de huéspedes.");
                } else {
                    txt_DescuentosValorTotal.setText("No hay descuentos disponibles para las fechas seleccionadas.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al mostrar descuentos: " + e.getMessage());
            e.printStackTrace();
            txt_DescuentosValorTotal.setText("Error al calcular descuentos. Intente de nuevo.");
        }
    }

    @FXML
    private void abrirFormularioResena() {
        try {
            // Verificar que se haya seleccionado un alojamiento
            if (selectedHosting == null) {
                MainController.showAlert("Error", "Por favor, selecciona un alojamiento para dejar una reseña.", Alert.AlertType.ERROR);
                return;
            }

            // Cargar el formulario de reseñas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/bookyourstary/reviewForm.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Configurar el controlador del formulario de reseñas
            ReviewController controller = loader.getController();
            controller.setSelectedHosting(selectedHosting);

            // Configurar la ventana como modal
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Dejar Reseña");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            MainController.showAlert("Error", "No se pudo abrir la ventana de reseñas.", Alert.AlertType.ERROR);
        }
    }
}


