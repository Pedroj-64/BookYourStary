package co.edu.uniquindio.poo.bookyourstary.model;

import java.io.Serializable;
import java.time.LocalDate;

import co.edu.uniquindio.poo.bookyourstary.model.enums.BookingState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String bookingId;
    private Client client;
    private Hosting hosting;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfGuests;
    private double totalPrice;
    private BookingState bookingState;

    public Booking(String bookingId, Client client, Hosting hosting, LocalDate startDate, LocalDate endDate,
            int numberOfGuests, double totalPrice, BookingState bookingState) {
        this.bookingId = bookingId;
        this.client = client;
        this.hosting = hosting;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
        this.bookingState = bookingState;
    }
}
