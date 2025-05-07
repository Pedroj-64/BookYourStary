package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HostingService {

    private final HouseRepository houseRepository;
    private final ApartamentRepository apartamentRepository;
    private final HotelRepository hotelRepository;

    public HostingService(HouseRepository houseRepository, ApartamentRepository apartamentRepository,
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
            LinkedList<ServiceIncluded> services, double cleaningFee) {
        Hosting house = HostingFactory.createHouse(name, city, description, imageUrl,
                priceForNight, maxGuests, services, cleaningFee);
        saveHosting(house);
    }

    public void createHotel(String name, City city, String description, String imageUrl,
            double basePrice, int maxGuests,
            LinkedList<ServiceIncluded> services, LinkedList<Room> rooms) {
        Hosting hotel = HostingFactory.createHotel(name, city, description, imageUrl,
                basePrice, maxGuests, services, rooms);
        saveHosting(hotel);
    }

    public void createApartament(String name, City city, String description, String imageUrl,
            double priceForNight, int maxGuests,
            LinkedList<ServiceIncluded> services) {
        Hosting apartament = HostingFactory.createApartament(name, city, description, imageUrl, priceForNight,
                maxGuests, services, priceForNight);
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
}