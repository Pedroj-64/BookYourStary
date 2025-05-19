package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.ManageOrderController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TextField txt_numHuespedes;

    private final ManageOrderController manageOrderController = new ManageOrderController();
    private ObservableList<Hosting> hostingsObservable = FXCollections.observableArrayList();
    private ObservableList<Double> pricesObservable = FXCollections.observableArrayList();
    private Hosting selectedHosting = null;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupListeners();
        setupSelectionHandler();
        loadData();
        btn_Reservar.setDisable(true);
    }

    private void setupTableColumns() {
        tbc_nombreAlojamiento.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbc_tipoAlojamiento.setCellValueFactory(cellData -> {
            Hosting hosting = cellData.getValue();
            String typeName = hosting != null ? hosting.getClass().getSimpleName() : "";
            return new SimpleObjectProperty<>(typeName);
        });
        tbc_precioAlojamiento.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        tbc_ValorUnitario.setCellValueFactory(cellData -> {
            int index = tbl_Valores.getItems().indexOf(cellData.getValue());
            if (index >= 0 && index < pricesObservable.size()) {
                return new SimpleObjectProperty<>(pricesObservable.get(index));
            }
            return new SimpleObjectProperty<>(0.0);
        });
    }

    private void setupListeners() {
        Date_inicio.valueProperty().addListener((obs, oldVal, newVal) -> onDateOrGuestsChanged());
        Date_Fin.valueProperty().addListener((obs, oldVal, newVal) -> onDateOrGuestsChanged());
        txt_numHuespedes.textProperty().addListener((obs, oldVal, newVal) -> onDateOrGuestsChanged());
    }

    private void setupSelectionHandler() {
        tbl_TablaCompras.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedHosting = newSel;
            if (selectedHosting != null) {
                Date_inicio.setValue(LocalDate.now());
                Date_Fin.setValue(null);
                txt_numHuespedes.clear();
                btn_Reservar.setDisable(true);
            }
        });
        Date_Fin.valueProperty().addListener((obs, oldVal, newVal) -> checkEnableReserveButton());
        txt_numHuespedes.textProperty().addListener((obs, oldVal, newVal) -> checkEnableReserveButton());
    }

    private void checkEnableReserveButton() {
        boolean enable = selectedHosting != null && Date_inicio.getValue() != null && Date_Fin.getValue() != null
                && !txt_numHuespedes.getText().isBlank();
        btn_Reservar.setDisable(!enable);
    }

    private void loadData() {
        List<Hosting> hostings = manageOrderController.getPendingHostings();
        hostingsObservable.setAll(hostings);
        tbl_TablaCompras.setItems(hostingsObservable);

        LocalDate startDate = Date_inicio.getValue();
        LocalDate endDate = Date_Fin.getValue();
        if (startDate != null && endDate != null) {
            List<Double> prices = manageOrderController.calculatePricesForPendingHostings(startDate, endDate);
            pricesObservable.setAll(prices);
            tbl_Valores.setItems(pricesObservable);
        } else {
            pricesObservable.clear();
            tbl_Valores.setItems(pricesObservable);
        }
        showDiscountsSummary();
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
        } catch (NumberFormatException e) {
            MainController.showAlert("Invalid guests", "Please enter a valid number of guests.",AlertType.INFORMATION);
            return;
        }
        // Reservar solo el hosting seleccionado
        boolean success = manageOrderController.confirmSingleBooking(selectedHosting, startDate, endDate, numGuests);
        if (success) {
            hostingsObservable.remove(selectedHosting);
            tbl_TablaCompras.getSelectionModel().clearSelection();
            selectedHosting = null;
            btn_Reservar.setDisable(true);
            MainController.showAlert("Next reservation", "Now reserve the next accommodation, please.",AlertType.INFORMATION);
        }
        loadData();
    }

    @FXML
    void RegresarHome(ActionEvent event) {
        MainController.loadScene("home",900,600);
    }

    @FXML
    void onDateOrGuestsChanged() {
        loadData();
        showDiscountsSummary();
    }

    private void showDiscountsSummary() {
        LocalDate startDate = Date_inicio.getValue();
        LocalDate endDate = Date_Fin.getValue();
        if (startDate != null && endDate != null) {
            manageOrderController.showAppliedDiscountsSummary(startDate, endDate, txt_DescuentosValorTotal);
        }
    }
}
