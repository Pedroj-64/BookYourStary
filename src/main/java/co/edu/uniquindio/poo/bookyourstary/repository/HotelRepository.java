package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Hotel;

public class HotelRepository {

    private final LinkedList<Hotel> hotels;

    public HotelRepository() {
        this.hotels = new LinkedList<>();
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

}
