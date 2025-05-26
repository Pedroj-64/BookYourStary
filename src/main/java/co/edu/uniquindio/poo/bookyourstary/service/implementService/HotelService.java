package co.edu.uniquindio.poo.bookyourstary.service.implementService;

import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.Hotel;
import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;
import co.edu.uniquindio.poo.bookyourstary.repository.HotelRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HotelService {

    private final HotelRepository hotelRepository;
    private static HotelService instance;

    public static HotelService getInstance() {
        if (instance == null) {
            instance = new HotelService(HotelRepository.getInstance());
        }
        return instance;
    }

    private HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void saveHotel(String name, City city, String description, String imageUrl, double basePrice,
                          int maxGuests, LinkedList<ServiceIncluded> includedServices, LinkedList<Room> rooms,LocalDate availableFrom, LocalDate availableTo) {

        Hosting hotel =  HostingFactory.createHotel(name, city, description, imageUrl, basePrice,
                maxGuests, includedServices, rooms, availableFrom, availableTo);
        

        hotelRepository.save((Hotel)hotel);
    }

    public Optional<Hotel> findHotelById(String name) {
        return hotelRepository.findById(name);
    }

    public List<Hotel> findAllHotels() {
        return hotelRepository.findAll();
    }

    public void deleteHotel(String name) {
        Hotel hotel = findHotelById(name)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el hotel con ese nombre"));
        hotelRepository.delete(hotel);
    }

    public double calculateTotalPrice(Hotel hotel, int numberOfNights) {
        return hotel.getPricePerNight() * numberOfNights;
    }

    public List<Hotel> findAllAvailableHotels() {
        return hotelRepository.findAll().stream()
                .filter(h -> h.getAvailableTo().isAfter(LocalDate.now()))
                .toList();
    }
}
