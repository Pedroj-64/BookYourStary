package co.edu.uniquindio.poo.bookyourstary.model.strategy;

import java.time.LocalDate;

public interface OfferStrategy {

    double calculateDiscount(double originalPrice, int numberOfNights, LocalDate bookingDate);

}
