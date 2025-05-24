package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bill {

    private String billId;
    private double subtotal;
    private double total;
    private LocalDate date;
    private Client client;
    private Booking booking;

    public Bill(String billId, double total, LocalDate date, Client client, Booking booking, double subtotal) {
        this.billId = billId;
        this.total = total;
        this.subtotal = subtotal;
        this.date = date;
        this.client = client;
        this.booking = booking;
    }

}
