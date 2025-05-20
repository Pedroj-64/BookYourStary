package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Apartament;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.ApartamentRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ApartamentService {

    private final ApartamentRepository apartamentRepository;
    private static ApartamentService instance;

    public static ApartamentService getInstance() {
        if (instance == null) {
            instance = new ApartamentService(ApartamentRepository.getInstance());
        }
        return instance;
    }

    private ApartamentService(ApartamentRepository apartamentRepository) {
        this.apartamentRepository = apartamentRepository;
    }

    public void saveApartament(String name, City city, String description, String imageUrl, double priceForNight,
            int maxGuests, LinkedList<ServiceIncluded> includedServices, double priceForCleaning,LocalDate availableFrom,
            LocalDate availableTo) {
    
        Hosting apartament = HostingFactory.createApartament(name, city, description, imageUrl, priceForNight,
                maxGuests, includedServices, priceForCleaning,availableFrom, availableTo);

        apartamentRepository.save((Apartament) apartament);
    }

    public Optional<Apartament> findApartamentById(String name) {
        return apartamentRepository.findById(name);
    }

    public LinkedList<Apartament> findAllApartaments() {
        return apartamentRepository.findAll();
    }

    public void deleteApartament(String name) {
        Apartament apartament = findApartamentById(name)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el apartamento con ese nombre"));
        apartamentRepository.delete(apartament);
    }

    public double calculateTotalPrice(Apartament apartament, int numberOfNights) {
        return apartament.getPricePerNight() * numberOfNights + apartament.getPriceForCleaning();
    }

    public List<Apartament> findAllAvailableApartaments() {
        return apartamentRepository.findAll().stream()
                .filter(a -> a.getAvailableTo().isAfter(LocalDate.now()))
                .toList();
    }
}
