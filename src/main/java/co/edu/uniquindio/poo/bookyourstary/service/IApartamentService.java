package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface IApartamentService {
    /**
     * Crea un nuevo apartamento
     */
    Apartament createApartament(String name, City city, String description, String imageUrl,
                              double priceForNight, int maxGuests,
                              LinkedList<ServiceIncluded> services,
                              LocalDate availableFrom, LocalDate availableTo);

    /**
     * Encuentra un apartamento por su nombre
     */
    Optional<Apartament> findByName(String name);

    /**
     * Encuentra apartamentos por ciudad
     */
    List<Apartament> findByCity(City city);

    /**
     * Obtiene todos los apartamentos
     */
    List<Apartament> findAll();

    /**
     * Elimina un apartamento
     */
    void deleteApartament(String name);

    /**
     * Elimina todos los apartamentos
     */
    void clearAll();
}
