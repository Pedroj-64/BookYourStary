package co.edu.uniquindio.poo.bookyourstary.model.strategy;

import java.time.LocalDate;

public class LongStayDiscountStrategy  implements OfferStrategy{

    private int minNights;
    private double discountPercentage;

    public LongStayDiscountStrategy(int minNights, double discountPercentage) {
        this.minNights = minNights;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculateDiscount(double originalPrice, int numberOfNights, LocalDate bookingDate) {
        if (numberOfNights >= minNights) {
            return originalPrice * (1 - discountPercentage / 100);
        }
        return originalPrice;
    }
}
