package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.model.Hotel;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    /**
     * Crea una nueva habitación
     */
    Room createRoom(String number, double price, int capacity, String imageUrl, String description);

    /**
     * Agrega una habitación a un hotel
     */
    void addRoomToHotel(Hotel hotel, Room room);

    /**
     * Elimina una habitación de un hotel
     */
    void removeRoomFromHotel(Hotel hotel, String roomNumber);

    /**
     * Actualiza el precio de una habitación
     */
    void updateRoomPrice(Hotel hotel, String roomNumber, double newPrice);

    /**
     * Actualiza la capacidad de una habitación
     */
    void updateRoomCapacity(Hotel hotel, String roomNumber, int newCapacity);

    /**
     * Busca una habitación por su número
     */
    Optional<Room> findRoomByNumber(Hotel hotel, String roomNumber);

    /**
     * Obtiene todas las habitaciones de un hotel
     */
    List<Room> getRoomsByHotel(Hotel hotel);

    /**
     * Verifica si una habitación está disponible
     */
    boolean isRoomAvailable(Hotel hotel, String roomNumber);

    /**
     * Elimina todas las habitaciones
     */
    void clearAll();
}
