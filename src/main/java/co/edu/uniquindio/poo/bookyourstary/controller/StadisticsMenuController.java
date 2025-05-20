package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import java.util.List;
import java.util.stream.Collectors;

public class StadisticsMenuController {

    private final HostingService hostingService = HostingService.getInstance();
    private final HouseService houseService = HouseService.getInstance();
    private final HotelService hotelService = HotelService.getInstance();
    private final ApartamentService apartamentService = ApartamentService.getInstance();

    public ObservableList<String> getOpciones() {
        return FXCollections.observableArrayList("Todos", "Casa", "Apartamento", "Hotel");
    }

    public ObservableList<PieChart.Data> getOccupancyData(String selectedType) {
        List<Hosting> hostings = filterHostingsByType(selectedType);
        long total = hostings.size();
        long ocupados = hostings.stream().filter(this::isOccupied).count();
        long disponibles = total - ocupados;

        return FXCollections.observableArrayList(
                new PieChart.Data("Ocupados (" + ocupados + ")", ocupados),
                new PieChart.Data("Disponibles (" + disponibles + ")", disponibles)
        );
    }

    public XYChart.Series<String, Number> getEarningsSeries(String selectedType) {
        List<Hosting> hostings = filterHostingsByType(selectedType);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ganancias");

        hostings.forEach(hosting ->
                series.getData().add(new XYChart.Data<>(
                        hosting.getName(),
                        calcularGanancias(hosting)
                ))
        );

        return series;
    }

    public XYChart.Series<String, Number> getPopularityByCitySeries() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Reservas");

        hostingService.findAllHostings().stream()
                .collect(Collectors.groupingBy(
                        h -> h.getCity().getName(),
                        Collectors.counting()
                ))
                .forEach((ciudad, cantidad) ->
                        series.getData().add(new XYChart.Data<>(ciudad, cantidad))
                );

        return series;
    }

    // Lógica de apoyo
    private List<Hosting> filterHostingsByType(String tipo) {
        return hostingService.findAllHostings().stream()
                .filter(h -> tipo.equals("Todos") || h.getClass().getSimpleName().equalsIgnoreCase(tipo))
                .toList();
    }

    private boolean isOccupied(Hosting hosting) {
        // Implementar lógica real de ocupación (ej. comparar con reservas)
        return Math.random() > 0.5; // Ejemplo aleatorio
    }

    private double calcularGanancias(Hosting hosting) {
        // Implementar lógica real de ganancias (ej. historial de reservas * precio)
        return hosting.getPricePerNight() * 30; // Ejemplo: 30 noches
    }
}