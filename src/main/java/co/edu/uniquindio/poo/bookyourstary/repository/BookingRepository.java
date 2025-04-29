package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Booking;

public class BookingRepository {

    private final LinkedList<Booking> bookings;

    public BookingRepository() {
        this.bookings = new LinkedList<>();
    }

    public void save(Booking booking) {
        bookings.add(booking);
    }

    public Optional<Booking> findById(String bookingId) {
        return bookings.stream().filter(booking -> booking.getBookingId().equals(bookingId)).findFirst();
    }

    public LinkedList<Booking> findAll() {
        return new LinkedList<>(bookings);
    }

    public void delete(String bookingId) {
        bookings.removeIf(booking -> booking.getBookingId().equals(bookingId));
    }
}
