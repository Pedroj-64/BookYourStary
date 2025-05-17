package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Offer;

public class OfferRepository {

    private final LinkedList<Offer> offers;
    private static OfferRepository instance;

    private OfferRepository() {
        this.offers = new LinkedList<>();
    }

    public static OfferRepository getInstance() {
        if (instance == null) {
            instance = new OfferRepository();
        }
        return instance;
    }

    public void save(Offer offer) {
        offers.add(offer);
    }

    public Optional<Offer> findByName(String name) {
        return offers.stream().filter(offer -> offer.getName().equals(name)).findFirst();
    }

    public void delete(Offer offer) {
        offers.remove(offer);
    }

    public LinkedList<Offer> findAll() {
        return new LinkedList<>(offers);
    }

}
