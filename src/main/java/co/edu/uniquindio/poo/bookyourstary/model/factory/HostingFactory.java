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
        return House.builder().name(name).city(city2).description(description).imageUrl(imageUrl)
                .priceForNight(priceForNight).maxGuests(maxGuests).includedServices(serviceIncludeds)
                .cleaningFee(priceForCleaning).build();
    }

    public static Hosting createHotel(String name, City city2, String description, String imageUrl, double basePrice,
            int maxGuests, LinkedList<ServiceIncluded> serviceIncludeds, LinkedList<Room> rooms) {
        return Hotel.builder().name(name).city(city2).description(description).imageUrl(imageUrl)
                .basePrice(basePrice).maxGuests(maxGuests).includedServices(serviceIncludeds).rooms(rooms).build();
    }

    public static Hosting createApartament(String name, City city2, String description, String imageUrl,
            double priceForNight, int maxGuests, LinkedList<ServiceIncluded> serviceIncludeds,
            double priceForCleaning) {
        return Apartament.builder().name(name).city(city2).description(description).imageUrl(imageUrl)
                .priceForNight(priceForNight).maxGuests(maxGuests).includedServices(serviceIncludeds)
                .priceForCleaning(priceForCleaning).build();
    }

}
