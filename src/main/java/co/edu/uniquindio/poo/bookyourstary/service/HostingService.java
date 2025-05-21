package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HostingService {

    private final HouseRepository houseRepository;
    private final ApartamentRepository apartamentRepository;
    private final HotelRepository hotelRepository;

    private static HostingService instance;

    public static HostingService getInstance() {
        if (instance == null) {
            instance = new HostingService(HouseRepository.getInstance(), ApartamentRepository.getInstance(), HotelRepository.getInstance());
        }
        return instance;
    }

    private HostingService(HouseRepository houseRepository, ApartamentRepository apartamentRepository,
            HotelRepository hotelRepository) {
        this.houseRepository = houseRepository;
        this.apartamentRepository = apartamentRepository;
        this.hotelRepository = hotelRepository;
    }

  
    public void saveHosting(Hosting hosting) {
        if (hosting instanceof House) {
            houseRepository.save((House) hosting);
        } else if (hosting instanceof Apartament) {
            apartamentRepository.save((Apartament) hosting);
        } else if (hosting instanceof Hotel) {
            hotelRepository.save((Hotel) hosting);
        } else {
            throw new IllegalArgumentException("Tipo de alojamiento no soportado");
        }
    }

    // Métodos específicos para creación usando el Factory
    public void createHouse(String name, City city, String description, String imageUrl,
            double priceForNight, int maxGuests,
            LinkedList<ServiceIncluded> services, double cleaningFee,LocalDate availableFrom, LocalDate availableTo) {
        Hosting house = HostingFactory.createHouse(name, city, description, imageUrl,
                priceForNight, maxGuests, services, cleaningFee,availableFrom, availableTo);
        saveHosting(house);
    }

    public void createHotel(String name, City city, String description, String imageUrl,
            double basePrice, int maxGuests,
            LinkedList<ServiceIncluded> services, LinkedList<Room> rooms,LocalDate availableFrom, LocalDate availableTo) {
        Hosting hotel = HostingFactory.createHotel(name, city, description, imageUrl,
                basePrice, maxGuests, services, rooms,availableFrom, availableTo);
        saveHosting(hotel);
    }

    public void createApartament(String name, City city, String description, String imageUrl,
            double priceForNight, int maxGuests,
            LinkedList<ServiceIncluded> services,LocalDate availableFrom, LocalDate availableTo) {
        Hosting apartament = HostingFactory.createApartament(name, city, description, imageUrl, priceForNight,
                maxGuests, services, priceForNight,availableFrom, availableTo);
        saveHosting(apartament);
    }

 
    public List<Hosting> findAllHostings() {
        List<Hosting> hostings = new LinkedList<>();
        hostings.addAll(houseRepository.findAll());
        hostings.addAll(apartamentRepository.findAll());
        hostings.addAll(hotelRepository.findAll());
        return hostings;
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
    }

    public void deleteHosting(String name) {
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
    }

    /**
     * Actualiza un alojamiento existente con los nuevos datos proporcionados.
     * Busca el alojamiento por nombre (o id único si lo tienes) y reemplaza sus datos.
     * Si no existe, lanza una excepción.
     */
    public void updateHosting(Hosting updatedHosting) {
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
    }

    public boolean hasWifi(Hosting hosting) {
        if (hosting == null || hosting.getIncludedServices() == null) return false;
        return hosting.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("wifi"));
    }

    public boolean hasPool(Hosting hosting) {
        if (hosting == null || hosting.getIncludedServices() == null) return false;
        return hosting.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("piscina"));
    }

    public boolean hasBreakfast(Hosting hosting) {
        if (hosting == null || hosting.getIncludedServices() == null) return false;
        return hosting.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("desayuno"));
    }

    
   
    public void addHostings(List<Hosting> hostings) {
        for (Hosting hosting : hostings) {
            saveHosting(hosting);
        }
    }
}