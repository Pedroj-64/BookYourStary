package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.service.BookingService;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.SessionManager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ManageOrderController {

    /**
     * Obtiene la lista de hostings pendientes de CartManager (carrito de compras).
     */
    public List<Hosting> getPendingHostings() {
        return MainController.getInstance().getCartManager().getPendingReservations();
    }

    /**
     * Crea y confirma las reservas para todos los hostings en el carrito, usando el cliente en sesión.
     * Limpia el carrito tras confirmar.
     */
    public void confirmAllBookings(LocalDate startDate, LocalDate endDate, int numberOfGuests) {
        Object usuario = SessionManager.getInstance().getUsuarioActual();
        if (!(usuario instanceof Client client)) {
            throw new IllegalStateException("No hay un cliente autenticado en la sesión");
        }
        BookingService bookingService = MainController.getInstance().getBookingService();
        List<Hosting> hostings = getPendingHostings();
        for (Hosting hosting : hostings) {
            try {
                // Crea la reserva (Booking) en estado PENDING
                var booking = bookingService.createBooking(client, hosting, startDate, endDate, numberOfGuests);
                // Confirma la reserva (descuenta saldo, cambia estado, etc.)
                bookingService.confirmBooking(booking.getBookingId());
            } catch (IllegalStateException e) {
                // Fondos insuficientes u otro error de confirmación
                MainController.showAlert(
                    "Fondos insuficientes",
                    "No tienes saldo suficiente para confirmar la reserva de: " + hosting.getName(),
                    AlertType.ERROR
                );
            
                break;
            }
        }
        // Limpia el carrito después de intentar confirmar todas las reservas
        MainController.getInstance().getCartManager().clear();
    }

    /**
     * Devuelve una lista de precios (uno por hosting pendiente) para el rango de fechas dado,
     * aplicando promociones/ofertas si existen.
     */    public List<Double> calculatePricesForPendingHostings(LocalDate startDate, LocalDate endDate) {
        List<Hosting> hostings = getPendingHostings();
        // Verificaciones de seguridad para evitar NullPointerException
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de inicio o fin no pueden ser nulas");
        }
        
        // Asegurar que hay al menos una noche
        long nights = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate));
        System.out.println("Calculando precios para " + hostings.size() + " alojamientos por " + nights + " noches");
        
        try {
            var offerController = MainController.getInstance().getOfferController();
            return hostings.stream()
                    .map(h -> {
                        if (h == null) {
                            System.err.println("¡Alerta! Alojamiento nulo encontrado en la lista de pendientes");
                            return 0.0; // Precio cero para alojamientos nulos
                        }
                        
                        double basePrice = h.getPricePerNight() * nights;
                        System.out.println("Precio base para " + h.getName() + ": $" + basePrice);
                        
                        double finalPrice;
                        try {
                            finalPrice = offerController.applyApplicableOffers(basePrice, (int) nights, startDate);
                            System.out.println("Precio final con descuentos para " + h.getName() + ": $" + finalPrice);
                        } catch (Exception e) {
                            System.err.println("Error al aplicar descuentos para " + h.getName() + ": " + e.getMessage());
                            finalPrice = basePrice; // Usar precio base si hay error en los descuentos
                        }
                        
                        return finalPrice;
                    })
                    .toList();
        } catch (Exception e) {
            System.err.println("Error general al calcular precios: " + e.getMessage());
            e.printStackTrace();
            // En caso de error, devolver lista vacía en lugar de null
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Suma todos los precios de los hostings pendientes y devuelve el subtotal.
     */
    public double calculateSubtotalForPendingHostings(LocalDate startDate, LocalDate endDate) {
        List<Double> prices = calculatePricesForPendingHostings(startDate, endDate);
        return prices.stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Generates a summary of discounts applied to each pending hosting and displays it in the TextArea.
     * The format is: "Discount [offerName] applied to [hostingName]"
     * Also shows the subtotal at the end.
     */
    public void showAppliedDiscountsSummary(LocalDate startDate, LocalDate endDate, TextArea textArea) {
        List<Hosting> hostings = getPendingHostings();
        long nights = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
        var offerController = MainController.getInstance().getOfferController();
        var offerService = MainController.getInstance().getOfferService();
        StringBuilder sb = new StringBuilder();
        for (Hosting hosting : hostings) {
            double basePrice = hosting.getPricePerNight() * nights;
            var offers = offerService.getAllOffers();
            boolean anyApplied = false;
            for (var offer : offers) {
                double priceWithOffer = offerService.applyOffer(offer.getName(), basePrice, (int) nights, startDate);
                if (offerController.applyApplicableOffers(basePrice, (int) nights, startDate) < basePrice &&
                    priceWithOffer < basePrice) {
                    sb.append("Discount '").append(offer.getName()).append("' applied to '")
                      .append(hosting.getName()).append("'\n");
                    anyApplied = true;
                }
            }
            if (!anyApplied) {
                sb.append("No discount applied to '").append(hosting.getName()).append("'\n");
            }
        }
        double subtotal = calculateSubtotalForPendingHostings(startDate, endDate);
        sb.append("\nSubtotal: $").append(String.format("%.2f", subtotal));
        textArea.setText(sb.toString());
    }

    /**
     * Confirms a booking for a single hosting, removing it from the cart if successful.
     * Returns true if booking was successful, false otherwise.
     */
    public boolean confirmSingleBooking(Hosting hosting, LocalDate startDate, LocalDate endDate, int numberOfGuests) {
        Object usuario = SessionManager.getInstance().getUsuarioActual();
        if (!(usuario instanceof Client client)) {
            MainController.showAlert(
                "Authentication Error",
                "No authenticated client in session. Please log in.",
                AlertType.ERROR
            );
            return false;
        }
        BookingService bookingService = MainController.getInstance().getBookingService();
        try {
            var booking = bookingService.createBooking(client, hosting, startDate, endDate, numberOfGuests);
            bookingService.confirmBooking(booking.getBookingId());
            MainController.getInstance().getCartManager().remove(hosting); // Remove from cart on success
            // Success alert will be handled by the ViewController, this method just returns status
            return true;
        } catch (IllegalArgumentException e) { // Handles errors from createBooking (invalid dates, capacity)
            MainController.showAlert(
                "Booking Error",
                "Could not create booking for: " + hosting.getName() + ". Reason: " + e.getMessage(),
                AlertType.ERROR
            );
            return false;
        } catch (IllegalStateException e) { // Handles errors from confirmBooking (insufficient funds)
            MainController.showAlert(
                "Booking Failed",
                "Could not confirm booking for: " + hosting.getName() + ". Reason: " + e.getMessage(),
                AlertType.ERROR
            );
            return false;
        } catch (Exception e) { // Catch any other unexpected errors
            MainController.showAlert(
                "Unexpected Error",
                "An unexpected error occurred while booking " + hosting.getName() + ": " + e.getMessage(),
                AlertType.ERROR
            );
            return false;
        }
    }
}
