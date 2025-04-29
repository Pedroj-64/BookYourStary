package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.House;

public class HouseRepository {

    private final LinkedList<House> houses;

    public HouseRepository() {
        this.houses = new LinkedList<>();
    }

    public void save(House house) {
        houses.add(house);
    }

    public Optional<House> findById(String name) {
        return houses.stream().filter(house -> house.getName().equals(name)).findFirst();
    }

    public void delete(House house) {
        houses.remove(house);
    }

    public LinkedList<House> findAll() {
        return new LinkedList<>(houses);
    }

}
