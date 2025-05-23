package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.time.LocalDate;
import co.edu.uniquindio.poo.bookyourstary.service.HostingFilterService;
import co.edu.uniquindio.poo.bookyourstary.util.UtilInterfaces;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.ShowHostingList;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import javafx.scene.control.Button;

public class HomeController {

    private final HostingFilterService hostingFilterService = new HostingFilterService();

    /**
     * Devuelve una lista aleatoria de 5 alojamientos disponibles para la fecha actual.
     */
    public List<Hosting> getRandomHostings() {
        LocalDate today = LocalDate.now();
        List<Hosting> availableHostings = MainController.getInstance().getHostingService().findAllHostings();
        Collections.shuffle(availableHostings, new Random());
        return availableHostings.stream().limit(5).collect(Collectors.toList());
    }

   
    public List<Hosting> filterHostings(String ciudad, String tipoDeAlojamiento, Double minPrice, Double maxPrice,
                                        LocalDate minDate, LocalDate maxDate, Integer numHuespedes,
                                        Boolean wifi, Boolean piscina, Boolean desayuno, LocalDate fechaInicio, LocalDate fechaFin) {
        return hostingFilterService.filterHostings(ciudad, tipoDeAlojamiento, minPrice, maxPrice, minDate, maxDate, numHuespedes, wifi, piscina, desayuno, fechaInicio, fechaFin);
    }

   

    public void updateHostingDisplay(List<Hosting> hostings,
                                     List<ImageView> imageViews,
                                     List<Label> titleLabels,
                                     List<Label> descLabels,
                                     List<Label> priceLabels,
                                     List<Label> cityLabels,
                                     List<Label> serviceLabels) {
        ShowHostingList.showHostings(hostings, imageViews, titleLabels, descLabels, priceLabels, cityLabels, serviceLabels);
    }

    public static List<String> getHostingTypes() {
        return UtilInterfaces.getHostingTypes();
    }

    public List<City> getAvailableCities() {
        return UtilInterfaces.getColombianCities();
    }

    public void updateButton(Button button) {
        button.setText("Cerrar Sesion");
        button.getStyleClass().clear();
        button.getStyleClass().add("button.css");
        button.setOnAction(e -> {
            MainController.getInstance().getSessionManager().cerrarSesion();
            MainController.loadScene("home", 900, 600);
        });
    }

    public void updateButtonIfLoggedIn(Button button) {
        Object user = MainController.getInstance().getSessionManager().getUsuarioActual();
        if (user != null) {
            updateButton(button);
        }
    }



    /**
     * Asocia cada botón de reservar con su hosting correspondiente usando setUserData.
     * El botón 1 recibe el hosting 0, el botón 2 el hosting 1, etc.
     */
    public void assignHostingsToReserveButtons(List<Button> reserveButtons, List<Hosting> hostings) {
        int count = Math.min(reserveButtons.size(), hostings.size());
        for (int i = 0; i < count; i++) {
            reserveButtons.get(i).setUserData(hostings.get(i));
        }
    }

    /**
     * Agrega un hosting a la lista de reservas pendientes (carrito de compras) usando CartManager.
     * @return true si el hosting fue añadido, false si ya estaba o era null.
     */
    public boolean addHostingToPendingReservations(Hosting hosting) {
        return MainController.getInstance().getCartManager().addHosting(hosting);
    }

    /**
     * Devuelve la lista de reservas pendientes (carrito de compras) usando CartManager.
     */
    public static List<Hosting> getPendingReservations() {
        return MainController.getInstance().getCartManager().getPendingReservations();
    }

    /**
     * Limpia la lista de reservas pendientes (por ejemplo, al confirmar la compra) usando CartManager.
     */
    public static void clearPendingReservations() {
        MainController.getInstance().getCartManager().clear();
    }

    /**
     * Valida y procesa la reserva de un alojamiento
     * @return true si la reserva puede proceder, false si hay algún problema
     */
    public boolean handleReservation(Object currentUser, Hosting hosting) {
        if (currentUser == null) {
            MainController.showAlert(
                "Acción Requerida",
                "Por favor, inicie sesión o regístrese para poder reservar.",
                javafx.scene.control.Alert.AlertType.INFORMATION);
            return false;
        }

        if (!(currentUser instanceof co.edu.uniquindio.poo.bookyourstary.model.Client)) {
            MainController.showAlert(
                "Acción no permitida",
                "Solo los clientes pueden realizar reservas.",
                javafx.scene.control.Alert.AlertType.WARNING);
            return false;
        }

        if (hosting == null) {
            MainController.showAlert(
                "Error",
                "No se encontró el alojamiento seleccionado.",
                javafx.scene.control.Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    /**
     * Verifica el estado del usuario y actualiza el header correspondiente
     */
    public void handleUserHeaderUpdate(javafx.scene.layout.AnchorPane header) {
        Object user = MainController.getInstance().getSessionManager().getUsuarioActual();
        if (user instanceof co.edu.uniquindio.poo.bookyourstary.model.Client) {
            co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.ViewLoader.setContent(header, "UserHeader");
        } else if (user instanceof co.edu.uniquindio.poo.bookyourstary.model.Admin) {
            co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.ViewLoader.setContent(header, "AdminHeader");
        }
    }

    /**
     * Navega a la pantalla de inicio de sesión
     */
    public void navigateToLogin() {
        MainController.loadScene("userLogin", 900, 600);
    }

    /**
     * Navega a la pantalla de registro
     */
    public void navigateToRegister() {
        MainController.loadScene("userRegister", 900, 600);
    }
}
