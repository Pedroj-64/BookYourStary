package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.*;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio principal para la gestión de alojamientos en el sistema BookYourStary.
 * Esta clase implementa el patrón Singleton y maneja todas las operaciones relacionadas
 * con la creación, actualización, eliminación y búsqueda de alojamientos.
 * 
 * @author BookYourStary
 * @version 1.0
 */
public class HostingService {
    private final HouseRepository houseRepository;
    private final ApartamentRepository apartamentRepository;
    private final HotelRepository hotelRepository;
    private final HostingRepository hostingRepository;
    private static HostingService instance;

    /**
     * Obtiene la instancia única del servicio de alojamientos.
     * @return la instancia única de HostingService
     */
    public static HostingService getInstance() {
        if (instance == null) {
            instance = new HostingService(
                HouseRepository.getInstance(), 
                ApartamentRepository.getInstance(), 
                HotelRepository.getInstance(),
                HostingRepository.getInstance()
            );
        }
        return instance;
    }
    
    // Getter para el repositorio de apartamentos
    public ApartamentRepository getApartamentRepository() {
        return apartamentRepository;
    }
    
    // Getter para el repositorio de casas
    public HouseRepository getHouseRepository() {
        return houseRepository;
    }
    
    // Getter para el repositorio de hoteles
    public HotelRepository getHotelRepository() {
        return hotelRepository;
    }

    private HostingService(HouseRepository houseRepository, ApartamentRepository apartamentRepository,
                           HotelRepository hotelRepository, HostingRepository hostingRepository) {
        this.houseRepository = houseRepository;
        this.apartamentRepository = apartamentRepository;
        this.hotelRepository = hotelRepository;
        this.hostingRepository = hostingRepository;
    }    /**
     * Guarda un alojamiento en el repositorio correspondiente y persiste los cambios en XML
     * @param hosting El alojamiento a guardar
     * @throws RuntimeException si ocurre un error al guardar
     */
    public void saveHosting(Hosting hosting) {
        try {
            if (hosting == null) {
                throw new IllegalArgumentException("El hosting no puede ser nulo");
            }
            
            // Guardar en el repositorio específico y en el repositorio general
            switch (hosting) {
                case House house -> {
                    houseRepository.save(house);
                    hostingRepository.save(house);
                }
                case Apartament apartament -> {
                    apartamentRepository.save(apartament);
                    hostingRepository.save(apartament);
                }
                case Hotel hotel -> {
                    hotelRepository.save(hotel);
                    hostingRepository.save(hotel);
                }
                case null, default -> {
                    hostingRepository.save(hosting);
                }
            }
            
            XmlSerializationManager.getInstance().saveAllData();
            System.out.println("Alojamiento guardado y persistido con éxito: " + hosting.getName());
        } catch (Exception e) {
            System.err.println("Error al guardar el alojamiento: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el alojamiento", e);
        }
    }

    /**
     * Crea una nueva casa en el sistema y la persiste
     * @param name Nombre de la casa
     * @param city Ciudad donde se encuentra
     * @param description Descripción de la casa
     * @param imageUrl URL de la imagen
     * @param priceForNight Precio por noche
     * @param maxGuests Número máximo de huéspedes
     * @param services Servicios incluidos
     * @param cleaningFee Tarifa de limpieza
     * @param availableFrom Fecha desde la que está disponible
     * @param availableTo Fecha hasta la que está disponible
     */    public House createHouse(String name, City city, String description, String imageUrl,
            double priceForNight, int maxGuests, List<ServiceIncluded> services, 
            double cleaningFee, LocalDate availableFrom, LocalDate availableTo) {
        validateBasicParameters(name, city, description, imageUrl, priceForNight, maxGuests, availableFrom, availableTo);
        
        Hosting hosting = HostingFactory.createHouse(name, city, description, imageUrl,
                priceForNight, maxGuests, services, cleaningFee, availableFrom, availableTo);
                
        if (!(hosting instanceof House)) {
            throw new RuntimeException("Error al crear la casa: tipo de objeto incorrecto");
        }
        
        House house = (House) hosting;
        saveHosting(house);
        return house;
    }

    /**
     * Crea un nuevo hotel en el sistema y lo persiste
     * @param name Nombre del hotel
     * @param city Ciudad donde se encuentra
     * @param description Descripción del hotel
     * @param imageUrl URL de la imagen
     * @param basePrice Precio base
     * @param maxGuests Número máximo de huéspedes
     * @param services Servicios incluidos
     * @param rooms Lista de habitaciones
     * @param availableFrom Fecha desde la que está disponible
     * @param availableTo Fecha hasta la que está disponible
     */    public Hotel createHotel(String name, City city, String description, String imageUrl,
            double basePrice, int maxGuests, List<ServiceIncluded> services, 
            List<Room> rooms, LocalDate availableFrom, LocalDate availableTo) {
        validateBasicParameters(name, city, description, imageUrl, basePrice, maxGuests, availableFrom, availableTo);
        
        Hosting hosting = HostingFactory.createHotel(name, city, description, imageUrl,
                basePrice, maxGuests, services, rooms, availableFrom, availableTo);
                
        if (!(hosting instanceof Hotel)) {
            throw new RuntimeException("Error al crear el hotel: tipo de objeto incorrecto");
        }
        
        Hotel hotel = (Hotel) hosting;
        saveHosting(hotel);
        return hotel;
    }

