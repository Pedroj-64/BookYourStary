package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IStatisticsService {
    /**
     * Obtiene las estadísticas de ocupación por ciudad
     */
    Map<City, Double> getOccupancyRateByCity();

    /**
     * Obtiene los alojamientos más reservados
     */
    List<Hosting> getMostBookedHostings(int limit);

    /**
     * Obtiene los clientes con más reservas
     */
    List<Client> getMostActiveClients(int limit);

    /**
     * Obtiene el ingreso total por alojamiento
     */
    Map<Hosting, Double> getTotalIncomeByHosting();

    /**
     * Obtiene el ingreso total por ciudad
     */
    Map<City, Double> getTotalIncomeByCity();

    /**
     * Obtiene el promedio de valoraciones por alojamiento
     */
    Map<Hosting, Double> getAverageRatingsByHosting();

    /**
     * Obtiene las estadísticas de reservas por período
     */
    Map<String, Integer> getBookingStatsByPeriod(LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene el porcentaje de cancelaciones por alojamiento
     */
    Map<Hosting, Double> getCancellationRateByHosting();
    
    /**
     * Obtiene la duración promedio de estancia por alojamiento
     */
    Map<Hosting, Double> getAverageStayDurationByHosting();
}
