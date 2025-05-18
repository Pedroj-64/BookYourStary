package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.enums.HostingType;
import co.edu.uniquindio.poo.bookyourstary.service.HostingFilterService;

import java.time.LocalDate;
import java.util.List;

public class FilterController {

    private final HostingFilterService hostingFilterService;

    public FilterController() {
        this.hostingFilterService = HostingFilterService.getInstance();
    }

    /**
     * Filtro centralizado: todos los parámetros pueden ser null si no se usan.
     */
    public List<Hosting> filterHostings(City city, Double minPrice, Double maxPrice, Integer maxGuests,
                                        HostingType type, LocalDate fechaInicio, LocalDate fechaFin,
                                        Boolean wifi, Boolean piscina, Boolean desayuno) {
        String ciudad = city != null ? city.toString() : null;
        String tipo = type != null ? type.toString() : null;
        return hostingFilterService.filterHostings(
            ciudad, tipo, minPrice, maxPrice, null, null, maxGuests, wifi, piscina, desayuno, fechaInicio, fechaFin
        );
    }

    // Métodos individuales si se quieren mantener (opcional)
    public List<Hosting> filterByCity(City city) {
        return filterHostings(city, null, null, null, null, null, null, null, null, null);
    }
    public List<Hosting> filterByPriceRange(double minPrice, double maxPrice) {
        return filterHostings(null, minPrice, maxPrice, null, null, null, null, null, null, null);
    }
    public List<Hosting> filterByMaxGuests(int maxGuests) {
        return filterHostings(null, null, null, maxGuests, null, null, null, null, null, null);
    }
    public List<Hosting> filterByType(HostingType type) {
        return filterHostings(null, null, null, null, type, null, null, null, null, null);
    }
}