    /**
     * Crea un nuevo apartamento en el sistema.
     * 
     * @param name nombre del apartamento
     * @param city ciudad donde se encuentra
     * @param description descripción del apartamento
     * @param imageUrl URL de la imagen del apartamento
     * @param priceForNight precio por noche
     * @param maxGuests número máximo de huéspedes
     * @param services servicios incluidos
     * @param cleaningFee tarifa de limpieza
     * @param availableFrom fecha desde cuando está disponible
     * @param availableTo fecha hasta cuando está disponible
     * @return el apartamento creado
     * @throws IllegalArgumentException si algún parámetro no es válido
     */    public Apartament createApartament(String name, City city, String description, String imageUrl,
            double priceForNight, int maxGuests, List<ServiceIncluded> services, double cleaningFee,
            LocalDate availableFrom, LocalDate availableTo) {
        validateBasicParameters(name, city, description, imageUrl, priceForNight, maxGuests, availableFrom, availableTo);

        Hosting hosting = HostingFactory.createApartament(name, city, description, imageUrl,
                priceForNight, maxGuests, services, cleaningFee, availableFrom, availableTo);
        
        if (!(hosting instanceof Apartament)) {
            throw new RuntimeException("Error al crear el apartamento: tipo de objeto incorrecto");
        }
        
        Apartament apartament = (Apartament) hosting;
        saveHosting(apartament);
        return apartament;
    }

    public Hosting findByName(String name) {
        Optional<House> house = houseRepository.findById(name);
        if (house.isPresent()) {
            return house.get();
        }
        
        Optional<Apartament> apartament = apartamentRepository.findById(name);
        if (apartament.isPresent()) {
            return apartament.get();
        }
        
        Optional<Hotel> hotel = hotelRepository.findById(name);
        if (hotel.isPresent()) {
            return hotel.get();
        }
        
        return null;
    }

    /**
     * Crea un nuevo apartamento con servicios predeterminados.
     * 
     * @param name nombre del apartamento
     * @param city ciudad donde se encuentra
     * @param description descripción del apartamento
     * @param imageUrl URL de la imagen del apartamento
     * @param priceForNight precio por noche
     * @param maxGuests número máximo de huéspedes
     * @param cleaningFee tarifa de limpieza
     * @param availableFrom fecha desde cuando está disponible
     * @param availableTo fecha hasta cuando está disponible
     * @return el apartamento creado
     * @throws IllegalArgumentException si algún parámetro no es válido
     */
    public Apartament createApartament(String name, City city, String description, String imageUrl,
            double priceForNight, int maxGuests, double cleaningFee,
            LocalDate availableFrom, LocalDate availableTo) {
        return createApartament(name, city, description, imageUrl, priceForNight, maxGuests,
                new LinkedList<>(), cleaningFee, availableFrom, availableTo);
    }

    public List<Hosting> findAllHostings() {
        try {
            // Solo usar el repositorio general que contiene todos los hostings
            List<Hosting> allHostings = hostingRepository.findAll();
            System.out.println("Total de hostings encontrados: " + allHostings.size());
            return new LinkedList<>(allHostings);
        } catch (Exception e) {
            System.err.println("Error al buscar todos los hostings: " + e.getMessage());
            e.printStackTrace();
            return new LinkedList<>(); // Devolvemos una lista vacía en caso de error
        }
    }


    public List<Hosting> findHostingsByCity(City city) {
        return findAllHostings().stream()
                .filter(h -> h.getCity().equals(city))
                .toList();
    }

    /**
     * Retorna true si el alojamiento está disponible en la fecha dada.
     * Por defecto, verifica que la fecha esté entre availableFrom y availableTo.
     */
    public boolean isAvailableOn(Hosting hosting, LocalDate date) {
        if (hosting == null || date == null) return false;
        LocalDate from = hosting.getAvailableFrom();
        LocalDate to = hosting.getAvailableTo();
        return (from == null || !date.isBefore(from)) && (to == null || !date.isAfter(to));
    }

    /**
     * Devuelve una lista de alojamientos disponibles para la fecha dada.
     */
    public List<Hosting> getAvailableHostingsOn(LocalDate date) {
        return findAllHostings().stream()
            .filter(h -> isAvailableOn(h, date))
            .toList();
    }    public void deleteHosting(String name) {
        Optional<House> house = houseRepository.findById(name);
        Optional<Apartament> apartament = apartamentRepository.findById(name);
        Optional<Hotel> hotel = hotelRepository.findById(name);

        if (house.isPresent()) {
            houseRepository.delete(house.get());
        } else if (apartament.isPresent()) {
            apartamentRepository.delete(apartament.get());
        } else if (hotel.isPresent()) {
            hotelRepository.delete(hotel.get());
        } else {
            throw new IllegalArgumentException("No se encontró el alojamiento con nombre: " + name);
        }
        
        // Persistir la eliminación en XML
        XmlSerializationManager.getInstance().saveAllData();
    }

