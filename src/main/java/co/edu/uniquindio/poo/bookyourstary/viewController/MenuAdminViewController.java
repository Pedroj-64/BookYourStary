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
import co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.ViewLoader;

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
    private ImageView img_Alojamiento2;

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

    // Declaración de listas sin inicializar (inicializar en initialize())
    private List<ImageView> imageViews;
    private List<Label> titleLabels;
    private List<Label> descLabels;
    private List<Label> priceLabels;
    private List<Label> cityLabels;
    private List<Label> serviceLabels;
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
        try {
            Button sourceButton = (Button) event.getSource();
            if (sourceButton == null) {
                System.err.println("El botón origen es nulo");
                return;
            }

            Hosting hosting = (Hosting) sourceButton.getUserData();
            if (hosting != null) {
                System.out.println("Abriendo ventana de edición para: " + hosting.getName());
                menuAdminController.openEditHostingWindow(hosting);
                // Recargar los alojamientos después de la edición
                loadAndDisplayHostings();
            } else {
                System.err.println("No hay alojamiento asociado al botón");
                MainController.showAlert(
                        "Error",
                        "No se encontró el alojamiento a editar.",
                        AlertType.ERROR);
            }
        } catch (Exception e) {
            System.err.println("Error al abrir el menú de edición: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error",
                    "No se pudo abrir el editor: " + e.getMessage(),
                    AlertType.ERROR);
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
        try {
            Button sourceButton = (Button) event.getSource();
            if (sourceButton == null) {
                System.err.println("El botón origen es nulo");
                return;
            }

            Hosting hosting = (Hosting) sourceButton.getUserData();
            if (hosting != null) {
                System.out.println("Editando alojamiento: " + hosting.getName());
                menuAdminController.openEditHostingWindow(hosting);
                // Recargar los alojamientos después de la edición
                loadAndDisplayHostings();
            } else {
                System.err.println("No hay alojamiento asociado al botón de edición");
                MainController.showAlert(
                        "Error",
                        "No se encontró el alojamiento a editar.",
                        AlertType.ERROR);
            }
        } catch (Exception e) {
            System.err.println("Error al editar alojamiento: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error",
                    "No se pudo editar el alojamiento: " + e.getMessage(),
                    AlertType.ERROR);
        }
    }

    private void setupComboBox() {
        combo_tipoAlojamiento.getItems().setAll(MenuAdminController.getHostingTypes());
        combo_Ciudad.getItems().setAll(menuAdminController.getAvailableCities());
    }

    @FXML
    void initialize() {
        try {
            // Inicializar listas después de que los campos FXML han sido inyectados
            imageViews = List.of(img_Alojamiento, img_Alojamiento1, img_Alojamiento2, img_Alojamiento3, img_Alojamiento4);
            titleLabels = List.of(lbl_tituloAlojamiento, lbl_tituloAlojamiento1, lbl_tituloAlojamiento2, lbl_tituloAlojamiento3, lbl_tituloAlojamiento4);
            descLabels = List.of(lbl_descripcion, lbl_descripcion1, lbl_descripcion2, lbl_descripcion3, lbl_descripcion4);
            priceLabels = List.of(lbl_Price, lbl_Price1, lbl_Price2, lbl_Price3, lbl_Price4);
            cityLabels = List.of(lbl_Cuidad, lbl_Cuidad1, lbl_Cuidad2, lbl_Cuidad3, lbl_Cuidad4);
            serviceLabels = List.of(lbl_serviciosAdicionales, lbl_serviciosAdicionales1, lbl_serviciosAdicionales2, lbl_serviciosAdicionales3, lbl_serviciosAdicionales4);
            editButtons = List.of(btn_editing, btn_editing1, btn_editing2, btn_editing3, btn_editigin4);

            System.out.println("Inicializando MenuAdminViewController...");
            // Setup combobox first
            setupComboBox();

            // Load the admin header
            if (UserHeader0 != null) {
                System.out.println("Cargando AdminHeader en UserHeader0...");
                try {
                    ViewLoader.setContent(UserHeader0, "AdminHeader");
                    System.out.println("AdminHeader cargado exitosamente");
                } catch (Exception e) {
                    System.err.println("Error al cargar AdminHeader: " + e.getMessage());
                    e.printStackTrace();

                    // Intentar carga alternativa con ruta completa
                    try {
                        System.out.println("Intentando ruta alternativa para AdminHeader...");
                        ViewLoader.setContent(UserHeader0, "co/edu/uniquindio/poo/bookyourstary/AdminHeader");
                        System.out.println("AdminHeader cargado exitosamente con ruta alternativa");
                    } catch (Exception ex) {
                        System.err.println("Error en carga alternativa de AdminHeader: " + ex.getMessage());
                        ex.printStackTrace();
                        MainController.showAlert("Error de UI",
                                "No se pudo cargar el cabezal del administrador. Detalles: " + ex.getMessage(),
                                AlertType.WARNING);
                    }
                }
            } else {
                System.err.println("UserHeader0 es nulo en MenuAdminViewController.initialize()");
                MainController.showAlert("Error de UI",
                        "No se pudo cargar el cabezal del administrador. Esto puede afectar la funcionalidad.",
                        AlertType.WARNING);
            }

            // Initialize and load the hostings display
            try {
                System.out.println("Inicializando la vista de alojamientos...");
                // Cargar y mostrar alojamientos
                loadAndDisplayHostings();
                System.out.println("Vista de alojamientos inicializada correctamente");
            } catch (Exception e) {
                System.err.println("Error al inicializar la vista de alojamientos: " + e.getMessage());
                e.printStackTrace();
                MainController.showAlert("Error de inicialización",
                        "No se pudieron cargar los alojamientos: " + e.getMessage(),
                        AlertType.ERROR);
            }

            System.out.println("MenuAdminViewController inicializado completamente");

        } catch (Exception e) {
            System.err.println("Error general en la inicialización de MenuAdminViewController: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert("Error de inicialización",
                    "No se pudo inicializar el panel de administración: " + e.getMessage(),
                    AlertType.ERROR);
        }
    }

    private void loadAndDisplayHostings() {
        try {
            // Cargar alojamientos
            currentHostings = menuAdminController.getRandomHostings();
            System.out.println("Alojamientos cargados: " + (currentHostings != null ? currentHostings.size() : 0));

            // Verificar que los elementos necesarios para mostrar los alojamientos existan
            boolean hasImageViews = imageViews != null && !imageViews.isEmpty();
            boolean hasTitleLabels = titleLabels != null && !titleLabels.isEmpty();
            boolean hasDescLabels = descLabels != null && !descLabels.isEmpty();
            boolean hasPriceLabels = priceLabels != null && !priceLabels.isEmpty();
            boolean hasCityLabels = cityLabels != null && !cityLabels.isEmpty();
            boolean hasServiceLabels = serviceLabels != null && !serviceLabels.isEmpty();
            boolean hasEditButtons = editButtons != null && !editButtons.isEmpty();

            if (!hasImageViews) System.err.println("imageViews es nulo o vacío");
            if (!hasTitleLabels) System.err.println("titleLabels es nulo o vacío");
            if (!hasDescLabels) System.err.println("descLabels es nulo o vacío");
            if (!hasPriceLabels) System.err.println("priceLabels es nulo o vacío");
            if (!hasCityLabels) System.err.println("cityLabels es nulo o vacío");
            if (!hasServiceLabels) System.err.println("serviceLabels es nulo o vacío");
            if (!hasEditButtons) System.err.println("editButtons es nulo o vacío");

            // Verificar si podemos mostrar los alojamientos
            if (currentHostings != null && !currentHostings.isEmpty() &&
                    hasImageViews && hasTitleLabels && hasDescLabels &&
                    hasPriceLabels && hasCityLabels && hasServiceLabels) {

                System.out.println("Actualizando visualización de alojamientos...");
                menuAdminController.updateHostingDisplay(
                        currentHostings, imageViews, titleLabels, descLabels,
                        priceLabels, cityLabels, serviceLabels
                );

                // Asignar alojamientos a los botones de edición si están disponibles
                if (hasEditButtons) {
                    menuAdminController.assignHostingsToEditButtons(editButtons, currentHostings);
                    System.out.println("Alojamientos asignados a botones de edición");
                }

                System.out.println("Visualización de alojamientos actualizada exitosamente");
            } else {
                System.err.println("No se puede mostrar la lista de alojamientos porque faltan elementos en la interfaz o no hay alojamientos");
                // Intentar mostrar al menos una notificación al usuario
                if (contenedorPrincipal != null) {
                    // Si no se pueden mostrar los alojamientos pero el contenedor principal existe,
                    // al menos mostrar un mensaje en la interfaz
                    if (currentHostings == null || currentHostings.isEmpty()) {
                        MainController.showAlert("Sin alojamientos",
                                "No hay alojamientos disponibles para mostrar.",
                                AlertType.INFORMATION);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar y mostrar los alojamientos: " + e.getMessage());
            e.printStackTrace();
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
