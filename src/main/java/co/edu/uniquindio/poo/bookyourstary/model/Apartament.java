package co.edu.uniquindio.poo.bookyourstary.model;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Apartament implements Hosting {

    private String name;
    private City city2;
    private String description;
    private String imageUrl;
    private double priceForNight;
    private int maxGuests;
    private LinkedList<ServiceIncluded> serviceIncludeds;
    private double priceForCleaning;

    public Apartament(String name, City city2, String description, String imageUrl, double priceForNight, int maxGuests,
            LinkedList<ServiceIncluded> serviceIncludeds, double priceForCleaning) {
        this.name = name;
        this.city2 = city2;
        this.description = description;
        this.imageUrl = imageUrl;
        this.priceForNight = priceForNight;
        this.maxGuests = maxGuests;
        this.serviceIncludeds = serviceIncludeds;
        this.priceForCleaning = priceForCleaning;
    }

    @Override
    public City getCity(){
        return city2;
    }

    public String getCityName(){
        return city2.toString();
    }

    

}
