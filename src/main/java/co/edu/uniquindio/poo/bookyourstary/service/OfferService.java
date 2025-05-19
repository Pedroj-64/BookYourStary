package co.edu.uniquindio.poo.bookyourstary.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import co.edu.uniquindio.poo.bookyourstary.config.mapping.FestivosColombia;
import co.edu.uniquindio.poo.bookyourstary.model.Offer;
import co.edu.uniquindio.poo.bookyourstary.model.strategy.LongStayDiscountStrategy;
import co.edu.uniquindio.poo.bookyourstary.model.strategy.OfferStrategy;
import co.edu.uniquindio.poo.bookyourstary.model.strategy.SpecialDateDiscountStrategy;
import co.edu.uniquindio.poo.bookyourstary.repository.OfferRepository;

public class OfferService {

    private final OfferRepository offerRepository;

    private static OfferService instance;

    private OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
        ensureDefaultOffers();
    }

    private void ensureDefaultOffers() {
        // Long Stay Offer (more than 5 nights, 10% discount)
        if (findOfferByName("Long Stay").isEmpty()) {
            addOffer(new Offer(
                "Long Stay",
                "10% discount for long stays (more than 5 nights)",
                new LongStayDiscountStrategy(5, 10),
                null, null
            ));
        }
        // Special Date Offer (Colombian holidays)
        Set<LocalDate> holidays = FestivosColombia.obtenerFestivos(LocalDate.now().getYear());
        for (LocalDate holiday : holidays) {
            String name = "Special Date: " + holiday;
            if (findOfferByName(name).isEmpty()) {
                addOffer(new Offer(
                    name,
                    "15% discount for Colombian holiday: " + holiday,
                    new SpecialDateDiscountStrategy(holiday, 15),
                    holiday, holiday
                ));
            }
        }
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
