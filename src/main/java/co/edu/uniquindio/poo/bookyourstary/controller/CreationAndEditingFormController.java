package co.edu.uniquindio.poo.bookyourstary.controller;

import java.util.LinkedList;
import java.util.List;

import co.edu.uniquindio.poo.bookyourstary.config.mapping.ServicesMapping;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;
import co.edu.uniquindio.poo.bookyourstary.util.UtilInterfaces;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreationAndEditingFormController {

    private Hosting hostingToEdit;

    private static CreationAndEditingFormController instance;

    private CreationAndEditingFormController() {
    }

    public static CreationAndEditingFormController getInstance() {
        if (instance == null) {
            instance = new CreationAndEditingFormController();
        }
        return instance;
    }

    public static List<String> getHostingTypes() {
        return UtilInterfaces.getHostingTypes();
    }

    public List<City> getAvailableCities() {
        return UtilInterfaces.getColombianCities();
    }

    /**
     * Settea todos los campos del formulario de edición/creación según el hosting
     * almacenado en la clase (hostingToEdit).
     * Si hostingToEdit es null, deja los campos como están y setea el país a Colombia.
     * Recibe las listas de campos relevantes para mantener el código limpio en el
     * ViewController.
     */
    public void setFormFieldsFromHosting(
            List<TextField> textFields,
            TextArea txtDescripcion,
            ComboBox<City> cbCiudad,
            ComboBox<String> cbTipoAlojamiento,
            ComboBox<String> cbPais,
            DatePicker dateInicio,
            DatePicker dateFinal,
            CheckBox chkWifi,
            CheckBox chkPiscina,
            CheckBox chkDesayuno,
            ImageView imgFoto) {
        if (hostingToEdit == null) {
            cbPais.getItems().setAll("Colombia");
            cbPais.setValue("Colombia");
            return;
        }
        textFields.get(0).setText(hostingToEdit.getName());
        txtDescripcion.setText(hostingToEdit.getDescription());
        textFields.get(1).setText(String.valueOf(hostingToEdit.getPricePerNight()));
        textFields.get(2).setText(String.valueOf(hostingToEdit.getMaxGuests()));
        dateInicio.setValue(hostingToEdit.getAvailableFrom());
        dateFinal.setValue(hostingToEdit.getAvailableTo());
        cbCiudad.setValue(hostingToEdit.getCity());
        if (cbTipoAlojamiento != null && hostingToEdit.getClass().getSimpleName() != null) {
            cbTipoAlojamiento.setValue(hostingToEdit.getClass().getSimpleName());
        }
        var hostingService = MainController.getInstance().getHostingService();
        chkWifi.setSelected(hostingService.hasWifi(hostingToEdit));
        chkPiscina.setSelected(hostingService.hasPool(hostingToEdit));
        chkDesayuno.setSelected(hostingService.hasBreakfast(hostingToEdit));
        if (hostingToEdit.getImageUrl() != null && !hostingToEdit.getImageUrl().isEmpty()) {
            imgFoto.setImage(new Image("file:" + hostingToEdit.getImageUrl()));
        }
    }

    /**
     * Abre un FileChooser para seleccionar una imagen, la asigna al ImageView y actualiza el imageUrl del hostingToEdit.
     * Solo permite seleccionar archivos de imagen (jpg, jpeg, png, gif).
     *
     * @param parentWindow la ventana padre para el FileChooser (puede ser null si no tienes una referencia directa)
     * @param imgFoto el ImageView donde se mostrará la imagen seleccionada
     * @return la ruta absoluta de la imagen seleccionada, o null si se cancela
     */
    public String selectImage(Window parentWindow, ImageView imgFoto) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(parentWindow);
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            if (imgFoto != null) {
                imgFoto.setImage(new Image("file:" + imagePath));
            }
            if (hostingToEdit != null) {
                hostingToEdit.setImageUrl(imagePath);
            }
            return imagePath;
        }
        return null;
    }

    /**
     * Guarda o actualiza un alojamiento según los datos del formulario.
     * Si hostingToEdit existe, lo actualiza; si no, crea uno nuevo y lo guarda.
     * Los servicios incluidos se generan dinámicamente según los checkboxes.
     */
    public void saveHosting(
            List<TextField> textFields,
            TextArea txtDescripcion,
            ComboBox<City> cbCiudad,
            ComboBox<String> cbTipoAlojamiento,
            ComboBox<String> cbPais,
            DatePicker dateInicio,
            DatePicker dateFinal,
            CheckBox chkWifi,
            CheckBox chkPiscina,
            CheckBox chkDesayuno,
            ImageView imgFoto) {
        var hostingService = MainController.getInstance().getHostingService();
        boolean isEdit = hostingToEdit != null;
        var includedServices = buildIncludedServicesList(chkWifi, chkPiscina, chkDesayuno);
        String tipoAlojamiento = cbTipoAlojamiento.getValue();
        if (isEdit) {
            hostingToEdit.setName(textFields.get(0).getText());
            hostingToEdit.setDescription(txtDescripcion.getText());
            hostingToEdit.setPricePerNight(Double.parseDouble(textFields.get(1).getText()));
            hostingToEdit.setMaxGuests(Integer.parseInt(textFields.get(2).getText()));
            hostingToEdit.setAvailableFrom(dateInicio.getValue());
            hostingToEdit.setAvailableTo(dateFinal.getValue());
            hostingToEdit.setCity(cbCiudad.getValue());
            hostingToEdit.setIncludedServices(includedServices);
            // Si no hay imagen seleccionada, asignar la predeterminada
            if (hostingToEdit.getImageUrl() == null || hostingToEdit.getImageUrl().isEmpty()) {
                hostingToEdit.setImageUrl("src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png");
            }
            hostingService.updateHosting(hostingToEdit);
        } else {
            String imageUrl = (imgFoto.getImage() != null && (hostingToEdit != null && hostingToEdit.getImageUrl() != null && !hostingToEdit.getImageUrl().isEmpty()))
                ? hostingToEdit.getImageUrl()
                : "src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png";
            if ("Casa".equalsIgnoreCase(tipoAlojamiento)) {
                hostingService.createHouse(
                    textFields.get(0).getText(),
                    cbCiudad.getValue(),
                    txtDescripcion.getText(),
                    imageUrl,
                    Double.parseDouble(textFields.get(1).getText()),
                    Integer.parseInt(textFields.get(2).getText()),
                    includedServices,
                    250, 
                    dateInicio.getValue(),
                    dateFinal.getValue()
                );
            } else if ("Apto".equalsIgnoreCase(tipoAlojamiento)) {
                hostingService.createApartament(
                    textFields.get(0).getText(),
                    cbCiudad.getValue(),
                    txtDescripcion.getText(),
                    imageUrl,
                    Double.parseDouble(textFields.get(1).getText()),
                    Integer.parseInt(textFields.get(2).getText()),
                    includedServices,
                    dateInicio.getValue(),
                    dateFinal.getValue()
                );
            } else if ("Hotel".equalsIgnoreCase(tipoAlojamiento)) {
                hostingService.createHotel(
                    textFields.get(0).getText(),
                    cbCiudad.getValue(),
                    txtDescripcion.getText(),
                    imageUrl,
                    Double.parseDouble(textFields.get(1).getText()),
                    Integer.parseInt(textFields.get(2).getText()),
                    includedServices,
                    new java.util.LinkedList<>(),
                    dateInicio.getValue(),
                    dateFinal.getValue()
                );
            }
        }
    }

    /**
     * Construye la lista de servicios incluidos según el estado de los checkboxes.
     */
    private LinkedList<ServiceIncluded> buildIncludedServicesList(
            CheckBox chkWifi, CheckBox chkPiscina, CheckBox chkDesayuno) {
        var services = new java.util.LinkedList<ServiceIncluded>();
        var mapping = ServicesMapping.getInstance();
        if (chkWifi.isSelected()) {
            services.add(mapping.getServiceByName("wifi"));
        }
        if (chkPiscina.isSelected()) {
            services.add(mapping.getServiceByName("piscina"));
        }
        if (chkDesayuno.isSelected()) {
            services.add(mapping.getServiceByName("desayuno"));
        }
        return services;
    }


}
