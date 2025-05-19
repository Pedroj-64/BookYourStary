package co.edu.uniquindio.poo.bookyourstary.config.mapping;

import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;
import java.util.HashMap;
import java.util.Map;

public class ServicesMapping {
    private static ServicesMapping instance;
    private final Map<String, ServiceIncluded> services;

    private ServicesMapping() {
        services = new HashMap<>();
        // Crear los servicios incluidos actuales con id, name y description
        services.put("wifi", new ServiceIncluded("1", "wifi", "Wi-Fi gratis en todo el alojamiento"));
        services.put("piscina", new ServiceIncluded("2", "piscina", "Piscina disponible para huéspedes"));
        services.put("desayuno", new ServiceIncluded("3", "desayuno", "Desayuno incluido"));
    }

    public static ServicesMapping getInstance() {
        if (instance == null) {
            instance = new ServicesMapping();
        }
        return instance;
    }

    public ServiceIncluded getServiceByName(String name) {
        return services.get(name.toLowerCase());
    }

    public Map<String, ServiceIncluded> getAllServices() {
        return services;
    }
}
