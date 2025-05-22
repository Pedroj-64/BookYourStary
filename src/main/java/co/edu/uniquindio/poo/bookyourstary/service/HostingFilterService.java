package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.repository.HostingRepository;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HostingFilterService {
    private static HostingFilterService instance;

    public static HostingFilterService getInstance() {
        if (instance == null) {
            instance = new HostingFilterService();
        }
        return instance;
    }    public List<Hosting> filterHostings(String ciudad, String tipoDeAlojamiento, Double minPrice, Double maxPrice,
            LocalDate minDate, LocalDate maxDate, Integer numHuespedes,
            Boolean wifi, Boolean piscina, Boolean desayuno, LocalDate fechaInicio, LocalDate fechaFin) {
        // Depuración: imprimir los criterios de filtrado
        System.out.println("Filtrando con los siguientes criterios:");
        System.out.println("- Ciudad: " + (ciudad != null ? ciudad : "No especificada"));
        System.out.println("- Tipo: " + (tipoDeAlojamiento != null ? tipoDeAlojamiento : "No especificado"));
        System.out.println("- Precio min: " + (minPrice != null ? minPrice : "No especificado"));
        System.out.println("- Precio max: " + (maxPrice != null ? maxPrice : "No especificado"));
        System.out.println("- Número de huéspedes: " + (numHuespedes != null ? numHuespedes : "No especificado"));
        System.out.println("- Wifi: " + (wifi != null ? "Sí" : "No especificado"));
        System.out.println("- Piscina: " + (piscina != null ? "Sí" : "No especificado"));
        System.out.println("- Desayuno: " + (desayuno != null ? "Sí" : "No especificado"));
        
        // Obtener todos los alojamientos antes de filtrar
        List<Hosting> allHostings = MainController.getInstance().getHostingService().findAllHostings();
        System.out.println("Total de alojamientos antes del filtrado: " + allHostings.size());
          // Usar el método que obtiene todos los alojamientos de todos los repositorios
        List<Hosting> results = allHostings.stream()
                .filter(h -> {
                    if (ciudad == null) return true;
                    try {
                        // Verificar tanto el nombre de la ciudad como su representación en cadena
                        return (h.getCity() != null && 
                               (h.getCity().getName().equalsIgnoreCase(ciudad) || 
                                h.getCity().toString().equalsIgnoreCase(ciudad) || 
                                h.getCityName().equalsIgnoreCase(ciudad)));
                    } catch (Exception e) {
                        System.err.println("Error al comparar ciudades: " + e.getMessage());
                        return false;
                    }
                })

                .filter(h -> tipoDeAlojamiento == null
                        || h.getClass().getSimpleName().equalsIgnoreCase(tipoDeAlojamiento))
                .filter(h -> minPrice == null || h.getPricePerNight() >= minPrice)
                .filter(h -> maxPrice == null || h.getPricePerNight() <= maxPrice)
                .filter(h -> numHuespedes == null || h.getMaxGuests() >= numHuespedes)                
                .filter(h -> wifi == null
                        || (h.getIncludedServices() != null && h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("wifi"))))
                .filter(h -> piscina == null
                        || (h.getIncludedServices() != null && h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("piscina"))))
                .filter(h -> desayuno == null
                        || (h.getIncludedServices() != null && h.getIncludedServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase("desayuno"))))                .filter(h -> {
                    if (fechaInicio == null || fechaFin == null)
                        return true;
                    
                    // Verificar que la fecha del alojamiento esté disponible
                    if (h.getAvailableFrom() == null || h.getAvailableTo() == null)
                        return true;
                        
                    // Comprobar si hay superposición de periodos de tiempo
                    // El alojamiento está disponible si su periodo se superpone con el periodo solicitado
                    return !fechaInicio.isAfter(h.getAvailableTo()) && 
                           !fechaFin.isBefore(h.getAvailableFrom());
                })
                .collect(Collectors.toList());
                
        // Llamar al método de registro y devolver los resultados
        return logFilterResults(results);
    }
    
    private List<Hosting> logFilterResults(List<Hosting> results) {
        System.out.println("Total de alojamientos después del filtrado: " + results.size());
        
        if (results.isEmpty()) {
            System.out.println("No se encontraron alojamientos con los criterios especificados");
        } else {
            System.out.println("Alojamientos encontrados:");
            for (Hosting h : results) {
                System.out.println("- " + h.getName() + " (" + h.getClass().getSimpleName() + ") en " + h.getCityName() + 
                                  ", precio: " + h.getPricePerNight());
            }
        }
        
        return results;
    }
}
