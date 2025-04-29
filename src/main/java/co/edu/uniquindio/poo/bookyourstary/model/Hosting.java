package co.edu.uniquindio.poo.bookyourstary.model;

import java.util.LinkedList;

public interface Hosting {

    String getName();
    City getCity();
    String getDescription();
    String getImageUrl();
    double getPriceForNight();
    int getMaxGuests();
    LinkedList<ServiceIncluded> getServiceIncludeds();

}
