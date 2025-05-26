package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.Offer;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.OfferService;

/**
 * Controlador interno para gestionar la aplicación de descuentos a reservas.
 * Centraliza la lógica de descuentos para que pueda ser reutilizada en varios
 * controladores.
 */
public class DiscountManager {

    private static DiscountManager instance;
    private final OfferService offerService;

    private DiscountManager() {
        this.offerService = MainController.getInstance().getOfferService();
    }

    public static synchronized DiscountManager getInstance() {
        if (instance == null) {
            instance = new DiscountManager();
        }
        return instance;
    }

    /**
     * Calcula el precio con descuentos para un alojamiento en un rango de fechas.
     * 
     * @param hosting   Alojamiento para el cual calcular el precio
     * @param startDate Fecha de inicio de la reserva
     * @param endDate   Fecha de fin de la reserva
     * @return Precio con los descuentos aplicados
     */
    public double calculateDiscountedPrice(Hosting hosting, LocalDate startDate, LocalDate endDate) {
        // Calcular número de noches
        long nights = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));

        // Precio base sin descuentos
        double basePrice = hosting.getPricePerNight() * nights;

        // Obtener la mejor oferta para este alojamiento
        double bestPrice = basePrice;

        // Aplicar todas las ofertas aplicables y quedarse con el mejor precio
        List<Offer> offers = offerService.getAllOffers();
        for (Offer offer : offers) {
            if (isOfferValidForDate(offer, startDate)) {
                double priceWithOffer = offer.getStrategy().calculateDiscount(basePrice, (int) nights, startDate);
                if (priceWithOffer < bestPrice) {
                    bestPrice = priceWithOffer;
                }
            }
        }

        return bestPrice;
    }

    /**
     * Obtiene todas las ofertas aplicables a un alojamiento con sus precios.
     * 
     * @param hosting   Alojamiento
     * @param startDate Fecha de inicio
     * @param endDate   Fecha de fin
     * @return Mapa con las ofertas aplicables y los precios resultantes
     */
    public Map<Offer, Double> getApplicableOffersWithPrices(Hosting hosting, LocalDate startDate, LocalDate endDate) {
        Map<Offer, Double> result = new HashMap<>();
        long nights = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
        double basePrice = hosting.getPricePerNight() * nights;

        // Verificar cada oferta disponible
        List<Offer> offers = offerService.getAllOffers();
        for (Offer offer : offers) {
            if (isOfferValidForDate(offer, startDate)) {
                double priceWithOffer = offer.getStrategy().calculateDiscount(basePrice, (int) nights, startDate);
                if (priceWithOffer < basePrice) {
                    result.put(offer, priceWithOffer);
                }
            }
        }

        return result;
    }

    /**
     * Obtiene la mejor oferta para un alojamiento en las fechas especificadas.
     * 
     * @param hosting   Alojamiento
     * @param startDate Fecha de inicio
     * @param endDate   Fecha de fin
     * @return La mejor oferta y su precio, o null si no hay ofertas aplicables
     */
    public Entry<Offer, Double> getBestOfferWithPrice(Hosting hosting, LocalDate startDate, LocalDate endDate) {
        Map<Offer, Double> offers = getApplicableOffersWithPrices(hosting, startDate, endDate);
        if (offers.isEmpty()) {
            return null;
        }

        // Encontrar la mejor oferta (la que dé el precio más bajo)
        Offer bestOffer = null;
        double bestPrice = Double.MAX_VALUE;

        for (Entry<Offer, Double> entry : offers.entrySet()) {
            if (entry.getValue() < bestPrice) {
                bestPrice = entry.getValue();
                bestOffer = entry.getKey();
            }
        }

        // Usar Map.Entry para devolver tanto la oferta como el precio
        return Map.entry(bestOffer, bestPrice);
    }

    /**
     * Genera un resumen detallado de los descuentos aplicados y los precios.
     * 
     * @param hostings  Lista de alojamientos
     * @param startDate Fecha de inicio
     * @param endDate   Fecha de fin
     * @return Texto formateado con el resumen de descuentos
     */
    public String generateDiscountSummary(List<Hosting> hostings, LocalDate startDate, LocalDate endDate) {
        if (hostings == null || hostings.isEmpty()) {
            return "No hay alojamientos seleccionados para calcular descuentos.";
        }

        StringBuilder summary = new StringBuilder();
        double totalOriginal = 0;
        double totalFinal = 0;
        long nights = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));

        summary.append("Resumen de Descuentos para ").append(hostings.size())
                .append(" alojamiento(s) - ").append(nights).append(" noche(s)\n");
        summary.append("------------------------------------------------\n\n");

        for (Hosting hosting : hostings) {
            double originalPrice = hosting.getPricePerNight() * nights;
            totalOriginal += originalPrice;

            summary.append("ALOJAMIENTO: ").append(hosting.getName()).append("\n");
            summary.append("  Precio base: $").append(String.format("%.2f", originalPrice)).append("\n");

            // Obtener todas las ofertas aplicables con sus precios
            Map<Offer, Double> offers = getApplicableOffersWithPrices(hosting, startDate, endDate);

            if (offers.isEmpty()) {
                summary.append("  * Sin descuentos disponibles *\n");
                totalFinal += originalPrice;
            } else {
                // Mostrar todas las ofertas disponibles
                summary.append("  Ofertas disponibles:\n");

                // Encontrar la mejor oferta
                double bestPrice = originalPrice;
                String bestOfferName = "";

                for (Map.Entry<Offer, Double> entry : offers.entrySet()) {
                    Offer offer = entry.getKey();
                    double price = entry.getValue();
                    double savings = originalPrice - price;
                    double percentage = (savings / originalPrice) * 100;

                    summary.append("    - ").append(offer.getName())
                            .append(": $").append(String.format("%.2f", price))
                            .append(" (ahorro: $").append(String.format("%.2f", savings))
                            .append(", ").append(String.format("%.1f", percentage)).append("%)\n");

                    if (price < bestPrice) {
                        bestPrice = price;
                        bestOfferName = offer.getName();
                    }
                }

                // Resaltar la mejor oferta
                summary.append("  ** MEJOR OFERTA: ").append(bestOfferName)
                        .append(" - Precio final: $").append(String.format("%.2f", bestPrice))
                        .append(" **\n");

                totalFinal += bestPrice;
            }

            summary.append("\n");
        }

        // Mostrar totales
        double totalSavings = totalOriginal - totalFinal;
        double totalPercentage = totalOriginal > 0 ? (totalSavings / totalOriginal) * 100 : 0;

        summary.append("================================================\n");
        summary.append("TOTAL ORIGINAL: $").append(String.format("%.2f", totalOriginal)).append("\n");
        summary.append("TOTAL CON DESCUENTOS: $").append(String.format("%.2f", totalFinal)).append("\n");
        summary.append("AHORRO TOTAL: $").append(String.format("%.2f", totalSavings))
                .append(" (").append(String.format("%.1f", totalPercentage)).append("%)\n");

        return summary.toString();
    }

    /**
     * Verifica si una oferta es válida para una fecha determinada.
     */
    private boolean isOfferValidForDate(Offer offer, LocalDate date) {
        // Si la oferta no tiene fechas específicas, se considera siempre válida
        if (offer.getStartDate() == null || offer.getEndDate() == null) {
            return true;
        }

        // La fecha debe estar dentro del rango de validez de la oferta
        return !date.isBefore(offer.getStartDate()) && !date.isAfter(offer.getEndDate());
    }
}
