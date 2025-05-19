package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Hotel;

public class HotelRepository {

    private final LinkedList<Hotel> hotels;
    private static HotelRepository instance;

    private HotelRepository() {
        this.hotels = new LinkedList<>();
    }

    public static HotelRepository getInstance() {
        if (instance == null) {
            instance = new HotelRepository();
        }
        return instance;
    }

    public void save(Hotel hotel) {
        hotels.add(hotel);
    }

    public Optional<Hotel> findById(String name) {
        return hotels.stream().filter(hotel -> hotel.getName().equals(name)).findFirst();
    }

    public void delete(Hotel hotel) {
        hotels.remove(hotel);
    }

    public LinkedList<Hotel> findAll() {
        return new LinkedList<>(hotels);
    }

    /**
     * Actualiza un hotel existente en la lista, reemplazando el que tiene el mismo nombre.
     */
    public void update(Hotel hotel) {
        for (int i = 0; i < hotels.size(); i++) {
            if (hotels.get(i).getName().equals(hotel.getName())) {
                hotels.set(i, hotel);
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró el hotel a actualizar: " + hotel.getName());
    }

}
