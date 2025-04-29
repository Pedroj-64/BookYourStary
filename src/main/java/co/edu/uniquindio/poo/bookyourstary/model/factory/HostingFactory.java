package co.edu.uniquindio.poo.bookyourstary.model.factory;

import java.util.LinkedList;

import co.edu.uniquindio.poo.bookyourstary.model.Apartament;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.Hotel;
import co.edu.uniquindio.poo.bookyourstary.model.House;
import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;

public class HostingFactory {

    public static Hosting createHouse(String name, City city2, String description, String imageUrl,
            double priceForNight, int maxGuests, LinkedList<ServiceIncluded> serviceIncludeds,
            double priceForCleaning) {
        return new House(name, city2, description, imageUrl, priceForNight, maxGuests, serviceIncludeds,
                priceForCleaning);
    }

    public static Hosting createHotel(String name, City city2, String description, String imageUrl, double basePrice,
            int maxGuests, LinkedList<ServiceIncluded> serviceIncludeds, LinkedList<Room> rooms) {
        return new Hotel(name, city2, description, imageUrl, basePrice, maxGuests, serviceIncludeds, rooms);
    }

    public static Hosting createApartament(String name, City city2, String description, String imageUrl,
            double priceForNight, int maxGuests, LinkedList<ServiceIncluded> serviceIncludeds,
            double priceForCleaning) {
        return new Apartament(name, city2, description, imageUrl, priceForNight, maxGuests, serviceIncludeds,
                priceForCleaning);
    }

}
