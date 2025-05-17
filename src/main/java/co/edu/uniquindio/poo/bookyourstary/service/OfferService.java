package co.edu.uniquindio.poo.bookyourstary.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Offer;
import co.edu.uniquindio.poo.bookyourstary.model.strategy.OfferStrategy;
import co.edu.uniquindio.poo.bookyourstary.repository.OfferRepository;

public class OfferService {

    private final OfferRepository offerRepository;

    private static OfferService instance;

    private OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public static OfferService getInstance() {
        if (instance == null) {
            instance = new OfferService(OfferRepository.getInstance());
        }
        return instance;
    }

    public void addOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public Optional<Offer> findOfferByName(String name) {
        return offerRepository.findByName(name);
    }

    public LinkedList<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public void deleteOffer(String name) {
        Optional<Offer> offerOpt = offerRepository.findByName(name);
        offerOpt.ifPresent(offerRepository::delete);
    }

    public double applyOffer(String offerName, double originalPrice, int numberOfNights, LocalDate bookingDate) {
        Optional<Offer> offerOpt = offerRepository.findByName(offerName);
        if (offerOpt.isPresent()) {
            Offer offer = offerOpt.get();
            OfferStrategy strategy = offer.getStrategy();
            return strategy.calculateDiscount(originalPrice, numberOfNights, bookingDate);
        }
        // Si la oferta no se encuentra, devolver el precio original
        return originalPrice;
    }
}
