package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface IHouseService {
    /**
     * Crea una nueva casa
     */
    House createHouse(String name, City city, String description, String imageUrl,
                     double priceForNight, int maxGuests,
                     LinkedList<ServiceIncluded> services, double cleaningFee,
                     LocalDate availableFrom, LocalDate availableTo);

    /**
     * Encuentra una casa por su nombre
     */
    Optional<House> findByName(String name);

    /**
     * Encuentra casas por ciudad
     */
    List<House> findByCity(City city);

    /**
     * Obtiene todas las casas
     */
    List<House> findAll();

    /**
     * Actualiza la tarifa de limpieza de una casa
     */
    void updateCleaningFee(String houseName, double newCleaningFee);

    /**
     * Elimina una casa
     */
    void deleteHouse(String name);

    /**
     * Elimina todas las casas
     */
    void clearAll();
}
