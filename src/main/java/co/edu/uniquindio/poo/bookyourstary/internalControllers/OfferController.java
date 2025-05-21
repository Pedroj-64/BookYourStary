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
            if (isOfferApplicable(offer, bookingDate, numberOfNights)) {
                discountedPrice = offerService.applyOffer(offer.getName(), discountedPrice, numberOfNights,
                        bookingDate);
            }
        }

        return discountedPrice;
    }

    /**
     * Aplica todas las ofertas globales (Long Stay y Special Date) automáticamente.
     * No depende de fechas de inicio/fin, sino de la lógica de la estrategia.
     */
    public double applyAllGlobalOffers(double originalPrice, int numberOfNights, LocalDate bookingDate) {
        double discountedPrice = originalPrice;
        List<Offer> offers = offerService.getAllOffers();
        for (Offer offer : offers) {
            // Aplica la estrategia de la oferta directamente
            double newPrice = offer.getStrategy().calculateDiscount(discountedPrice, numberOfNights, bookingDate);
            if (newPrice < discountedPrice) {
                discountedPrice = newPrice;
            }
        }
        return discountedPrice;
    }

    /**
     * Verifica si una oferta es aplicable dependiendo de la fecha y duración de la
     * reserva.
     * 
     * @param offer          La oferta a evaluar.
     * @param bookingDate    La fecha de la reserva.
     * @param numberOfNights La cantidad de noches que se reservan.
     * @return true si la oferta es aplicable, false en caso contrario.
     */
    private boolean isOfferApplicable(Offer offer, LocalDate bookingDate, int numberOfNights) {
        // Verificar si la oferta está dentro del rango de fechas
        boolean isDateInRange = !bookingDate.isBefore(offer.getStartDate()) && !bookingDate.isAfter(offer.getEndDate());

        // Verificar si la oferta es aplicable dependiendo de la estrategia
        boolean isApplicable = isDateInRange
                && offer.getStrategy().calculateDiscount(0, numberOfNights, bookingDate) < 1;

        return isApplicable;
    }
}
