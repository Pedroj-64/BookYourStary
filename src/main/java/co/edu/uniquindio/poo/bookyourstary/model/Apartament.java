package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.LinkedList;

import co.edu.uniquindio.poo.bookyourstary.model.enums.HostingType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Apartament implements Hosting {

    private String name;
    private City city;  
    private String description;
    private String imageUrl;
    private double pricePerNight;
    private int maxGuests;
    private LinkedList<ServiceIncluded> includedServices;  
    private double priceForCleaning;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    public Apartament(String name, City city, String description, String imageUrl, double pricePerNight, int maxGuests,
                      LinkedList<ServiceIncluded> includedServices, double priceForCleaning,
                      LocalDate availableFrom, LocalDate availableTo) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.imageUrl = imageUrl;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.includedServices = includedServices;
        this.priceForCleaning = priceForCleaning;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    @Override
    public City getCity() {
        return city;
    }

    @Override
    public HostingType getHostingType() {
        return HostingType.APARTAMENT;
    }

    @Override
    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    @Override
    public LocalDate getAvailableTo() {
        return availableTo;
    }

}

