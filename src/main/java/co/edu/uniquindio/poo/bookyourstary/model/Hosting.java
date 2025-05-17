package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;
import java.util.LinkedList;

import co.edu.uniquindio.poo.bookyourstary.model.enums.HostingType;

public interface Hosting {

    String getName();
    City getCity();
    String getDescription();
    String getImageUrl();
    double getPricePerNight();
    int getMaxGuests();
    LinkedList<ServiceIncluded> getIncludedServices(); 
    HostingType getHostingType();
    LocalDate getAvailableFrom();
    LocalDate getAvailableTo();

    default String getCityName() {
        return getCity().toString();
    }
}
