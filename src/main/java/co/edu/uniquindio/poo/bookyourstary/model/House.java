package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public static class HouseBuilder {
        private String name;
        private City city;
        private String description;
        private String imageUrl;
        private double pricePerNight;
        private int maxGuests;
        private List<ServiceIncluded> includedServices = new LinkedList<>();
        private double cleaningFee;
        private LocalDate availableFrom;
        private LocalDate availableTo;

        public HouseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HouseBuilder city(City city) {
            this.city = city;
            return this;
        }

        public HouseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HouseBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public HouseBuilder pricePerNight(double pricePerNight) {
            this.pricePerNight = pricePerNight;
            return this;
        }

        public HouseBuilder maxGuests(int maxGuests) {
            this.maxGuests = maxGuests;
            return this;
        }

        public HouseBuilder includedServices(List<ServiceIncluded> includedServices) {
            this.includedServices = includedServices;
            return this;
        }

        public HouseBuilder cleaningFee(double cleaningFee) {
            this.cleaningFee = cleaningFee;
            return this;
        }

        public HouseBuilder availableFrom(LocalDate availableFrom) {
            this.availableFrom = availableFrom;
            return this;
        }

        public HouseBuilder availableTo(LocalDate availableTo) {
            this.availableTo = availableTo;
            return this;
        }

        public House build() {
            return new House(name, city, description, imageUrl, pricePerNight, maxGuests,
                           includedServices, cleaningFee, availableFrom, availableTo);
        }
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
