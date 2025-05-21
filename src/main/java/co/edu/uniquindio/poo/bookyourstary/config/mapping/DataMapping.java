package co.edu.uniquindio.poo.bookyourstary.config.mapping;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.CreateClient;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.service.CityService;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataMapping {
    public static Admin createTestAdmin() {
        String hashedPassword = PasswordUtil.hashPassword("adminpass");
        Admin admin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", hashedPassword);
        MainController.getInstance().getAdminRepository().saveAdmin(admin);
        return admin;
    }

    public static Client createTestClient() {
        Client client = CreateClient.createFullClient(
            "10", "TestUser", "3001234567", "usuario@prueba.com", "userpass"
        );
        client.setActive(true);
        return client;
    }

    public static void createAllHostings() {
        // Servicios incluidos de ejemplo
        LinkedList<ServiceIncluded> serviciosBasicos = new LinkedList<>();
        serviciosBasicos.add(new ServiceIncluded("1", "Wifi", "Internet inalámbrico disponible"));
        serviciosBasicos.add(new ServiceIncluded("2", "TV", "Televisión por cable incluida"));
        List<ServiceIncluded> serviciosPremium = new LinkedList<>();
        serviciosPremium.add(new ServiceIncluded("1", "Wifi", "Internet inalámbrico disponible"));
        serviciosPremium.add(new ServiceIncluded("2", "TV", "Televisión por cable incluida"));
        serviciosPremium.add(new ServiceIncluded("3", "Desayuno", "Desayuno incluido todas las mañanas"));

        // Ciudades de ejemplo
        // Get the CityService instance
        CityService cityService = MainController.getInstance().getCityService();

        // Create and save cities defined directly and from getColombianCities()
        // Using a temporary list to manage cities to be added to hostings
        List<City> hostingCities = new ArrayList<>();

        City armenia = new City("Armenia", "Colombia", "Quindio");
        cityService.saveCity(armenia.getName(), armenia.getCountry(), armenia.getDepartament());
        hostingCities.add(cityService.findCityById("Armenia").orElseThrow(() -> new RuntimeException("Armenia not found after saving")));
        
        City cali = new City("Cali", "Colombia", "Valle del Cauca"); // Corrected department based on getColombianCities
        cityService.saveCity(cali.getName(), cali.getCountry(), cali.getDepartament());
        hostingCities.add(cityService.findCityById("Cali").orElseThrow(() -> new RuntimeException("Cali not found after saving")));

        City bogota = new City("Bogotá", "Colombia", "Cundinamarca"); // Corrected department
        cityService.saveCity(bogota.getName(), bogota.getCountry(), bogota.getDepartament());
        hostingCities.add(cityService.findCityById("Bogotá").orElseThrow(() -> new RuntimeException("Bogotá not found after saving")));
        
        // Save other cities from getColombianCities if not already added
        List<City> allDefinedCities = getColombianCities();
        for (City cityDef : allDefinedCities) {
            if (cityService.findCityById(cityDef.getName()).isEmpty()) {
                cityService.saveCity(cityDef.getName(), cityDef.getCountry(), cityDef.getDepartament());
            }
            // Ensure hostingCities contains all unique cities needed for hostings if they differ
            // For simplicity, the hostings below use armenia, cali, bogota directly.
            // If hostings were to be created for Medellin, Cartagena etc., they should be added to hostingCities
        }
        
        // Retrieve the managed instances for hostings
        City managedArmenia = hostingCities.stream().filter(c -> c.getName().equals("Armenia")).findFirst().orElseThrow();
        City managedCali = hostingCities.stream().filter(c -> c.getName().equals("Cali")).findFirst().orElseThrow();
        City managedBogota = hostingCities.stream().filter(c -> c.getName().equals("Bogotá")).findFirst().orElseThrow();


        // Habitaciones para hoteles
        LinkedList<Room> roomsHotelSol = new LinkedList<>();
        roomsHotelSol.add(new Room("101", 120000, 2, "FotoHotelRelleno.png", "Habitación doble"));
        roomsHotelSol.add(new Room("102", 150000, 3, "FotoHotelRelleno.png", "Habitación triple"));
        LinkedList<Room> roomsHotelLuna = new LinkedList<>();
        roomsHotelLuna.add(new Room("201", 100000, 2, "FotoHotelRelleno.png", "Habitación doble"));
        roomsHotelLuna.add(new Room("202", 130000, 3, "FotoHotelRelleno.png", "Habitación triple"));
        LinkedList<Room> roomsHotelEstrella = new LinkedList<>();
        roomsHotelEstrella.add(new Room("301", 110000, 2, "FotoHotelRelleno.png", "Habitación doble"));
        roomsHotelEstrella.add(new Room("302", 140000, 3, "FotoHotelRelleno.png", "Habitación triple"));

        // 3 Hoteles
        MainController.getInstance().getHostingService().createHotel("Hotel Sol", managedArmenia, "Hotel en Armenia", "sol.jpg", 120000, 5, serviciosPremium, roomsHotelSol, LocalDate.now(), LocalDate.now().plusDays(30));
        MainController.getInstance().getHostingService().createHotel("Hotel Luna", managedCali, "Hotel en Cali", "FotoHotelRelleno.png", 100000, 4, serviciosBasicos, roomsHotelLuna, LocalDate.now(), LocalDate.now().plusDays(30));
        MainController.getInstance().getHostingService().createHotel("Hotel Estrella", managedBogota, "Hotel en Bogotá", "FotoHotelRelleno.png", 110000, 6, serviciosPremium, roomsHotelEstrella, LocalDate.now(), LocalDate.now().plusDays(30));

        // 3 Casas
        MainController.getInstance().getHostingService().createHouse("Casa Verde", managedArmenia, "Casa familiar", "FotoHotelRelleno.png", 200000, 4, serviciosBasicos, 30000, LocalDate.now(), LocalDate.now().plusDays(30));
        MainController.getInstance().getHostingService().createHouse("Casa Azul", managedCali, "Casa con piscina", "FotoHotelRelleno.png", 250000, 5, serviciosPremium, 35000, LocalDate.now(), LocalDate.now().plusDays(30));
        MainController.getInstance().getHostingService().createHouse("Casa Roja", managedBogota, "Casa céntrica", "FotoHotelRelleno.png", 180000, 6, serviciosBasicos, 25000, LocalDate.now(), LocalDate.now().plusDays(30));

        // 3 Apartamentos
        MainController.getInstance().getHostingService().createApartament("Apto Centro", managedArmenia, "Apto en el centro", "FotoHotelRelleno.png", 90000, 2, serviciosBasicos, LocalDate.now(), LocalDate.now().plusDays(30));
        MainController.getInstance().getHostingService().createApartament("Apto Norte", managedCali, "Apto moderno", "FotoHotelRelleno.png", 110000, 3, serviciosPremium, LocalDate.now(), LocalDate.now().plusDays(30));
        MainController.getInstance().getHostingService().createApartament("Apto Sur", managedBogota, "Apto económico", "FotoHotelRelleno.png", 80000, 1, serviciosBasicos, LocalDate.now(), LocalDate.now().plusDays(30));
    }

    public static List<City> getColombianCities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City("Armenia", "Colombia", "Quindío"));
        cities.add(new City("Cali", "Colombia", "Valle del Cauca"));
        cities.add(new City("Bogotá", "Colombia", "Cundinamarca"));
        cities.add(new City("Medellín", "Colombia", "Antioquia"));
        cities.add(new City("Cartagena", "Colombia", "Bolívar"));
        cities.add(new City("Barranquilla", "Colombia", "Atlántico"));
        return cities;
    }
}
