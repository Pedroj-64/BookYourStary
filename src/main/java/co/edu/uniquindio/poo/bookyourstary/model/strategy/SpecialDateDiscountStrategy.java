package co.edu.uniquindio.poo.bookyourstary.model.strategy;

import java.time.LocalDate;

public class SpecialDateDiscountStrategy  implements OfferStrategy{

    private LocalDate specialDate;
    private double discountPercentage;

    public SpecialDateDiscountStrategy(LocalDate specialDate, double discountPercentage) {
        this.specialDate = specialDate;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculateDiscount(double originalPrice, int numberOfNights, LocalDate bookingDate) {
        if (bookingDate.equals(specialDate)) {
            return originalPrice * (1 - discountPercentage / 100);
        }
        return originalPrice;
    }
}
