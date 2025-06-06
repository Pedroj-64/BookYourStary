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
public class House implements Hosting {

    private String name;
    private City city;
    private String description;
    private String imageUrl;
    private double pricePerNight;
    private int maxGuests;
    private List<ServiceIncluded> includedServices;
    private double cleaningFee;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    public House() {
        this.includedServices = new LinkedList<>();
    }

    public House(String name, City city, String description, String imageUrl, double pricePerNight,
            int maxGuests, List<ServiceIncluded> includedServices, double cleaningFee,
            LocalDate availableFrom, LocalDate availableTo) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.imageUrl = imageUrl;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.includedServices = includedServices != null ? new LinkedList<>(includedServices) : new LinkedList<>();
        this.cleaningFee = cleaningFee;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    public static HouseBuilder builder() {
        return new HouseBuilder();
    }

    @Override
    public List<ServiceIncluded> getIncludedServices() {
        return new LinkedList<>(includedServices);
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
