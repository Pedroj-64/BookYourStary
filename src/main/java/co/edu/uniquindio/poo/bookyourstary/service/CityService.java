package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.repository.CityRepository;

import java.util.List;
import java.util.Optional;

public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public void saveCity(String name, String country, String departament) {
        City city = new City(name, country, departament);
        cityRepository.save(city);
    }

    public Optional<City> findCityById(String cityName) {
        return cityRepository.findById(cityName);
    }

    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    public void deleteCity(String cityName) {
        @SuppressWarnings("unused")
        City city = findCityById(cityName)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la ciudad con ese nombre"));
        cityRepository.delete(cityName);
    }
}
