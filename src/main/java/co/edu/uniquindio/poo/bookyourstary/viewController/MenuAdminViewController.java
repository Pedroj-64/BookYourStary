package co.edu.uniquindio.poo.bookyourstary.viewController;

import java.time.LocalDate;
import java.util.List;

import co.edu.uniquindio.poo.bookyourstary.controller.MenuAdminController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.util.FilterData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenuAdminViewController {

    @FXML
    private AnchorPane UserHeader0;

    @FXML
    private Button btn_Ayuda;

    @FXML
    private Button btn_BuscarFiltro;

    @FXML
    private Button btn_CerrarSesion;

    @FXML
    private Button btn_Siguiente;

    @FXML
    private Button btn_Tuerquita;

    @FXML
    private Button btn_editigin4;

    @FXML
    private Button btn_editing;

    @FXML
    private Button btn_editing1;

    @FXML
    private Button btn_editing2;

    @FXML
    private Button btn_editing3;

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
    private ImageView img_Alojamiento3;

    @FXML
    private ImageView img_Alojamiento4;

    @FXML
    private ImageView img_Carrito;

    @FXML
    private ImageView img_Carrito1;

    @FXML
    private ImageView img_Carrito11;

    @FXML
    private ImageView img_Carrito111;

    @FXML
    private ImageView img_Carrito112;

    @FXML
    private ImageView img_Carrito12;

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
    private Label lbl_Nombre;

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
    private Label lbl_Saldo;

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

    private List<ImageView> imageViews = List.of(img_Alojamiento, img_Alojamiento1, img_Alojamiento3, img_Alojamiento4,
            img_Alojamiento4);
    private List<Label> titleLabels = List.of(lbl_tituloAlojamiento, lbl_tituloAlojamiento1, lbl_tituloAlojamiento2,
            lbl_tituloAlojamiento3, lbl_tituloAlojamiento4);
    private List<Label> descLabels = List.of(lbl_descripcion, lbl_descripcion1, lbl_descripcion2, lbl_descripcion3,
            lbl_descripcion4);
    private List<Label> priceLabels = List.of(lbl_Price, lbl_Price1, lbl_Price2, lbl_Price3, lbl_Price4);
    private List<Label> cityLabels = List.of(lbl_Cuidad, lbl_Cuidad1, lbl_Cuidad2, lbl_Cuidad3, lbl_Cuidad4);
    private List<Label> serviceLabels = List.of(lbl_serviciosAdicionales, lbl_serviciosAdicionales1,
            lbl_serviciosAdicionales2, lbl_serviciosAdicionales3, lbl_serviciosAdicionales4);
    private List<Button> editButtons;
    private List<Hosting> currentHostings;

    MenuAdminController menuAdminController = new MenuAdminController();

    private FilterData collectFilterData() {
        String ciudad = combo_Ciudad.getValue().getName() != null ? combo_Ciudad.getValue().toString() : null;
        String tipo = combo_tipoAlojamiento.getValue() != null ? combo_tipoAlojamiento.getValue().toString() : null;
        Double minPrecio = txt_minPrecio.getText().isEmpty() ? null : Double.valueOf(txt_minPrecio.getText());
        Double maxPrecio = txt_maxPrecio.getText().isEmpty() ? null : Double.valueOf(txt_maxPrecio.getText());
        Integer numHuespedes = txt_numHuespedes.getText().isEmpty() ? null
                : Integer.valueOf(txt_numHuespedes.getText());
        Boolean wifi = check_wifi.isSelected() ? true : null;
        Boolean piscina = check_piscina.isSelected() ? true : null;
        Boolean desayuno = check_desayuno.isSelected() ? true : null;
        LocalDate fechaInicio = date_inicio.getValue();
        LocalDate fechaFin = date_fin.getValue();
        return new FilterData(ciudad, tipo, minPrecio, maxPrecio, numHuespedes, wifi, piscina, desayuno, fechaInicio,
                fechaFin);
    }

    @FXML
    void CerrarSesion(ActionEvent event) {

        menuAdminController.logout();

    }

    @FXML
    void abrirMenuAdmin(ActionEvent event) {

        Button sourceButton = (Button) event.getSource();
        Hosting hosting = (Hosting) sourceButton.getUserData();
        if (hosting != null) {
            menuAdminController.openEditHostingWindow(hosting);
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
    void editing(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        Hosting hosting = (Hosting) sourceButton.getUserData();
        if (hosting != null) {
            menuAdminController.openEditHostingWindow(hosting);
        }
    }

    private void setupComboBox() {
        combo_tipoAlojamiento.getItems().setAll(MenuAdminController.getHostingTypes());
        combo_Ciudad.getItems().setAll(menuAdminController.getAvailableCities());
    }

    @FXML
    void initialize() {
        setupComboBox();
        editButtons = List.of(btn_editing, btn_editing1, btn_editing2, btn_editing3, btn_editigin4);
        currentHostings = menuAdminController.getRandomHostings();
        menuAdminController.updateHostingDisplay(currentHostings, imageViews, titleLabels, descLabels, priceLabels,
                cityLabels, serviceLabels);
        menuAdminController.assignHostingsToEditButtons(editButtons, currentHostings);
    }

    @FXML
    void filtrar(ActionEvent event) {
        FilterData data = collectFilterData();
        currentHostings = menuAdminController.filterHostings(
                data.ciudad, data.tipo, data.minPrecio, data.maxPrecio, data.fechaInicio, data.fechaFin,
                data.numHuespedes, data.wifi,
                data.piscina, data.desayuno, data.fechaInicio, data.fechaFin);
        menuAdminController.updateHostingDisplay(currentHostings, imageViews, titleLabels, descLabels, priceLabels,
                cityLabels, serviceLabels);
        menuAdminController.assignHostingsToEditButtons(editButtons, currentHostings);
    }

    @FXML
    void refresh(ActionEvent event) {

        scrollAlojamientos.setHvalue(0);
        currentHostings = menuAdminController.getRandomHostings();
        menuAdminController.updateHostingDisplay(currentHostings, imageViews, titleLabels, descLabels, priceLabels,
                cityLabels, serviceLabels);
        menuAdminController.assignHostingsToEditButtons(editButtons, currentHostings);
    }
}
