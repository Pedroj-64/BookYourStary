package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.City;

public class CityRepository {

    private final List<City> cities;
    private static CityRepository instance;

    private CityRepository() {
        this.cities = new LinkedList<>();
    }

    public static CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository();
        }
        return instance;
    }

    public void save(City city) {
        if (findById(city.getName()).isPresent()) {
            throw new IllegalArgumentException("City with name " + city.getName() + " already exists.");
        }
        cities.add(city);
    }

    public Optional<City> findById(String cityName) {
        return cities.stream()
                .filter(city -> city.getName().equals(cityName))
                .findFirst();
    }

    public List<City> findAll() {
        return new LinkedList<>(cities);
    }

    public void delete(String cityName) {
        cities.removeIf(city -> city.getName().equals(cityName));
    }

    public void clearAll() {
        cities.clear();
    }
}
