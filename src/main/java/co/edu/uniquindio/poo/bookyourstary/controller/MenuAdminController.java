package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.util.UtilInterfaces;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class MenuAdminController {
    private final HomeController homeController = new HomeController();

    public List<Hosting> getRandomHostings() {
        return homeController.getRandomHostings();
    }

    public List<Hosting> filterHostings(String ciudad, String tipoDeAlojamiento, Double minPrice, Double maxPrice,
                                        LocalDate minDate, LocalDate maxDate, Integer numHuespedes,
                                        Boolean wifi, Boolean piscina, Boolean desayuno, LocalDate fechaInicio, LocalDate fechaFin) {
        return homeController.filterHostings(ciudad, tipoDeAlojamiento, minPrice, maxPrice, minDate, maxDate, numHuespedes, wifi, piscina, desayuno, fechaInicio, fechaFin);
    }

    public void updateHostingDisplay(List<Hosting> hostings,
                                     List<ImageView> imageViews,
                                     List<Label> titleLabels,
                                     List<Label> descLabels,
                                     List<Label> priceLabels,
                                     List<Label> cityLabels,
                                     List<Label> serviceLabels) {
        homeController.updateHostingDisplay(hostings, imageViews, titleLabels, descLabels, priceLabels, cityLabels, serviceLabels);
    }

    public static List<String> getHostingTypes() {
        return UtilInterfaces.getHostingTypes();
    }

    public List<City> getAvailableCities() {
        return UtilInterfaces.getColombianCities();
    }

    public void logout() {
        MainController.getInstance().getSessionManager().cerrarSesion();
        MainController.loadScene("home", 900, 600);
    }

    /**
     * Asigna cada objeto Hosting de la lista a los botones de edición usando setUserData.
     * El botón 1 recibe el hosting 0, el botón 2 el hosting 1, etc.
     */
    public void assignHostingsToEditButtons(List<Button> editButtons, List<Hosting> hostings) {
        int count = Math.min(editButtons.size(), hostings.size());
        for (int i = 0; i < count; i++) {
            editButtons.get(i).setUserData(hostings.get(i));
        }
    }

    /**
     * Abre la ventana de edición de alojamiento en un nuevo Stage modal y le pasa el objeto Hosting a editar.
     */
    public void openEditHostingWindow(Hosting hosting) {
        try {
            FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/bookyourstary/CreationAndEditingForm.fxml"));
            Parent root = loader.load();
            CreationAndEditingFormController controller = MainController.getInstance().getCreationAndEditingFormController();
            controller.setHostingToEdit(hosting);
            Stage stage = new Stage();
            stage.setTitle("Editar alojamiento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            MainController.showAlert(
                "Error", "No se pudo abrir la ventana de edición: " + e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
