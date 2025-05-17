package co.edu.uniquindio.poo.bookyourstary.config.mapping;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.CreateClient;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.factory.HostingFactory;

import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataMapping {
    public static Admin createTestAdmin() {
        Admin admin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
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

    public static List<Hosting> getAllHostings() {
        List<Hosting> hostings = new ArrayList<>();

        // Servicios incluidos de ejemplo
        LinkedList<ServiceIncluded> serviciosBasicos = new LinkedList<>();
        serviciosBasicos.add(new ServiceIncluded("1", "Wifi", "Internet inalámbrico disponible"));
        serviciosBasicos.add(new ServiceIncluded("2", "TV", "Televisión por cable incluida"));
        LinkedList<ServiceIncluded> serviciosPremium = new LinkedList<>();
        serviciosPremium.add(new ServiceIncluded("1", "Wifi", "Internet inalámbrico disponible"));
        serviciosPremium.add(new ServiceIncluded("2", "TV", "Televisión por cable incluida"));
        serviciosPremium.add(new ServiceIncluded("3", "Desayuno", "Desayuno incluido todas las mañanas"));

        // Ciudades de ejemplo
        City armenia = new City("Armenia", "Colombia", "Quindio");
        City cali = new City("Cali", "Colombia", "Valle");
        City bogota = new City("Bogotá", "Colombia", "Santander");

        // Habitaciones para hoteles
        LinkedList<Room> roomsHotelSol = new LinkedList<>();
        roomsHotelSol.add(new Room("101", 120000, 2, "url1.jpg", "Habitación doble"));
        roomsHotelSol.add(new Room("102", 150000, 3, "url2.jpg", "Habitación triple"));
        LinkedList<Room> roomsHotelLuna = new LinkedList<>();
        roomsHotelLuna.add(new Room("201", 100000, 2, "url3.jpg", "Habitación doble"));
        roomsHotelLuna.add(new Room("202", 130000, 3, "url4.jpg", "Habitación triple"));
        LinkedList<Room> roomsHotelEstrella = new LinkedList<>();
        roomsHotelEstrella.add(new Room("301", 110000, 2, "url5.jpg", "Habitación doble"));
        roomsHotelEstrella.add(new Room("302", 140000, 3, "url6.jpg", "Habitación triple"));

        // 3 Hoteles
        hostings.add(HostingFactory.createHotel("Hotel Sol", armenia, "Hotel en Armenia", "sol.jpg", 120000, 5, serviciosPremium, roomsHotelSol, LocalDate.now(), LocalDate.now().plusDays(30)));
        hostings.add(HostingFactory.createHotel("Hotel Luna", cali, "Hotel en Cali", "luna.jpg", 100000, 4, serviciosBasicos, roomsHotelLuna,LocalDate.now(), LocalDate.now().plusDays(30)));
        hostings.add(HostingFactory.createHotel("Hotel Estrella", bogota, "Hotel en Bogotá", "estrella.jpg", 110000, 6, serviciosPremium, roomsHotelEstrella,LocalDate.now(), LocalDate.now().plusDays(30)));

        // 3 Casas
        hostings.add(HostingFactory.createHouse("Casa Verde", armenia, "Casa familiar", "verde.jpg", 200000, 4, serviciosBasicos, 30000,LocalDate.now(), LocalDate.now().plusDays(30)));
        hostings.add(HostingFactory.createHouse("Casa Azul", cali, "Casa con piscina", "azul.jpg", 250000, 5, serviciosPremium, 35000,LocalDate.now(), LocalDate.now().plusDays(30)));
        hostings.add(HostingFactory.createHouse("Casa Roja", bogota, "Casa céntrica", "roja.jpg", 180000, 6, serviciosBasicos, 25000,LocalDate.now(), LocalDate.now().plusDays(30)));

        // 3 Apartamentos
        hostings.add(HostingFactory.createApartament("Apto Centro", armenia, "Apto en el centro", "apto1.jpg", 90000, 2, serviciosBasicos, 15000,LocalDate.now(), LocalDate.now().plusDays(30)));
        hostings.add(HostingFactory.createApartament("Apto Norte", cali, "Apto moderno", "apto2.jpg", 110000, 3, serviciosPremium, 20000,LocalDate.now(), LocalDate.now().plusDays(30)));
        hostings.add(HostingFactory.createApartament("Apto Sur", bogota, "Apto económico", "apto3.jpg", 80000, 1, serviciosBasicos, 10000,LocalDate.now(), LocalDate.now().plusDays(30)));

        return hostings;
    }
}