    /**
     * Actualiza un alojamiento existente con los nuevos datos proporcionados.
     * Busca el alojamiento por nombre (o id único si lo tienes) y reemplaza sus datos.
     * Si no existe, lanza una excepción.
     */    public void updateHosting(Hosting updatedHosting) {
        if (updatedHosting instanceof House) {
            Optional<House> existing = houseRepository.findById(updatedHosting.getName());
            if (existing.isPresent()) {
                houseRepository.update((House) updatedHosting);
            } else {
                throw new IllegalArgumentException("No se encontró la casa a actualizar: " + updatedHosting.getName());
            }
        } else if (updatedHosting instanceof Apartament) {
            Optional<Apartament> existing = apartamentRepository.findById(updatedHosting.getName());
            if (existing.isPresent()) {
                apartamentRepository.update((Apartament) updatedHosting);
            } else {
                throw new IllegalArgumentException("No se encontró el apartamento a actualizar: " + updatedHosting.getName());
            }
        } else if (updatedHosting instanceof Hotel) {
            Optional<Hotel> existing = hotelRepository.findById(updatedHosting.getName());
            if (existing.isPresent()) {
                hotelRepository.update((Hotel) updatedHosting);
            } else {
                throw new IllegalArgumentException("No se encontró el hotel a actualizar: " + updatedHosting.getName());
            }
        } else {
            throw new IllegalArgumentException("Tipo de alojamiento no soportado para actualización");
        }
        
        // Persistir todos los datos después de la actualización
        XmlSerializationManager.getInstance().saveAllData();
    }
    /**
     * Verifica si un alojamiento tiene servicio de WiFi.
     * 
     * @param hosting el alojamiento a verificar
     * @return true si tiene WiFi, false en caso contrario
     */
    public boolean hasWifi(Hosting hosting) {
        return hosting.getIncludedServices().stream()
                .anyMatch(service -> service.getName().equalsIgnoreCase("wifi"));
    }

    /**
     * Verifica si un alojamiento tiene piscina.
     * 
     * @param hosting el alojamiento a verificar
     * @return true si tiene piscina, false en caso contrario
     */
    public boolean hasPool(Hosting hosting) {
        return hosting.getIncludedServices().stream()
                .anyMatch(service -> service.getName().equalsIgnoreCase("piscina"));
    }

    /**
     * Verifica si un alojamiento incluye desayuno.
     * 
     * @param hosting el alojamiento a verificar
     * @return true si incluye desayuno, false en caso contrario
     */
    public boolean hasBreakfast(Hosting hosting) {
        return hosting.getIncludedServices().stream()
                .anyMatch(service -> service.getName().equalsIgnoreCase("desayuno"));
    }

    /**
     * Añade una lista de alojamientos al sistema
     * @param hostings Lista de alojamientos a añadir
     */    public void addHostings(List<Hosting> hostings) {
        // Agregar cada hosting usando saveHosting que lo guarda en todos los repositorios necesarios
        // Ya no limpiamos los repositorios para mantener los datos existentes
        if (hostings != null) {
            for (Hosting hosting : hostings) {
                if (hosting != null) {
                    saveHosting(hosting);
                }
            }
        }
    }

    /**
     * Valida los parámetros básicos comunes a todos los alojamientos.
     * 
     * @param name nombre del alojamiento
     * @param city ciudad
     * @param description descripción
     * @param imageUrl URL de la imagen
     * @param priceForNight precio por noche
     * @param maxGuests número máximo de huéspedes
     * @param availableFrom fecha desde
     * @param availableTo fecha hasta
     * @throws IllegalArgumentException si algún parámetro no es válido
     */
    private void validateBasicParameters(String name, City city, String description, String imageUrl,
            double priceForNight, int maxGuests, LocalDate availableFrom, LocalDate availableTo) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (city == null) {
            throw new IllegalArgumentException("La ciudad no puede ser nula");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("La URL de la imagen no puede estar vacía");
        }
        if (priceForNight <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser positivo");
        }
        if (maxGuests <= 0) {
            throw new IllegalArgumentException("El número máximo de huéspedes debe ser positivo");
        }
        if (availableFrom == null || availableTo == null) {
            throw new IllegalArgumentException("Las fechas de disponibilidad no pueden ser nulas");
        }
        if (availableFrom.isAfter(availableTo)) {
            throw new IllegalArgumentException("La fecha desde no puede ser posterior a la fecha hasta");
        }
    }

    /**
     * Elimina todos los alojamientos del sistema.
     * Este método elimina todos los registros de alojamientos en todos los repositorios
     * y limpia también el repositorio general de hostings.
     */
    public void clearAll() {
        hostingRepository.clearAll();
        houseRepository.clearAll();
        apartamentRepository.clearAll();
        hotelRepository.clearAll();
        XmlSerializationManager.getInstance().saveAllData();
    }
}