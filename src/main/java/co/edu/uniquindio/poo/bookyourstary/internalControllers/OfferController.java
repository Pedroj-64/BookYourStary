package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import co.edu.uniquindio.poo.bookyourstary.model.Offer;
import co.edu.uniquindio.poo.bookyourstary.service.OfferService;

public class OfferController {

    private final OfferService offerService;

    private static OfferController instance;

    public static OfferController getInstance() {
        if (instance == null) {
            // Usar el singleton de OfferService si existe, o crearlo aquí si es necesario
            instance = new OfferController(OfferService.getInstance());
        }
        return instance;
    }

    private OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    /**
     * Automatiza la aplicación de las ofertas disponibles según la fecha de reserva
     * y la duración.
     * 
     * @param originalPrice  El precio original de la reserva.
     * @param numberOfNights El número de noches que se van a reservar.
     * @param bookingDate    La fecha de la reserva.
     * @return El precio con el descuento aplicado.
     */
    public double applyApplicableOffers(double originalPrice, int numberOfNights, LocalDate bookingDate) {
        double discountedPrice = originalPrice;

        // Obtener todas las ofertas disponibles
        List<Offer> offers = offerService.getAllOffers();

        // Iterar sobre las ofertas y aplicar aquellas que correspondan
        for (Offer offer : offers) {
            // Check if the bookingDate is within the offer's active period
            if (!bookingDate.isBefore(offer.getStartDate()) && !bookingDate.isAfter(offer.getEndDate())) {
                // The strategy itself will determine if it's applicable (e.g., min nights)
                // and return originalPrice if not, or discounted price if applicable.
                // The offerService.applyOffer might be redundant if the strategy is directly accessible and reliable.
                // Assuming offer.getStrategy().calculateDiscount IS the reliable way.
                discountedPrice = offer.getStrategy().calculateDiscount(discountedPrice, numberOfNights, bookingDate);
            }
        }
        return discountedPrice;
    }

    /**
     * Applies all offers based on their strategies if the bookingDate is within the offer's active period.
     * This method seems to be a duplicate or very similar to applyApplicableOffers if strategies are self-contained.
     * Keeping it for now but noting potential redundancy. If applyApplicableOffers is the primary method,
     * this one might be removed or refactored.
     * For consistency, this should also check offer active dates.
     */
    public double applyAllGlobalOffers(double originalPrice, int numberOfNights, LocalDate bookingDate) {
        double discountedPrice = originalPrice;
        List<Offer> offers = offerService.getAllOffers();
        for (Offer offer : offers) {
            // Check if the bookingDate is within the offer's active period
            if (!bookingDate.isBefore(offer.getStartDate()) && !bookingDate.isAfter(offer.getEndDate())) {
                 // Aplica la estrategia de la oferta directamente
                double newPrice = offer.getStrategy().calculateDiscount(discountedPrice, numberOfNights, bookingDate);
                // This check is fine if calculateDiscount returns the new price.
                if (newPrice < discountedPrice) {
                    discountedPrice = newPrice;
                }
            }
        }
        return discountedPrice;
    }

    // The isOfferApplicable method is removed as its logic was flawed and
    // the responsibility is better placed within the strategy or the loop in applyApplicableOffers.
}
