package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface IHotelService {
    /**
     * Crea un nuevo hotel
     */
    Hotel createHotel(String name, City city, String description, String imageUrl,
                     double basePrice, int maxGuests,
                     LinkedList<ServiceIncluded> services, LinkedList<Room> rooms,
                     LocalDate availableFrom, LocalDate availableTo);

    /**
     * Encuentra un hotel por su nombre
     */
    Optional<Hotel> findByName(String name);

    /**
     * Encuentra hoteles por ciudad
     */
    List<Hotel> findByCity(City city);

    /**
     * Obtiene todos los hoteles
     */
    List<Hotel> findAll();

    /**
     * Agrega una habitación a un hotel
     */
    void addRoom(String hotelName, Room room);

    /**
     * Elimina una habitación de un hotel
     */
    void removeRoom(String hotelName, String roomNumber);

    /**
     * Elimina un hotel
     */
    void deleteHotel(String name);

    /**
     * Elimina todos los hoteles
     */
    void clearAll();
}
