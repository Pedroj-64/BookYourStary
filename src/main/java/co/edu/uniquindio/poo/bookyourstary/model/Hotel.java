package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

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
    private List<ServiceIncluded> includedServices;
    private List<Room> rooms;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    public Hotel() {
        this.includedServices = new LinkedList<>();
        this.rooms = new LinkedList<>();
    }

    public Hotel(String name, City city, String description, String imageUrl,
            double basePrice, int maxGuests, List<ServiceIncluded> includedServices,
            List<Room> rooms, LocalDate availableFrom, LocalDate availableTo) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
        this.maxGuests = maxGuests;
        this.includedServices = includedServices != null ? new LinkedList<>(includedServices) : new LinkedList<>();
        this.rooms = rooms != null ? new LinkedList<>(rooms) : new LinkedList<>();
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    public static HotelBuilder builder() {
        return new HotelBuilder();
    }

    @Override
    public City getCity() {
        return city;
    }

    @Override
    public double getPricePerNight() {
        return rooms.stream().mapToDouble(Room::getPriceForNight).min().orElse(0.0);
    }

    @Override
    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    @Override
    public LocalDate getAvailableTo() {
        return availableTo;
    }

    @Override
    public void setPricePerNight(double pricePerNight) {
        this.basePrice = pricePerNight;
    }

    @Override
    public List<ServiceIncluded> getIncludedServices() {
        return new LinkedList<>(includedServices);
    }

    public List<Room> getRooms() {
        return new LinkedList<>(rooms);
    }
}
