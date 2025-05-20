package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.HouseRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HouseService {

    private final HouseRepository houseRepository;
    private static HouseService instance;

    public static HouseService getInstance() {
        if (instance == null) {
            instance = new HouseService(HouseRepository.getInstance());
        }
        return instance;
    }

    private HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public void saveHouse(String name, City city, String description, String imageUrl, double priceForNight,
                          int maxGuests, LinkedList<ServiceIncluded> includedServices, double cleaningFee,LocalDate availableFrom, LocalDate availableTo) {

        Hosting house = HostingFactory.createHouse(name, city, description, imageUrl, priceForNight,
                maxGuests, includedServices, cleaningFee, availableFrom, availableTo);
        

        houseRepository.save((House)house);
    }

    public Optional<House> findHouseById(String name) {
        return houseRepository.findById(name);
    }

    public LinkedList<House> findAllHouses() {
        return houseRepository.findAll();
    }

    public void deleteHouse(String name) {
        House house = findHouseById(name)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la casa con ese nombre"));
        houseRepository.delete(house);
    }

    public double calculateTotalPrice(House house, int numberOfNights) {
        return house.getPricePerNight() * numberOfNights + house.getCleaningFee();
    }

    public List<House> findAllAvailableHouses() {
        return houseRepository.findAll().stream()
                .filter(h -> h.getAvailableTo().isAfter(LocalDate.now()))
                .toList();
    }
}
