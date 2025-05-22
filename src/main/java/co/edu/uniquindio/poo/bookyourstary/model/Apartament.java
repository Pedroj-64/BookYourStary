package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Apartament implements Hosting {

    private String name;
    private City city;  
    private String description;
    private String imageUrl;
    private double pricePerNight;
    private int maxGuests;
    private List<ServiceIncluded> includedServices;
    private double priceForCleaning;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    public Apartament() {
        this.includedServices = new LinkedList<>();
    }

    public Apartament(String name, City city, String description, String imageUrl, 
                     double pricePerNight, int maxGuests, List<ServiceIncluded> includedServices,
                     double priceForCleaning, LocalDate availableFrom, LocalDate availableTo) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.imageUrl = imageUrl;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.includedServices = includedServices != null ? new LinkedList<>(includedServices) : new LinkedList<>();
        this.priceForCleaning = priceForCleaning;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    public static ApartamentBuilder builder() {
        return new ApartamentBuilder();
    }

    public static class ApartamentBuilder {
        private String name;
        private City city;
        private String description;
        private String imageUrl;
        private double pricePerNight;
        private int maxGuests;
        private List<ServiceIncluded> includedServices = new LinkedList<>();
        private double priceForCleaning;
        private LocalDate availableFrom;
        private LocalDate availableTo;

        public ApartamentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ApartamentBuilder city(City city) {
            this.city = city;
            return this;
        }

        public ApartamentBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ApartamentBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ApartamentBuilder pricePerNight(double pricePerNight) {
            this.pricePerNight = pricePerNight;
            return this;
        }

        public ApartamentBuilder maxGuests(int maxGuests) {
            this.maxGuests = maxGuests;
            return this;
        }

        public ApartamentBuilder includedServices(List<ServiceIncluded> includedServices) {
            this.includedServices = includedServices;
            return this;
        }

        public ApartamentBuilder priceForCleaning(double priceForCleaning) {
            this.priceForCleaning = priceForCleaning;
            return this;
        }

        public ApartamentBuilder availableFrom(LocalDate availableFrom) {
            this.availableFrom = availableFrom;
            return this;
        }

        public ApartamentBuilder availableTo(LocalDate availableTo) {
            this.availableTo = availableTo;
            return this;
        }

        public Apartament build() {
            return new Apartament(name, city, description, imageUrl, pricePerNight, maxGuests,
                                includedServices, priceForCleaning, availableFrom, availableTo);
        }
    }

    @Override
    public List<ServiceIncluded> getIncludedServices() {
        return new LinkedList<>(includedServices);
    }

    @Override
    public City getCity() {
        return city;
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

