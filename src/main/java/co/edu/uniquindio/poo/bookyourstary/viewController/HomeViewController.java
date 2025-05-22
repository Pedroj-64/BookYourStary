package co.edu.uniquindio.poo.bookyourstary.viewController;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import co.edu.uniquindio.poo.bookyourstary.controller.HomeController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.*;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.util.FilterData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class HomeViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane UserHeader0;

    @FXML
    private Button btn_Ayuda;

    @FXML
    private Button btn_BuscarFiltro;

    @FXML
    private Button btn_IniciarSesion;

    @FXML
    private Button btn_Siguiente;

    @FXML
    private Button btn_reservar;

    @FXML
    private Button btn_reservar1;

    @FXML
    private Button btn_reservar2;

    @FXML
    private Button btn_reservar3;

    @FXML
    private Button btn_reservar4;

    @FXML
    private Button btn_walletTopUp; // Button for wallet top-up

    @FXML
    private HBox central_btn;

    @FXML
    private CheckBox check_desayuno;

    @FXML
    private CheckBox check_piscina;

    @FXML
    private CheckBox check_wifi;

    @FXML
    private ComboBox<City> combo_Ciudad;

    @FXML
    private ComboBox<String> combo_tipoAlojamiento;

    @FXML
    private VBox contenedorPrincipal;

    @FXML
    private DatePicker date_fin;

    @FXML
    private DatePicker date_inicio;

    @FXML
    private ImageView img_Alojamiento;

    @FXML
    private ImageView img_Alojamiento1;

    @FXML
    private ImageView img_Alojamiento2;

    @FXML
    private ImageView img_Alojamiento3;

    @FXML
    private ImageView img_Alojamiento4;

    @FXML
    private Label lbl_Cuidad;

    @FXML
    private Label lbl_Cuidad1;

    @FXML
    private Label lbl_Cuidad2;

    @FXML
    private Label lbl_Cuidad3;

    @FXML
    private Label lbl_Cuidad4;

    @FXML
    private Label lbl_Price;

    @FXML
    private Label lbl_Price1;

    @FXML
    private Label lbl_Price2;

    @FXML
    private Label lbl_Price3;

    @FXML
    private Label lbl_Price4;

    @FXML
    private Label lbl_Registrate;

    @FXML
    private Label lbl_descripcion;

    @FXML
    private Label lbl_descripcion1;

    @FXML
    private Label lbl_descripcion2;

    @FXML
    private Label lbl_descripcion3;

    @FXML
    private Label lbl_descripcion4;

    @FXML
    private Label lbl_serviciosAdicionales;

    @FXML
    private Label lbl_serviciosAdicionales1;

    @FXML
    private Label lbl_serviciosAdicionales2;

    @FXML
    private Label lbl_serviciosAdicionales3;

    @FXML
    private Label lbl_serviciosAdicionales4;

    @FXML
    private Label lbl_tituloAlojamiento;

    @FXML
    private Label lbl_tituloAlojamiento1;

    @FXML
    private Label lbl_tituloAlojamiento2;

    @FXML
    private Label lbl_tituloAlojamiento3;

    @FXML
    private Label lbl_tituloAlojamiento4;

    @FXML
    private VBox panelAlojamientos;

    @FXML
    private VBox panelFiltros;

    @FXML
    private ScrollPane scrollAlojamientos;

    @FXML
    private TextField txt_maxPrecio;

    @FXML
    private TextField txt_minPrecio;

    @FXML
    private TextField txt_numHuespedes;

    @FXML
    private Label lbl_puto;

    private List<ImageView> imageViews;
    private List<Label> titleLabels;
    private List<Label> descLabels;
    private List<Label> priceLabels;
    private List<Label> cityLabels;
    private List<Label> serviceLabels;
    private List<Button> reserveButtons;

    private final HomeController homeController = new HomeController();

    private FilterData collectFilterData() {
        return getFilterData(combo_Ciudad, combo_tipoAlojamiento, txt_minPrecio, txt_maxPrecio, txt_numHuespedes, check_wifi, check_piscina, check_desayuno, date_inicio, date_fin);
    }

    @NotNull
    static FilterData getFilterData(ComboBox<City> comboCiudad, ComboBox<String> comboTipoAlojamiento, TextField txtMinPrecio, TextField txtMaxPrecio, TextField txtNumHuespedes, CheckBox checkWifi, CheckBox checkPiscina, CheckBox checkDesayuno, DatePicker dateInicio, DatePicker dateFin) {
        String ciudad = comboCiudad.getValue().getName() != null ? comboCiudad.getValue().toString() : null;
        String tipo = comboTipoAlojamiento.getValue() != null ? comboTipoAlojamiento.getValue().toString() : null;
        Double minPrecio = txtMinPrecio.getText().isEmpty() ? null : Double.valueOf(txtMinPrecio.getText());
        Double maxPrecio = txtMaxPrecio.getText().isEmpty() ? null : Double.valueOf(txtMaxPrecio.getText());
        Integer numHuespedes = txtNumHuespedes.getText().isEmpty() ? null
                : Integer.valueOf(txtNumHuespedes.getText());
        Boolean wifi = checkWifi.isSelected() ? true : null;
        Boolean piscina = checkPiscina.isSelected() ? true : null;
        Boolean desayuno = checkDesayuno.isSelected() ? true : null;
        LocalDate fechaInicio = dateInicio.getValue();
        LocalDate fechaFin = dateFin.getValue();
        return new FilterData(ciudad, tipo, minPrecio, maxPrecio, numHuespedes, wifi, piscina, desayuno, fechaInicio,
                fechaFin);
    }

    private void setupComboBox() {
        combo_tipoAlojamiento.getItems().setAll(HomeController.getHostingTypes());
        combo_Ciudad.getItems().setAll(homeController.getAvailableCities());
    }

    private void checkUserAndUpdateHeader() {
        Object user = MainController.getInstance().getSessionManager().getUsuarioActual();
        if (user instanceof Client) {
            ViewLoader.setContent(UserHeader0, "UserHeader");
        } else if (user instanceof Admin) {
            ViewLoader.setContent(UserHeader0, "AdminHeader");
        }
    }

    @FXML
    void ayudaBtn(ActionEvent event) {
        MainController.showAlert(
                "Ayuda",
                "Si tienes problemas, contacta con el equipo de margaDevSociety.",
                AlertType.INFORMATION);
    }

    @FXML
    void filtrar(ActionEvent event) {

        FilterData data = collectFilterData();
        List<Hosting> filtrados = homeController.filterHostings(
                data.ciudad, data.tipo, data.minPrecio, data.maxPrecio, data.fechaInicio, data.fechaFin,
                data.numHuespedes, data.wifi,
                data.piscina, data.desayuno, data.fechaInicio, data.fechaFin);
        homeController.updateHostingDisplay(filtrados, imageViews, titleLabels, descLabels, priceLabels, cityLabels,
                serviceLabels);

    }

    @FXML
    void reservar(ActionEvent event) {
        Object currentUser = MainController.getInstance().getSessionManager().getUsuarioActual();
        if (currentUser == null) {
            MainController.showAlert(
                    "Acción Requerida",
                    "Por favor, inicie sesión o regístrese para poder reservar.",
                    AlertType.INFORMATION);
            // Optionally, redirect to login: MainController.loadScene("userLogin", 900, 600);
            return;
        }
        // Ensure the user is a Client, though guests (null user) are handled above.
        // If there are other user types that shouldn't reserve, add checks here.
        if (!(currentUser instanceof Client)) {
             MainController.showAlert(
                    "Acción no permitida",
                    "Solo los clientes pueden realizar reservas.",
                    AlertType.WARNING);
            return;
        }

        Button sourceButton = (Button) event.getSource();
        Hosting hosting = (Hosting) sourceButton.getUserData();
        if (hosting != null) {
            boolean added = homeController.addHostingToPendingReservations(hosting); // Adds to CartManager
            if (added) {
                MainController.showAlert(
                        "Alojamiento Añadido",
                        "'" + hosting.getName() + "' ha sido añadido a sus reservas pendientes. Revise su carrito para confirmar.",
                        AlertType.INFORMATION);
            } else {
                MainController.showAlert(
                        "Información",
                        "'" + hosting.getName() + "' ya se encuentra en sus reservas pendientes.",
                        AlertType.INFORMATION);
            }
        } else {
            MainController.showAlert(
                    "Error",
                    "No se pudo seleccionar el alojamiento para la reserva.",
                    AlertType.ERROR);
        }
    }

    @FXML
    void iniciarSesion(ActionEvent event) { // Added/Re-added this method
        MainController.loadScene("userLogin", 900, 600);
    }

    @FXML
    void registrateYa(MouseEvent event) {
        MainController.loadScene("userRegister", 900, 600);

    }

    @FXML
    void refresh(ActionEvent event) {
        // This method will re-fetch and display random hostings, similar to initialize
        scrollAlojamientos.setHvalue(0); // Reset scroll position if applicable
        List<Hosting> randomHostings = homeController.getRandomHostings();
        homeController.assignHostingsToReserveButtons(reserveButtons, randomHostings); // Re-assign data to buttons
        homeController.updateHostingDisplay(randomHostings, imageViews, titleLabels, descLabels, priceLabels,
                cityLabels, serviceLabels);
        // Any other UI updates needed on refresh
    }

    // Removed the duplicate, older reservar method that was here.

    public void creationList() {
        imageViews = List.of(img_Alojamiento, img_Alojamiento1, img_Alojamiento2, img_Alojamiento3, img_Alojamiento4);
        titleLabels = List.of(lbl_tituloAlojamiento, lbl_tituloAlojamiento1, lbl_tituloAlojamiento2,
                lbl_tituloAlojamiento3, lbl_tituloAlojamiento4);
        descLabels = List.of(lbl_descripcion, lbl_descripcion1, lbl_descripcion2, lbl_descripcion3, lbl_descripcion4);
        priceLabels = List.of(lbl_Price, lbl_Price1, lbl_Price2, lbl_Price3, lbl_Price4);
        cityLabels = List.of(lbl_Cuidad, lbl_Cuidad1, lbl_Cuidad2, lbl_Cuidad3, lbl_Cuidad4);
        serviceLabels = List.of(lbl_serviciosAdicionales, lbl_serviciosAdicionales1, lbl_serviciosAdicionales2,
                lbl_serviciosAdicionales3, lbl_serviciosAdicionales4);
        reserveButtons = List.of(btn_reservar, btn_reservar1, btn_reservar2, btn_reservar3, btn_reservar4);

    }

    @FXML
    void initialize() {
        creationList();
        setupComboBox();
        checkUserAndUpdateHeader();
        homeController.updateButtonIfLoggedIn(btn_IniciarSesion);
        List<Hosting> randomHostings = homeController.getRandomHostings();
        homeController.assignHostingsToReserveButtons(reserveButtons, randomHostings);
        homeController.updateHostingDisplay(randomHostings, imageViews, titleLabels, descLabels, priceLabels,
                cityLabels, serviceLabels);

    }

    @FXML
    void openWalletTopUp(ActionEvent event) {
        try {
            MainController.loadFXML("walletTopUp");
        } catch (Exception e) {
            e.printStackTrace();
            MainController.showAlert("Error", "No se pudo abrir la ventana de recarga de billetera.", AlertType.ERROR);
        }
    }
}
