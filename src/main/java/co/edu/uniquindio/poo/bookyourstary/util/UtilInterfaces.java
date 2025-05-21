package co.edu.uniquindio.poo.bookyourstary.util;

import co.edu.uniquindio.poo.bookyourstary.config.mapping.DataMapping;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import java.util.List;

public class UtilInterfaces {
    /**
     * Devuelve la lista de ciudades disponibles en Colombia.
     */
    public static List<City> getColombianCities() {
        return DataMapping.getColombianCities();
    }

    /**
     * Devuelve la lista de tipos de alojamiento soportados.
     */
    public static List<String> getHostingTypes() {
        return List.of("Apto", "Casa", "Hotel");
    }

    
}
