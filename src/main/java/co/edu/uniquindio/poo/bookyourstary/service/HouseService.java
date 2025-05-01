package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.HouseRepository;

import java.util.LinkedList;
import java.util.Optional;

public class HouseService {

    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public void saveHouse(String name, City city, String description, String imageUrl, double priceForNight,
                          int maxGuests, LinkedList<ServiceIncluded> includedServices, double cleaningFee) {

        Hosting house = HostingFactory.createHouse(name, city, description, imageUrl, priceForNight,
                maxGuests, includedServices, cleaningFee);
        

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
}
