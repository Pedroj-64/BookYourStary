package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.service.AuthService;
import co.edu.uniquindio.poo.bookyourstary.util.UtilInterfaces;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    }    /**
     * Abre la ventana de edición de alojamiento en un nuevo Stage modal y le pasa el objeto Hosting a editar.
     */    
    public void openEditHostingWindow(Hosting hosting) {
        try {
            if (hosting == null) {
                MainController.showAlert(
                    "Error", "No se puede editar un alojamiento nulo", javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }
            
            System.out.println("Abriendo ventana de edición para alojamiento: " + hosting.getName());
            
            // Usar MainController.loadFXML para cargar la vista
            Parent root = MainController.loadFXML("CreationAndEditingForm");
            if (root == null) {
                throw new Exception("No se pudo cargar la vista CreationAndEditingForm");
            }
            
            // Obtener el controlador y establecer el hosting a editar
            CreationAndEditingFormController controller = MainController.getInstance().getCreationAndEditingFormController();
            if (controller == null) {
                throw new Exception("No se pudo obtener el controlador del formulario");
            }
            
            // Establecer el alojamiento a editar
            controller.setHostingToEdit(hosting);
            
            // Configurar y mostrar la ventana
            Stage stage = new Stage();
            stage.setTitle("Editar alojamiento: " + hosting.getName());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(e -> {
                // Actualizar datos después de cerrar el formulario
                System.out.println("Ventana de edición cerrada, actualizando datos...");
                // Refrescar la vista actual después de editar un alojamiento
                try {
                    MainController.getInstance().refreshCurrentView();
                    System.out.println("Vista actualizada después de editar alojamiento");
                } catch (Exception ex) {
                    System.err.println("Error al actualizar vista después de editar alojamiento: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            stage.showAndWait();
        } catch (Exception e) {
            MainController.showAlert(
                "Error", "No se pudo abrir la ventana de edición: " + e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }/**
     * Abre la ventana de creación de alojamiento en un nuevo Stage modal.
     */
    public void openCreationWindow() {
        try {
            System.out.println("Abriendo ventana de creación de alojamiento");
            
            // Usar MainController.loadFXML para cargar la vista
            Parent root = MainController.loadFXML("CreationAndEditingForm");
            if (root == null) {
                throw new Exception("No se pudo cargar la vista CreationAndEditingForm");
            }
            
            // No necesitamos establecer un alojamiento para editar aquí,
            // porque estamos creando un nuevo alojamiento
            
            // Configurar y mostrar la ventana
            Stage stage = new Stage();
            stage.setTitle("Crear nuevo alojamiento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(e -> {
                // Actualizar datos después de cerrar el formulario
                System.out.println("Ventana de creación cerrada, actualizando datos...");
                // Refrescar la vista actual después de crear un alojamiento
                try {
                    MainController.getInstance().refreshCurrentView();
                    System.out.println("Vista actualizada después de crear alojamiento");
                } catch (Exception ex) {
                    System.err.println("Error al actualizar vista después de crear alojamiento: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            stage.showAndWait();
        } catch (Exception e) {
            MainController.showAlert(
                "Error", "No se pudo abrir la ventana de creación: " + e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }    /**
     * Obtiene la lista completa de alojamientos desde el servicio.
     */
    public List<Hosting> getAllHostings() {
        try {
            // Obtener directamente todos los alojamientos del servicio
            var hostingService = MainController.getInstance().getHostingService();
            List<Hosting> allHostings = hostingService.findAllHostings();
            // Asegurar que la lista no sea nula
            if (allHostings == null) {
                allHostings = new java.util.ArrayList<>();
            }
            System.out.println("getAllHostings encontró: " + allHostings.size() + " alojamientos");
            return allHostings;
        } catch (Exception e) {
            System.err.println("Error al obtener alojamientos: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    @FXML
    private Button btnManageRooms;

}
