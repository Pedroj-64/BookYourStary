package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.repository.CityRepository;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;

import java.util.List;
import java.util.Optional;

public class CityService {

    private final CityRepository cityRepository;    private static CityService instance;

    private CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public static CityService getInstance() {
        if (instance == null) {
            instance = new CityService(CityRepository.getInstance());
        }
        return instance;
    }

    /**
     * Guarda una nueva ciudad en el sistema.
     * 
     * @param name Nombre de la ciudad
     * @param country País de la ciudad
     * @param departament Departamento de la ciudad
     * @return La ciudad creada y guardada
     * @throws IllegalArgumentException si algún parámetro es nulo o vacío, o si la ciudad ya existe
     */
    public City saveCity(String name, String country, String departament) {
        // Validar que los parámetros no sean nulos o vacíos
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser nulo o vacío");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("El país no puede ser nulo o vacío");
        }
        if (departament == null || departament.trim().isEmpty()) {
            throw new IllegalArgumentException("El departamento no puede ser nulo o vacío");
        }

        // Verificar si la ciudad ya existe
        if (findCityById(name).isPresent()) {
            throw new IllegalArgumentException("Ya existe una ciudad con el nombre: " + name);
        }

        // Crear y guardar la ciudad
        City city = new City(name, country, departament);
        try {
            cityRepository.save(city);
            // Guardar los cambios inmediatamente
            XmlSerializationManager.getInstance().saveAllData();
            return city;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la ciudad: " + e.getMessage(), e);
        }
    }

    /**
     * Encuentra una ciudad por su nombre.
     * 
     * @param name Nombre de la ciudad a buscar
     * @return La ciudad encontrada
     * @throws IllegalArgumentException si no se encuentra la ciudad
     */
    public static City getCityByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser nulo o vacío");
        }
        return getInstance().findCityById(name)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la ciudad: " + name));
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

    /**
     * Elimina todas las ciudades del sistema y guarda los cambios.
     */
    public void clearAll() {
        cityRepository.clearAll();
        XmlSerializationManager.getInstance().saveAllData();
    }
}
