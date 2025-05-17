package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.repository.HostingRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HostingFilterService {
    public List<Hosting> filterHostings(String ciudad, String tipoDeAlojamiento, Double minPrice, Double maxPrice,
                                        LocalDate minDate, LocalDate maxDate, Integer numHuespedes,
                                        Boolean wifi, Boolean piscina, Boolean desayuno) {
        return HostingRepository.getInstance().findAll().stream()
            .filter(h -> ciudad == null || h.getCity().toString().equalsIgnoreCase(ciudad))
            .filter(h -> tipoDeAlojamiento == null || h.getHostingType().toString().equalsIgnoreCase(tipoDeAlojamiento))
            .filter(h -> minPrice == null || h.getPricePerNight() >= minPrice)
            .filter(h -> maxPrice == null || h.getPricePerNight() <= maxPrice)
            .filter(h -> numHuespedes == null || h.getMaxGuests() >= numHuespedes)
            // Aquí deberás implementar la lógica para fechas y servicios incluidos (wifi, piscina, desayuno)
            .filter(h -> wifi == null || h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("wifi")))
            .filter(h -> piscina == null || h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("piscina")))
            .filter(h -> desayuno == null || h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("desayuno")))
            // TODO: Agregar lógica para disponibilidad por fechas (minDate, maxDate)
            .collect(Collectors.toList());
    }
}
