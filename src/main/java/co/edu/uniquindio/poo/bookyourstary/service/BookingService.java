package co.edu.uniquindio.poo.bookyourstary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uniquindio.poo.bookyourstary.model.Booking;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.enums.BookingState;
import co.edu.uniquindio.poo.bookyourstary.repository.BookingRepository;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Client client, Hosting hosting, LocalDate startDate, LocalDate endDate, int numberOfGuests) {

        validateDates(startDate, endDate);
        validateGuestCapacity(hosting, numberOfGuests);

        double costPerNight = hosting.getPricePerNight();
        long nights = startDate.until(endDate).getDays();
        double totalPrice = costPerNight * nights;

        String bookingId = UUID.randomUUID().toString();

        Booking booking = new Booking(
            bookingId,
            client,
            hosting,
            startDate,
            endDate,
            numberOfGuests,
            totalPrice,
            BookingState.PENDING
        );

        bookingRepository.save(booking);
        return booking;
    }

    public Optional<Booking> findById(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public void deleteBooking(String bookingId) {
        bookingRepository.delete(bookingId);
    }

    public void updateBookingState(String bookingId, BookingState newState) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        booking.setBookingState(newState);
    }


    private void validateDates(LocalDate start, LocalDate end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la de fin.");
        }
        if (start.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser futura.");
        }
    }

    private void validateGuestCapacity(Hosting hosting, int guests) {
        if (guests <= 0 || guests > hosting.getMaxGuests()) {
            throw new IllegalArgumentException("Cantidad de huéspedes inválida para este alojamiento.");
        }
    }

    public long calculateNumberOfNights(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
    
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }
    
        return startDate.until(endDate).getDays(); 
    }
    
}
