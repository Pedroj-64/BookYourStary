package co.edu.uniquindio.poo.bookyourstary.model.factory;

import java.time.LocalDate;
import java.util.List;

import co.edu.uniquindio.poo.bookyourstary.model.Apartament;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.Hotel;
import co.edu.uniquindio.poo.bookyourstary.model.House;
import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;

public class HostingFactory {    public static Hosting createHouse(String name, City city2, String description, String imageUrl,
            double priceForNight, int maxGuests, List<ServiceIncluded> serviceIncludeds,
            double priceForCleaning, LocalDate availableFrom, LocalDate availableTo) {
        House house = House.builder().name(name).city(city2).description(description).imageUrl(imageUrl)
                .pricePerNight(priceForNight).maxGuests(maxGuests).includedServices(serviceIncludeds)
                .cleaningFee(priceForCleaning)
                .availableFrom(availableFrom).availableTo(availableTo)
                .build();
        // Ya no es necesario guardar la casa aquí porque se guardará en HostingService.saveHosting
        return house;
    }    public static Hosting createHotel(String name, City city2, String description, String imageUrl, double basePrice,
            int maxGuests, List<ServiceIncluded> serviceIncludeds, List<Room> rooms,
            LocalDate availableFrom, LocalDate availableTo) {
        Hotel hotel = Hotel.builder().name(name).city(city2).description(description).imageUrl(imageUrl)
                .basePrice(basePrice).maxGuests(maxGuests).includedServices(serviceIncludeds).rooms(rooms)
                .availableFrom(availableFrom).availableTo(availableTo)
                .build();
        // Ya no es necesario guardar el hotel aquí porque se guardará en HostingService.saveHosting
        return hotel;
    }public static Hosting createApartament(String name, City city2, String description, String imageUrl,
            double priceForNight, int maxGuests, List<ServiceIncluded> serviceIncludeds,
            double priceForCleaning, LocalDate availableFrom, LocalDate availableTo) {
        Apartament apartament = Apartament.builder().name(name).city(city2).description(description).imageUrl(imageUrl)
                .pricePerNight(priceForNight).maxGuests(maxGuests).includedServices(serviceIncludeds)
                .priceForCleaning(priceForCleaning)
                .availableFrom(availableFrom).availableTo(availableTo)
                .build();
        // Ya no es necesario guardar el apartamento aquí porque se guardará en HostingService.saveHosting
        // HostingRepository.getInstance().save(apartament);
        return apartament;
    }

}
