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
    private String selectedImagePath; // To store the path of the selected image

    @FXML
    void cargarFoto(ActionEvent event) {
        Window parentWindow = btn_CargarImagen.getScene().getWindow();
        // selectImage now returns the path, store it
        this.selectedImagePath = formLogicController.selectImage(parentWindow, imgFoto);
    }

    @FXML
    void guardarAlojamiento(ActionEvent event) {
        formLogicController.saveHosting( // Use the logic controller instance
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
            this.selectedImagePath // Pass the stored image path string
        );
        // Optionally, close the window or reset the form after saving,
        // depending on whether saveHosting indicates success and if it's desired.
        // For now, alerts are handled within the logic controller.
    }

    @FXML
    void salirDelMenu(ActionEvent event) {
        formLogicController.setHostingToEdit(null); // Use the renamed variable
        BtnSalir.getScene().getWindow().hide();
    }


    private java.util.List<TextField> formTextFields;

    @FXML
    void initialize() {
        formTextFields = java.util.List.of(txtNombre, txtPrecio, txt_MaxGuests);
        cbPais.getItems().setAll("Colombia");
        cbPais.setValue("Colombia");
        setAllFieldsFromHosting();
    }

 

    public void setAllFieldsFromHosting() {
        // When setting fields for an existing hosting, clear any previously selected new image path
        this.selectedImagePath = null; 
        // If hostingToEdit has an image, setFormFieldsFromHosting in logic controller will load it into imgFoto.
        // The selectedImagePath will be null initially for edits, meaning "keep current image" unless a new one is chosen.
        formLogicController.setFormFieldsFromHosting( // Use the logic controller instance
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
    }

}
