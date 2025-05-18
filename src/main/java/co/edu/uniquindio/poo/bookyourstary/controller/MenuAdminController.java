package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
        return HomeController.getHostingTypes();
    }

    public List<City> getAvailableCities() {
        return homeController.getAvailableCities();
    }

    public void logout() {
        MainController.getInstance().getSessionManager().cerrarSesion();
        MainController.loadScene("home", 900, 600);
    }
}
