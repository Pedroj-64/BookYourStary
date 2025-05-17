package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Booking;

public class BookingRepository {

    private final LinkedList<Booking> bookings;
    private static BookingRepository instance;

    private BookingRepository() {
        this.bookings = new LinkedList<>();
    }

    public static BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepository();
        }
        return instance;
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

    public LinkedList<Booking> findByClientId(String clientId) {
        LinkedList<Booking> result = new LinkedList<>();
        for (Booking booking : bookings) {
            if (booking.getClient().getId().equals(clientId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public LinkedList<Booking> findByHostingName(String hostingName) {
        LinkedList<Booking> result = new LinkedList<>();
        for (Booking booking : bookings) {
            if (booking.getHosting().getName().equalsIgnoreCase(hostingName)) {
                result.add(booking);
            }
        }
        return result;
    }

}
