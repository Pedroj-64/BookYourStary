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
            try {
                imgFoto.setImage(new Image("file:" + hostingToEdit.getImageUrl()));
            } catch (Exception e) {
                imgFoto.setImage(new Image("file:src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png"));
                 MainController.showAlert("Advertencia", "No se pudo cargar la imagen: " + hostingToEdit.getImageUrl(), Alert.AlertType.WARNING);
            }
        } else {
             imgFoto.setImage(new Image("file:src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png"));
        }
    }

    public String selectImage(Window parentWindow, ImageView imgFoto) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(parentWindow);
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            if (imgFoto != null) {
                 try {
                    imgFoto.setImage(new Image("file:" + imagePath));
                } catch (Exception e) {
                    imgFoto.setImage(new Image("file:src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png"));
                    MainController.showAlert("Error", "No se pudo cargar la imagen seleccionada.", Alert.AlertType.ERROR);
                    return null;
                }
            }
            // Do not set hostingToEdit.setImageUrl here directly, let saveHosting handle it.
            return imagePath;
        }
        return null;
    }

    public void saveHosting(
            List<TextField> textFields, // 0: name, 1: price, 2: guests
            TextArea txtDescripcion,
            ComboBox<City> cbCiudad,
            ComboBox<String> cbTipoAlojamiento,
            ComboBox<String> cbPais, // Unused in current save logic but kept for signature
            DatePicker dateInicio,
            DatePicker dateFinal,
            CheckBox chkWifi,
            CheckBox chkPiscina,
            CheckBox chkDesayuno,
            String selectedImagePath) { // Changed ImageView to selectedImagePath

        var hostingService = MainController.getInstance().getHostingService();
        boolean isEdit = hostingToEdit != null;
        var includedServices = buildIncludedServicesList(chkWifi, chkPiscina, chkDesayuno);
        String tipoAlojamiento = cbTipoAlojamiento.getValue();

        String name = textFields.get(0).getText();
        String description = txtDescripcion.getText();
        double pricePerNight;
        int maxGuests;
        City city = cbCiudad.getValue();
        java.time.LocalDate availableFrom = dateInicio.getValue();
        java.time.LocalDate availableTo = dateFinal.getValue();

        // Basic Validations
        if (name.isEmpty() || description.isEmpty() || tipoAlojamiento == null || city == null || availableFrom == null || availableTo == null) {
            MainController.showAlert("Error de Validación", "Todos los campos marcados con * son obligatorios.", Alert.AlertType.ERROR);
            return;
        }
        if (availableFrom.isAfter(availableTo)) {
            MainController.showAlert("Error de Validación", "La fecha de inicio no puede ser posterior a la fecha final.", Alert.AlertType.ERROR);
            return;
        }

        try {
            pricePerNight = Double.parseDouble(textFields.get(1).getText());
            maxGuests = Integer.parseInt(textFields.get(2).getText());
            if (pricePerNight <= 0 || maxGuests <= 0) {
                 MainController.showAlert("Error de Validación", "Precio por noche y capacidad de huéspedes deben ser positivos.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            MainController.showAlert("Error de Formato", "Precio por noche y/o número de huéspedes deben ser números válidos.", Alert.AlertType.ERROR);
            return;
        }
        
        String finalImageUrl = selectedImagePath;
        if (isEdit) {
             if (finalImageUrl == null || finalImageUrl.isEmpty()) { // If no new image selected during edit, keep the old one
                finalImageUrl = hostingToEdit.getImageUrl();
            }
        } else { // For new hosting
            if (finalImageUrl == null || finalImageUrl.isEmpty()) { // If no image selected for new hosting, use default
                finalImageUrl = "src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png";
            }
        }


        try {
            if (isEdit) {
                hostingToEdit.setName(name);
                hostingToEdit.setDescription(description);
                hostingToEdit.setPricePerNight(pricePerNight);
                hostingToEdit.setMaxGuests(maxGuests);
                hostingToEdit.setAvailableFrom(availableFrom);
                hostingToEdit.setAvailableTo(availableTo);
                hostingToEdit.setCity(city);
                hostingToEdit.setIncludedServices(includedServices);
                hostingToEdit.setImageUrl(finalImageUrl);
                
                hostingService.updateHosting(hostingToEdit);
                MainController.showAlert("Éxito", "Alojamiento '" + name + "' actualizado correctamente.", Alert.AlertType.INFORMATION);
            } else { // Create new hosting
                if ("Casa".equalsIgnoreCase(tipoAlojamiento)) {
                    hostingService.createHouse(name, city, description, finalImageUrl, pricePerNight, maxGuests, includedServices, 25000, availableFrom, availableTo);
                    MainController.showAlert("Éxito", "Casa '" + name + "' creada correctamente.", Alert.AlertType.INFORMATION);                } else if ("Apartamento".equalsIgnoreCase(tipoAlojamiento)) {
                    // Usar el constructor directo del apartamento en lugar del método problemático
                    try {
                        var apartamento = new co.edu.uniquindio.poo.bookyourstary.model.Apartament(
                            name, city, description, finalImageUrl, 
                            pricePerNight, maxGuests, includedServices, 0.0, // Sin tarifa de limpieza
                            availableFrom, availableTo
                        );
                        hostingService.getApartamentRepository().save(apartamento);
                        MainController.showAlert("Éxito", "Apartamento '" + name + "' creado correctamente.", Alert.AlertType.INFORMATION);
                    } catch (Exception e) {
                        MainController.showAlert("Error", "No se pudo crear el apartamento: " + e.getMessage(), Alert.AlertType.ERROR);
                        return;
                    }
                } else if ("Hotel".equalsIgnoreCase(tipoAlojamiento)) {
                    hostingService.createHotel(name, city, description, finalImageUrl, pricePerNight, maxGuests, includedServices, new LinkedList<>(), availableFrom, availableTo);
                    MainController.showAlert("Éxito", "Hotel '" + name + "' creado correctamente.", Alert.AlertType.INFORMATION);
                } else {
                    MainController.showAlert("Error de Tipo", "Tipo de alojamiento desconocido: " + tipoAlojamiento, Alert.AlertType.ERROR);
                    return; 
                }
            }
        } catch (IllegalArgumentException e) {
            MainController.showAlert("Error de Validación", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            MainController.showAlert("Error Inesperado", "No se pudo guardar el alojamiento: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace(); // For debugging
        }
    }

    private LinkedList<ServiceIncluded> buildIncludedServicesList(
            CheckBox chkWifi, CheckBox chkPiscina, CheckBox chkDesayuno) {
        var services = new LinkedList<ServiceIncluded>();
        var mapping = ServicesMapping.getInstance(); // Ensure ServicesMapping is robust
        try {
            if (chkWifi.isSelected()) {
                services.add(mapping.getServiceByName("wifi"));
            }
            if (chkPiscina.isSelected()) {
                services.add(mapping.getServiceByName("piscina"));
            }
            if (chkDesayuno.isSelected()) {
                services.add(mapping.getServiceByName("desayuno"));
            }
        } catch (Exception e) {
            // Log this error or handle it, maybe some services are not defined in ServicesMapping
            System.err.println("Error building service list: " + e.getMessage());
        }
        return services;
    }
}
