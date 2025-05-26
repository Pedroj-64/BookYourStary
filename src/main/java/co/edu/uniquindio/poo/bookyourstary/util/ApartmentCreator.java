package co.edu.uniquindio.poo.bookyourstary.util;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Apartament;
import co.edu.uniquindio.poo.bookyourstary.model.City;
import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase utilitaria para crear apartamentos y evitar problemas de compatibilidad
 * con DataMapping
 */
public class ApartmentCreator {

    /**
     * Crea y guarda apartamentos directamente en el repositorio
     * Este método se ofrece como alternativa por si hay problemas con la interfaz
     * estándar
     */
    public static void createApartments() {
        try {
            System.out.println("Creando apartamentos utilizando método independiente...");

            // Verificamos primero si ya existen apartamentos en el sistema
            List<Apartament> existingApartaments = MainController.getInstance().getHostingService()
                    .getApartamentRepository().findAll();
            if (existingApartaments != null && !existingApartaments.isEmpty()) {
                System.out.println("Ya existen " + existingApartaments.size()
                        + " apartamentos en el sistema. No se crearán nuevos.");
                return;
            }

            // Ciudades
            City armenia = new City("Armenia", "Colombia", "Quindio");
            City cali = new City("Cali", "Colombia", "Valle");
            City bogota = new City("Bogotá", "Colombia", "Cundinamarca");

            // Servicios incluidos
            LinkedList<ServiceIncluded> serviciosBasicos = new LinkedList<>();
            serviciosBasicos.add(new ServiceIncluded("1", "Wifi", "Internet inalámbrico disponible"));
            serviciosBasicos.add(new ServiceIncluded("2", "TV", "Televisión por cable incluida"));

            LinkedList<ServiceIncluded> serviciosPremium = new LinkedList<>();
            serviciosPremium.add(new ServiceIncluded("1", "Wifi", "Internet inalámbrico disponible"));
            serviciosPremium.add(new ServiceIncluded("2", "TV", "Televisión por cable incluida"));
            serviciosPremium.add(new ServiceIncluded("3", "Desayuno", "Desayuno incluido todas las mañanas"));

            // Apartamentos
            Apartament apto1 = new Apartament(
                    "Apto Centro", armenia, "Apto en el centro", "FotoHotelRelleno.png",
                    90000.0, 2, new LinkedList<>(serviciosBasicos), 15000.0,
                    LocalDate.now(), LocalDate.now().plusDays(30));

            Apartament apto2 = new Apartament(
                    "Apto Norte", cali, "Apto moderno", "FotoHotelRelleno.png",
                    110000.0, 3, new LinkedList<>(serviciosPremium), 18000.0,
                    LocalDate.now(), LocalDate.now().plusDays(30));

            Apartament apto3 = new Apartament(
                    "Apto Sur", bogota, "Apto económico", "FotoHotelRelleno.png",
                    80000.0, 1, new LinkedList<>(serviciosBasicos), 12000.0,
                    LocalDate.now(), LocalDate.now().plusDays(30));

            // Guardar en el repositorio específico
            MainController.getInstance().getHostingService().getApartamentRepository().save(apto1);
            MainController.getInstance().getHostingService().getApartamentRepository().save(apto2);
            MainController.getInstance().getHostingService().getApartamentRepository().save(apto3);

            System.out.println("✓ Apartamentos creados correctamente: 3");
        } catch (Exception e) {
            System.err.println("✗ Error al crear apartamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
