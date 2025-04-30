package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;

import co.edu.uniquindio.poo.bookyourstary.model.strategy.OfferStrategy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Offer {

    private String name;
    private String description;
    private OfferStrategy strategy;
    private LocalDate startDate;
    private LocalDate endDate;

    public Offer(String name, String description, OfferStrategy strategy, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.strategy = strategy;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
