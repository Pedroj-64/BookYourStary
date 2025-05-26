package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.repository.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface IHostingService {
    /**
     * Crea una nueva casa
     */
    House createHouse(String name, City city, String description, String imageUrl,
                     double priceForNight, int maxGuests,
                     LinkedList<ServiceIncluded> services, double cleaningFee,
                     LocalDate availableFrom, LocalDate availableTo);

    /**
     * Crea un nuevo hotel
     */
    Hotel createHotel(String name, City city, String description, String imageUrl,
                     double basePrice, int maxGuests,
                     LinkedList<ServiceIncluded> services, LinkedList<Room> rooms,
                     LocalDate availableFrom, LocalDate availableTo);

    /**
     * Crea un nuevo apartamento
     */
    Apartament createApartment(String name, City city, String description, String imageUrl,
                             double priceForNight, int maxGuests,
                             LinkedList<ServiceIncluded> services,
                             LocalDate availableFrom, LocalDate availableTo);

    /**
     * Elimina un alojamiento por su nombre
     */
    void deleteHosting(String name);

    /**
     * Encuentra todos los alojamientos
     */
    List<Hosting> findAllHostings();

    /**
     * Encuentra alojamientos por ciudad
     */
    List<Hosting> findHostingsByCity(City city);

    /**
     * Encuentra un alojamiento por su nombre
     */
    Optional<Hosting> findByName(String name);

    /**
     * Obtiene el repositorio de apartamentos
     */
    ApartamentRepository getApartamentRepository();

    /**
     * Obtiene el repositorio de hoteles
     */
    HotelRepository getHotelRepository();

    /**
     * Obtiene el repositorio de casas
     */
    HouseRepository getHouseRepository();

    /**
     * Elimina todos los alojamientos
     */
    void clearAll();
}
