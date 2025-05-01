package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Apartament;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.ApartamentRepository;

import java.util.LinkedList;
import java.util.Optional;

public class ApartamentService {

    private final ApartamentRepository apartamentRepository;

    public ApartamentService(ApartamentRepository apartamentRepository) {
        this.apartamentRepository = apartamentRepository;
    }

    public void saveApartament(String name, City city, String description, String imageUrl, double priceForNight,
            int maxGuests, LinkedList<ServiceIncluded> includedServices, double priceForCleaning) {
    
        Hosting apartament = HostingFactory.createApartament(name, city, description, imageUrl, priceForNight,
                maxGuests, includedServices, priceForCleaning);

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
}
