package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.repository.HostingRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HostingFilterService {
    private static HostingFilterService instance;

    public static HostingFilterService getInstance() {
        if (instance == null) {
            instance = new HostingFilterService();
        }
        return instance;
    }

    public List<Hosting> filterHostings(String ciudad, String tipoDeAlojamiento, Double minPrice, Double maxPrice,
                                        LocalDate minDate, LocalDate maxDate, Integer numHuespedes,
                                        Boolean wifi, Boolean piscina, Boolean desayuno, LocalDate fechaInicio, LocalDate fechaFin) {
        return HostingRepository.getInstance().findAll().stream()
            .filter(h -> ciudad == null || h.getCity().toString().equalsIgnoreCase(ciudad))
            .filter(h -> tipoDeAlojamiento == null || h.getHostingType().toString().equalsIgnoreCase(tipoDeAlojamiento))
            .filter(h -> minPrice == null || h.getPricePerNight() >= minPrice)
            .filter(h -> maxPrice == null || h.getPricePerNight() <= maxPrice)
            .filter(h -> numHuespedes == null || h.getMaxGuests() >= numHuespedes)
            .filter(h -> wifi == null || h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("wifi")))
            .filter(h -> piscina == null || h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("piscina")))
            .filter(h -> desayuno == null || h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("desayuno")))
            // Lógica de disponibilidad por fechas
            .filter(h -> {
                if (fechaInicio == null || fechaFin == null) return true;
                return (h.getAvailableFrom() == null || !fechaInicio.isBefore(h.getAvailableFrom())) &&
                       (h.getAvailableTo() == null || !fechaFin.isAfter(h.getAvailableTo()));
            })
            .collect(Collectors.toList());
    }
}
