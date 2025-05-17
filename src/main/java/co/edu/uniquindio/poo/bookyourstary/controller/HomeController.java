package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.repository.HostingRepository;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.time.LocalDate;
import co.edu.uniquindio.poo.bookyourstary.service.HostingFilterService;

public class HomeController {

    private final HostingFilterService hostingFilterService = new HostingFilterService();

    /**
     * Devuelve una lista aleatoria de 5 alojamientos del repositorio
     */
    public List<Hosting> getRandomHostings() {
        List<Hosting> allHostings = HostingRepository.getInstance().findAll();
        Collections.shuffle(allHostings, new Random());
        return allHostings.stream().limit(5).collect(Collectors.toList());
    }

    /**
     * Consejo sobre el filtro: Si el filtro es complejo y se reutiliza en varias partes, es mejor una clase exclusiva (por ejemplo, HostingFilterService). Si solo se usa aquí, puedes implementarlo como método aquí.
     * 
     * Ejemplo de firma del método de filtro:
     */
    public List<Hosting> filterHostings(String ciudad, String tipoDeAlojamiento, Double minPrice, Double maxPrice,
                                        LocalDate minDate, LocalDate maxDate, Integer numHuespedes,
                                        Boolean wifi, Boolean piscina, Boolean desayuno) {
        return hostingFilterService.filterHostings(ciudad, tipoDeAlojamiento, minPrice, maxPrice, minDate, maxDate, numHuespedes, wifi, piscina, desayuno);
    }

    // Consejo: Si la lógica de filtro crece o se reutiliza, crea una clase HostingFilterService para mantener el código limpio y reutilizable

}
