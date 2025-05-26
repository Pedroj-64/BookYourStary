package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Offer;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.time.LocalDate;
import java.util.List;

public interface IOfferService {
    /**
     * Crea una nueva oferta
     */
    Offer createOffer(double discountPercentage, LocalDate startDate, LocalDate endDate, int minNights);

    /**
     * Aplica una oferta a un alojamiento
     */
    void applyOfferToHosting(Offer offer, Hosting hosting);

    /**
     * Obtiene todas las ofertas activas
     */
    List<Offer> getActiveOffers();

    /**
     * Obtiene ofertas por rango de fechas
     */
    List<Offer> getOffersByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Calcula el precio con descuento aplicando ofertas disponibles
     */
    double calculateDiscountedPrice(double originalPrice, int numberOfNights, LocalDate startDate);

    /**
     * Elimina una oferta
     */
    void deleteOffer(String offerId);

    /**
     * Verifica si hay ofertas aplicables para una reserva
     */
    boolean hasApplicableOffers(int numberOfNights, LocalDate startDate);

    /**
     * Elimina todas las ofertas
     */
    void clearAll();
}
