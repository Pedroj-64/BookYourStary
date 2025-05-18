package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.repository.HostingRepository;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.time.LocalDate;
import co.edu.uniquindio.poo.bookyourstary.service.HostingFilterService;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.ShowHostingList;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import co.edu.uniquindio.poo.bookyourstary.config.mapping.DataMapping;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import javafx.scene.control.Button;

public class HomeController {

    private final HostingFilterService hostingFilterService = new HostingFilterService();

    /**
     * Devuelve una lista aleatoria de 5 alojamientos del repositorio
     */
    public List<Hosting> getRandomHostings() {
        List<Hosting> allHostings = HostingRepository.getInstance().findAll();
        Collections.shuffle(allHostings, new Random());
        return allHostings.stream().limit(5).collect(Collectors.toList());
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
        return List.of("Apto", "Casa", "Hotel");
    }

    public List<City> getAvailableCities() {
        return DataMapping.getColombianCities();
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
}
