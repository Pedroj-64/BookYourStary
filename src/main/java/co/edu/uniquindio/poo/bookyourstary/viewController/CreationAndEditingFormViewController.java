package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.CreationAndEditingFormController;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CreationAndEditingFormViewController {


    @FXML
    private Button BtnGuardar;

    @FXML
    private Button BtnSalir;

    @FXML
    private Button btn_CargarImagen;

    @FXML
    private ComboBox<City> cbCiudad;

    @FXML
    private ComboBox<String> cbPais;

    @FXML
    private ComboBox<String> cbTipoAlojamiento;

    @FXML
    private CheckBox chkDesayuno;

    @FXML
    private CheckBox chkPiscina;

    @FXML
    private CheckBox chkWifi;

    @FXML
    private DatePicker date_final;

    @FXML
    private DatePicker date_inicio;

    @FXML
    private ImageView imgFoto;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txt_MaxGuests;

    @FXML
    private TextArea txt_descripcion;

    // Instance of the logic controller
    private final CreationAndEditingFormController formLogicController = CreationAndEditingFormController.getInstance();
    private String selectedImagePath; // To store the path of the selected image    @FXML
    void cargarFoto(ActionEvent event) {
        Window parentWindow = btn_CargarImagen.getScene().getWindow();
        this.selectedImagePath = formLogicController.selectImage(parentWindow, imgFoto);
    }@FXML
    void guardarAlojamiento(ActionEvent event) {
        Stage stage = (Stage) BtnGuardar.getScene().getWindow();
        formLogicController.saveHostingAndClose(
            formTextFields,
            txt_descripcion,
            cbCiudad,
            cbTipoAlojamiento,
            cbPais,
            date_inicio,
            date_final,
            chkWifi,
            chkPiscina,
            chkDesayuno,
            this.selectedImagePath,
            stage
        );
    }

    @FXML
    void salirDelMenu(ActionEvent event) {
        Stage stage = (Stage) BtnSalir.getScene().getWindow();
        formLogicController.closeWindow(stage);
    }


    private java.util.List<TextField> formTextFields;    @FXML
    void initialize() {
        System.out.println("Inicializando CreationAndEditingFormViewController");
        
        // Inicializar la lista de campos de texto
        formTextFields = java.util.List.of(txtNombre, txtPrecio, txt_MaxGuests);
        
        // Inicializar ComboBox de países
        cbPais.getItems().setAll("Colombia");
        cbPais.setValue("Colombia");
        
        // Inicializar ComboBox de ciudades
        cbCiudad.getItems().setAll(formLogicController.getAvailableCities());
        
        // Inicializar ComboBox de tipos de alojamiento
        cbTipoAlojamiento.getItems().setAll(CreationAndEditingFormController.getHostingTypes());
        
        // Establecer los campos del formulario basados en el alojamiento a editar (si existe)
        setAllFieldsFromHosting();
        
        System.out.println("Inicialización de CreationAndEditingFormViewController completada");
    }

     public void setAllFieldsFromHosting() {
        System.out.println("Estableciendo campos del formulario desde el alojamiento");
        
        // Al configurar campos para un alojamiento existente, borrar cualquier ruta de imagen seleccionada previamente
        this.selectedImagePath = null; 
        
        try {
            // Verificar si los ComboBox están inicializados correctamente
            if (cbCiudad.getItems().isEmpty()) {
                System.out.println("Inicializando ComboBox de ciudades porque estaba vacío");
                cbCiudad.getItems().setAll(formLogicController.getAvailableCities());
            }
            
            if (cbTipoAlojamiento.getItems().isEmpty()) {
                System.out.println("Inicializando ComboBox de tipos de alojamiento porque estaba vacío");
                cbTipoAlojamiento.getItems().setAll(CreationAndEditingFormController.getHostingTypes());
            }
            
            // Si hostingToEdit tiene una imagen, setFormFieldsFromHosting en el controlador lógico la cargará en imgFoto.
            // selectedImagePath será null inicialmente para las ediciones, lo que significa "mantener la imagen actual" a menos que se elija una nueva.
            formLogicController.setFormFieldsFromHosting( // Usar la instancia del controlador lógico
                formTextFields,
                txt_descripcion,
                cbCiudad,
                cbTipoAlojamiento,
                cbPais,
                date_inicio,
                date_final,
                chkWifi,
                chkPiscina,
                chkDesayuno,
                imgFoto
            );
            
            System.out.println("Campos del formulario establecidos correctamente");
        } catch (Exception e) {
            System.err.println("Error al establecer los campos del formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
