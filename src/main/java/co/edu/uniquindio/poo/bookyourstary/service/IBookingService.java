package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.enums.BookingState;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IBookingService {
    /**
     * Crea una nueva reserva
     */
    Booking createBooking(Client client, Hosting hosting, LocalDate startDate, LocalDate endDate, int numberOfGuests);

    /**
     * Obtiene una reserva por su ID
     */
    Optional<Booking> findById(String bookingId);

    /**
     * Obtiene todas las reservas
     */
    List<Booking> findAll();

    /**
     * Elimina una reserva
     */
    void deleteBooking(String bookingId);

    /**
     * Actualiza el estado de una reserva
     */
    void updateBookingState(String bookingId, BookingState newState);

    /**
     * Confirma una reserva
     */
    void confirmBooking(String bookingId);

    /**
     * Cancela una reserva
     */
    void cancelBooking(String bookingId);

    /**
     * Obtiene las reservas de un cliente
     */
    List<Booking> getBookingsByClient(String clientId);

    /**
     * Obtiene las reservas de un alojamiento
     */
    List<Booking> getBookingsByHosting(String hostingName);

    /**
     * Verifica disponibilidad de un alojamiento
     */
    boolean isHostingAvailable(String hostingName, LocalDate startDate, LocalDate endDate);

    /**
     * Calcula el número de noches entre dos fechas
     */
    long calculateNumberOfNights(LocalDate startDate, LocalDate endDate);

    /**
     * Elimina todas las reservas
     */
    void clearAll();
}
