package co.edu.uniquindio.poo.bookyourstary.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics {

    private int totalBookings;
    private double totalEarnings;
    private double occupancyPercentage;
    private String mostPopularAccommodation;
    private String mostProfitableType;

    public Statistics(int totalBookings, double totalEarnings, double occupancyPercentage,
            String mostPopularAccommodation, String mostProfitableType) {
        this.totalBookings = totalBookings;
        this.totalEarnings = totalEarnings;
        this.occupancyPercentage = occupancyPercentage;
        this.mostPopularAccommodation = mostPopularAccommodation;
        this.mostProfitableType = mostProfitableType;
    }


}
