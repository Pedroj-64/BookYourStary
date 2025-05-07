package co.edu.uniquindio.poo.bookyourstary.model;

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

    public Apartament(String name, City city, String description, String imageUrl, double pricePerNight, int maxGuests,
                      LinkedList<ServiceIncluded> includedServices, double priceForCleaning) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.imageUrl = imageUrl;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.includedServices = includedServices;
        this.priceForCleaning = priceForCleaning;
    }

    @Override
    public City getCity() {
        return city;
    }

    @Override
    public HostingType getHostingType() {
        return HostingType.APARTAMENT;
    }

}

