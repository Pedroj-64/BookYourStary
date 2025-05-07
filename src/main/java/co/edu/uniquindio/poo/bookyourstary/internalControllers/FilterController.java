package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.enums.HostingType;
import co.edu.uniquindio.poo.bookyourstary.service.HostingService;

import java.util.List;

public class FilterController {

    private final HostingService hostingService;

    public FilterController(HostingService hostingService) {
        this.hostingService = hostingService;
    }

    public List<Hosting> filterByCity(City city) {
        return hostingService.findHostingsByCity(city);
    }

    public List<Hosting> filterByPriceRange(double minPrice, double maxPrice) {
        return hostingService.findAllHostings().stream()
                .filter(hosting -> hosting.getPricePerNight() >= minPrice && hosting.getPricePerNight() <= maxPrice)
                .toList();
    }

    public List<Hosting> filterByMaxGuests(int maxGuests) {
        return hostingService.findAllHostings().stream()
                .filter(hosting -> hosting.getMaxGuests() >= maxGuests)
                .toList();
    }

    public List<Hosting> filterByType(HostingType type) {
        return hostingService.findAllHostings().stream()
                .filter(hosting -> hosting.getHostingType() == type)
                .toList();
    }

    public List<Hosting> filterHostings(City city, Double minPrice, Double maxPrice, Integer maxGuests,
            HostingType type) {
        return hostingService.findAllHostings().stream()
                .filter(hosting -> city == null || hosting.getCity().equals(city))
                .filter(hosting -> minPrice == null || hosting.getPricePerNight() >= minPrice)
                .filter(hosting -> maxPrice == null || hosting.getPricePerNight() <= maxPrice)
                .filter(hosting -> maxGuests == null || hosting.getMaxGuests() >= maxGuests)
                .filter(hosting -> type == null || hosting.getHostingType() == type)
                .toList();
    }

}
