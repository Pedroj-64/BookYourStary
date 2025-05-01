package co.edu.uniquindio.poo.bookyourstary.model;

import java.util.LinkedList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Hotel implements Hosting {

    private String name;
    private City city;  
    private String description;
    private String imageUrl;
    private double basePrice;
    private int maxGuests;
    private LinkedList<ServiceIncluded> includedServices;  
    private LinkedList<Room> rooms;

    public Hotel(String name, City city, String description, String imageUrl, double basePrice, int maxGuests,
                 LinkedList<ServiceIncluded> includedServices, LinkedList<Room> rooms) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
        this.maxGuests = maxGuests;
        this.includedServices = includedServices;
        this.rooms = rooms;
    }

    @Override
    public City getCity() {
        return city;
    }

 
    @Override
    public double getPricePerNight() {
        return rooms.stream().mapToDouble(Room::getPriceForNight).min().orElse(0.0);  // Precio mínimo de la habitación
    }

 
}
