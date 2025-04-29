package co.edu.uniquindio.poo.bookyourstary.model;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hotel implements Hosting {

    private String name;
    private City city2;
    private String cityName;
    private String description;
    private String imageUrl;
    private double basePrice;
    private int maxGuests;
    private LinkedList<ServiceIncluded> serviceIncludeds;
    private LinkedList<Room> rooms;

    public Hotel(String name, City city2, String description, String imageUrl, double basePrice, int maxGuests,
            LinkedList<ServiceIncluded> serviceIncludeds, LinkedList<Room> rooms) {
        this.name = name;
        this.city2 = city2;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
        this.maxGuests = maxGuests;
        this.serviceIncludeds = serviceIncludeds;
        this.rooms = rooms;
    }

    @Override
    public City getCity() {
        return city2;
    }

    @Override
    public double getPriceForNight() {
        return rooms.stream().mapToDouble(Room::getPriceForNight).min().orElse(0.0);
    }

    public String getCityName() {
        return city2.toString();
    }

}
