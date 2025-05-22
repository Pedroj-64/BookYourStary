package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public static class HotelBuilder {
        private String name;
        private City city;
        private String description;
        private String imageUrl;
        private double basePrice;
        private int maxGuests;
        private List<ServiceIncluded> includedServices = new LinkedList<>();
        private List<Room> rooms = new LinkedList<>();
        private LocalDate availableFrom;
        private LocalDate availableTo;

        public HotelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HotelBuilder city(City city) {
            this.city = city;
            return this;
        }

        public HotelBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HotelBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public HotelBuilder basePrice(double basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public HotelBuilder maxGuests(int maxGuests) {
            this.maxGuests = maxGuests;
            return this;
        }

        public HotelBuilder includedServices(List<ServiceIncluded> includedServices) {
            this.includedServices = includedServices;
            return this;
        }

        public HotelBuilder rooms(List<Room> rooms) {
            this.rooms = rooms;
            return this;
        }

        public HotelBuilder availableFrom(LocalDate availableFrom) {
            this.availableFrom = availableFrom;
            return this;
        }

        public HotelBuilder availableTo(LocalDate availableTo) {
            this.availableTo = availableTo;
            return this;
        }

        public Hotel build() {
            return new Hotel(name, city, description, imageUrl, basePrice, maxGuests, 
                           includedServices, rooms, availableFrom, availableTo);
        }
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
