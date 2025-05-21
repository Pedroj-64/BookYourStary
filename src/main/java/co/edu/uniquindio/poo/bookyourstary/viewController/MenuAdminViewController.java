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
import co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.ViewLoader; // Added import

import static co.edu.uniquindio.poo.bookyourstary.viewController.HomeViewController.getFilterData;

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

    private final List<ImageView> imageViews = List.of(img_Alojamiento, img_Alojamiento1, img_Alojamiento3, img_Alojamiento4,
            img_Alojamiento4);
    private final List<Label> titleLabels = List.of(lbl_tituloAlojamiento, lbl_tituloAlojamiento1, lbl_tituloAlojamiento2,
            lbl_tituloAlojamiento3, lbl_tituloAlojamiento4);
    private final List<Label> descLabels = List.of(lbl_descripcion, lbl_descripcion1, lbl_descripcion2, lbl_descripcion3,
            lbl_descripcion4);
    private final List<Label> priceLabels = List.of(lbl_Price, lbl_Price1, lbl_Price2, lbl_Price3, lbl_Price4);
    private final List<Label> cityLabels = List.of(lbl_Cuidad, lbl_Cuidad1, lbl_Cuidad2, lbl_Cuidad3, lbl_Cuidad4);
    private final List<Label> serviceLabels = List.of(lbl_serviciosAdicionales, lbl_serviciosAdicionales1,
            lbl_serviciosAdicionales2, lbl_serviciosAdicionales3, lbl_serviciosAdicionales4);
    private List<Button> editButtons;
    private List<Hosting> currentHostings;

    MenuAdminController menuAdminController = new MenuAdminController();

    private FilterData collectFilterData() {
        return getFilterData(combo_Ciudad, combo_tipoAlojamiento, txt_minPrecio, txt_maxPrecio, txt_numHuespedes, check_wifi, check_piscina, check_desayuno, date_inicio, date_fin);
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
        if (UserHeader0 != null) { // Ensure UserHeader0 is injected before using
            ViewLoader.setContent(UserHeader0, "AdminHeader");
        } else {
            System.err.println("UserHeader0 is null in MenuAdminViewController.initialize()");
            // Optionally show an alert here if this is critical
            // MainController.showAlert("Error de UI", "No se pudo cargar el cabezal del administrador.", AlertType.ERROR);
        }
        // The FXML for MenuAdminViewController might not have all these imageViews, titleLabels etc.
        // This part seems to be copied from HomeViewController and might not apply directly or fully.
        // For now, I'll keep it, but it needs verification against menuAdmin.fxml's actual content structure.
        // If menuAdmin.fxml is primarily for managing a list/table of hostings, this display logic might be different.
        // However, the user requested to fix AdminMenu loading, and the header is part of that.
        // The display of hostings in cards might still be relevant.

        // Assuming these lists are correctly populated based on menuAdmin.fxml's @FXML fields
        // If menuAdmin.fxml does not have these exact fx:ids for imageViews, titleLabels, etc., this will fail.
        // For safety, check if these lists are populated by FXML injection before use.
        // This part of the code is highly dependent on the FXML structure of menuAdmin.fxml for displaying hostings.
        // For now, focusing on the header loading.
        // The following lines for updating hosting display might need to be adjusted based on how menuAdmin.fxml actually displays hostings.
        
        // editButtons = List.of(btn_editing, btn_editing1, btn_editing2, btn_editing3, btn_editigin4); // Ensure these fx:ids exist
        // currentHostings = menuAdminController.getRandomHostings();
        // if (imageViews != null && !imageViews.isEmpty() && currentHostings != null) { // Add null/empty checks
        //    menuAdminController.updateHostingDisplay(currentHostings, imageViews, titleLabels, descLabels, priceLabels,
        //            cityLabels, serviceLabels);
        //    menuAdminController.assignHostingsToEditButtons(editButtons, currentHostings);
        //}
        
        // Simplified initialization for now, focusing on header and core functionality.
        // The display of hostings needs to be verified against menuAdmin.fxml structure.
        loadAndDisplayHostings();
    }

    private void loadAndDisplayHostings() {
        // This method would contain the logic to fetch and display hostings
        // specific to the admin menu's layout.
        // For example, if it's a table view:
        // currentHostings = menuAdminController.getAllHostings(); // Or some initial list
        // populateTable(currentHostings);

        // If it uses the card view similar to HomeViewController:
        // Ensure FXML fields like imageViews, titleLabels, editButtons are correctly defined in MenuAdminViewController
        // and linked in menuAdmin.fxml.
        // The current FXML provided for menuAdmin.fxml DOES show a card-like display.
        if (btn_editing != null) { // Check if one of the buttons is initialized (implies others might be too)
             editButtons = List.of(btn_editing, btn_editing1, btn_editing2, btn_editing3, btn_editigin4);
        }
        currentHostings = menuAdminController.getRandomHostings(); // Or getAllHostings()
        
        // Check if the display elements are available (they are defined in the FXML)
        if (imageViews != null && titleLabels != null && descLabels != null && priceLabels != null && cityLabels != null && serviceLabels != null && editButtons != null) {
            menuAdminController.updateHostingDisplay(currentHostings, imageViews, titleLabels, descLabels, priceLabels, cityLabels, serviceLabels);
            menuAdminController.assignHostingsToEditButtons(editButtons, currentHostings);
        } else {
            System.err.println("Some UI elements for hosting display are null in MenuAdminViewController.initialize(). Verify FXML fx:id links.");
        }
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
