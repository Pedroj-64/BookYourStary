package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.service.BookingService;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        if (startDate == null || endDate == null) {
            textArea.setText("Por favor, seleccione fechas válidas para ver los descuentos aplicables.");
            return;
        }

        int nights = (int) ChronoUnit.DAYS.between(startDate, endDate);
        if (nights <= 0) {
            textArea.setText("El rango de fechas seleccionado debe ser de al menos una noche.");
            return;
        }

        StringBuilder sb = new StringBuilder("Resumen de descuentos:\n\n");
        boolean anyApplied = false;
        double totalSavings = 0.0;

        List<Hosting> pendingHostings = getPendingHostings();
        
        for (Hosting hosting : pendingHostings) {
            try {
                double basePrice = hosting.getPricePerNight() * nights;
                double discountedPrice = MainController.getInstance()
                    .getOfferController()
                    .applyApplicableOffers(basePrice, nights, startDate);
                
                if (discountedPrice < basePrice) {
                    anyApplied = true;
                    double savings = basePrice - discountedPrice;
                    totalSavings += savings;
                    sb.append(String.format("%s:%n", hosting.getName()));
                    sb.append(String.format("  Precio original: $%.2f%n", basePrice));
                    sb.append(String.format("  Precio con descuento: $%.2f%n", discountedPrice));
                    sb.append(String.format("  Ahorro: $%.2f%n%n", savings));
                }
            } catch (Exception e) {
                System.err.println("Error calculando descuentos para " + hosting.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (anyApplied) {
            sb.append(String.format("\nAhorro total: $%.2f", totalSavings));
        } else {
            sb.append("No hay descuentos aplicables para las fechas seleccionadas.");
        }

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

    /**
     * Inicializa los valores por defecto para un nuevo formulario de reserva
     * @param datePicker_inicio Selector de fecha inicial
     * @param datePicker_fin Selector de fecha final
     * @param txt_numHuespedes Campo de texto para número de huéspedes
     */
    public void initializeDefaultValues(DatePicker datePicker_inicio, DatePicker datePicker_fin, TextField txt_numHuespedes) {
        // Obtener la fecha actual
        LocalDate today = LocalDate.now();
        
        // Configurar el DatePicker de inicio
        datePicker_inicio.setValue(today);
        datePicker_inicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        // Configurar el DatePicker de fin
        LocalDate tomorrow = today.plusDays(1);
        datePicker_fin.setValue(tomorrow);
        datePicker_fin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(tomorrow));
            }
        });

        // Agregar listener para mantener la lógica de fechas
        datePicker_inicio.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                LocalDate minEndDate = newVal.plusDays(1);
                if (datePicker_fin.getValue() == null || datePicker_fin.getValue().isBefore(minEndDate)) {
                    datePicker_fin.setValue(minEndDate);
                }
            }
        });

        txt_numHuespedes.setText("1");
    }

    /**
     * Valida si los datos del formulario son válidos para realizar una reserva
     */
    public boolean validateBookingForm(LocalDate startDate, LocalDate endDate, String numGuestsText) {
        LocalDate today = LocalDate.now();
        
        // Validación de fechas nulas
        if (startDate == null || endDate == null) {
            MainController.showAlert(
                "Error de validación",
                "Las fechas de inicio y fin son obligatorias.",
                AlertType.ERROR
            );
            return false;
        }

        // Validación de fecha inicial
        if (startDate.isBefore(today)) {
            MainController.showAlert(
                "Error de validación",
                "La fecha de inicio no puede ser anterior a hoy.",
                AlertType.ERROR
            );
            return false;
        }

        // Validación de rango de fechas
        if (!startDate.isBefore(endDate)) {
            MainController.showAlert(
                "Error de validación",
                "La fecha de fin debe ser posterior a la fecha de inicio.",
                AlertType.ERROR
            );
            return false;
        }

        // Validación de duración máxima (ejemplo: 30 días)
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days > 30) {
            MainController.showAlert(
                "Error de validación",
                "La duración máxima de una reserva es de 30 días.",
                AlertType.ERROR
            );
            return false;
        }

        // Validación de huéspedes
        try {
            int numGuests = Integer.parseInt(numGuestsText);
            if (numGuests <= 0) {
                MainController.showAlert(
                    "Error de validación",
                    "El número de huéspedes debe ser mayor que cero.",
                    AlertType.ERROR
                );
                return false;
            }
            if (numGuests > 10) { // ejemplo de límite máximo
                MainController.showAlert(
                    "Error de validación",
                    "El número máximo de huéspedes permitido es 10.",
                    AlertType.ERROR
                );
                return false;
            }
        } catch (NumberFormatException e) {
            MainController.showAlert(
                "Error de validación",
                "Por favor, ingrese un número válido de huéspedes.",
                AlertType.ERROR
            );
            return false;
        }
        
        return true;
    }

    /**
     * Analiza y devuelve el número de huéspedes del texto ingresado
     * @throws IllegalArgumentException si el número no es válido
     */
    public int parseNumGuests(String numGuestsText) throws IllegalArgumentException {
        try {
            int numGuests = Integer.parseInt(numGuestsText);
            if (numGuests <= 0) {
                throw new IllegalArgumentException("El número de huéspedes debe ser mayor que cero.");
            }
            return numGuests;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Por favor, ingrese un número válido de huéspedes.");
        }
    }
}
