package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.City;
import java.util.List;
import java.util.Optional;

public interface ICityService {
    /**
     * Guarda una nueva ciudad
     */
    City saveCity(String name, String country, String departament);

    /**
     * Busca una ciudad por su ID (nombre)
     */
    Optional<City> findCityById(String cityName);

    /**
     * Obtiene todas las ciudades
     */
    List<City> findAllCities();

    /**
     * Elimina una ciudad por su nombre
     */
    void deleteCity(String cityName);

    /**
     * Elimina todas las ciudades
     */
    void clearAll();
}
