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
    public double applyApplicableOffers(double basePrice, int numberOfNights, LocalDate bookingDate) {
        if (bookingDate == null) {
            System.err.println("Warning: Booking date is null, returning base price without discounts");
            return basePrice;
        }

        double finalPrice = basePrice;
        StringBuilder appliedOffers = new StringBuilder();
        List<Offer> offers = offerService.getAllOffers();

        for (Offer offer : offers) {
            if (offer.getStartDate() == null || offer.getEndDate() == null) {
                System.err.println("Warning: Offer dates are null for offer: " + offer.getName());
                continue;
            }

            // Verificar si la oferta está vigente
            if (!bookingDate.isBefore(offer.getStartDate()) && 
                !bookingDate.isAfter(offer.getEndDate())) {
                
                // Usar la estrategia de descuento de la oferta
                double newPrice = offer.getStrategy().calculateDiscount(basePrice, numberOfNights, bookingDate);
                
                if (newPrice < finalPrice) {
                    double discount = finalPrice - newPrice;
                    finalPrice = newPrice;
                    appliedOffers.append(String.format("Applied %s: -$%.2f%n", 
                        offer.getName(), discount));
                }
            }
        }

        if (appliedOffers.length() > 0) {
            System.out.println("Applied discounts:\n" + appliedOffers.toString());
        }

        return Math.max(0, finalPrice);
    }

    // Eliminar el método applyAllGlobalOffers ya que su funcionalidad está duplicada
    // y menos completa que applyApplicableOffers
}
