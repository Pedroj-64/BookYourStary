package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


public interface Hosting {

    String getName();
    City getCity();
    String getDescription();
    String getImageUrl();
    double getPricePerNight();
    int getMaxGuests();
    List<ServiceIncluded> getIncludedServices();
    LocalDate getAvailableFrom();
    LocalDate getAvailableTo();

    void setName(String name);
    void setCity(City city);
    void setDescription(String description);
    void setImageUrl(String imageUrl);
    void setPricePerNight(double pricePerNight);
    void setMaxGuests(int maxGuests);
    void setIncludedServices(List<ServiceIncluded> includedServices);
    void setAvailableFrom(LocalDate availableFrom);
    void setAvailableTo(LocalDate availableTo);

    default String getCityName() {
        return getCity().toString();
    }
}
