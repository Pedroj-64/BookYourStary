package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import java.time.LocalDate;
import java.util.List;

public interface IHostingFilterService {
    /**
     * Filtra alojamientos por ciudad
     */
    List<Hosting> filterByCity(List<Hosting> hostings, City city);

    /**
     * Filtra alojamientos por rango de precio
     */
    List<Hosting> filterByPriceRange(List<Hosting> hostings, double minPrice, double maxPrice);

    /**
     * Filtra alojamientos por capacidad de huéspedes
     */
    List<Hosting> filterByGuestCapacity(List<Hosting> hostings, int minGuests, int maxGuests);

    /**
     * Filtra alojamientos por tipo (Hotel, Casa, Apartamento)
     */
    List<Hosting> filterByType(List<Hosting> hostings, Class<?> hostingType);

    /**
     * Filtra alojamientos por disponibilidad de fechas
     */
    List<Hosting> filterByAvailability(List<Hosting> hostings, LocalDate startDate, LocalDate endDate);

    /**
     * Filtra alojamientos por servicios incluidos
     */
    List<Hosting> filterByServices(List<Hosting> hostings, List<ServiceIncluded> requiredServices);

    /**
     * Filtra alojamientos por valoración mínima
     */
    List<Hosting> filterByMinRating(List<Hosting> hostings, double minRating);
}
