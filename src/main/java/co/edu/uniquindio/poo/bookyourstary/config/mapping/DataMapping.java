package co.edu.uniquindio.poo.bookyourstary.config.mapping;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.CityService;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;

import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataMapping {
        public static Admin createTestAdmin() {
                // Primero verificamos si ya existe un administrador
                Admin existingAdmin = null;
                try {
                        existingAdmin = MainController.getInstance().getAdminService().getAdmin();
                } catch (Exception e) {
                        System.out.println("No se encontró administrador existente: " + e.getMessage());
                }

                if (existingAdmin != null) {
                        // Si existe, nos aseguramos de que esté activado
                        existingAdmin.setActive(true);
                        System.out.println("Administrador existente actualizado: " + existingAdmin.getName() + " ("
                                        + existingAdmin.getEmail() + ")");
                        return existingAdmin;
                } else {
                        // Si no existe, creamos uno nuevo
                        Admin admin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
                        admin.setActive(true); // Asegurarse de que el administrador esté activado
                        MainController.getInstance().getAdminRepository().saveAdmin(admin);
                        System.out.println(
                                        "NUEVO Admin creado y guardado con ID: " + admin.getId() + " y email: "
                                                        + admin.getEmail());
                        return admin;
                }
        }

        public static Client createTestClient() {
                try {
                        // Try to find if the client already exists
                        Optional<Client> existingClient = MainController.getInstance().getClientRepository()
                                        .findByEmail("usuario@prueba.com");

                        if (existingClient.isPresent()) {
                                Client client = existingClient.get();
                                client.setActive(true); // Make sure it's active
                                // Update the password in case it was changed
                                client.setPassword(PasswordUtil.hashPassword("userpass"));
                                // Save the updated client back to the repository
                                MainController.getInstance().getClientRepository().save(client);
                                System.out.println("Cliente de prueba existente actualizado: " + client.getName() +
                                                " (" + client.getEmail() + ")");
                                return client;
                        } else {
                                // Create a new client directly, don't use CreateClient class to avoid duplicate
                                // saves
                                Client client = new Client("10", "TestUser", "3001234567", "usuario@prueba.com",
                                                PasswordUtil.hashPassword("userpass"));
                                client.setActive(true); // Make sure it's active

                                // Create a wallet for the client
                                VirtualWallet wallet = MainController.getInstance().getVirtualWalletService()
                                                .createWalletForClient(client);
                                client.setVirtualWallet(wallet);

                                // Save the client to repository
                                MainController.getInstance().getClientRepository().save(client);
                                System.out.println("Cliente de prueba nuevo creado y guardado: " + client.getName() +
                                                " (" + client.getEmail() + ")");
                                return client;
                        }
                } catch (Exception e) {
                        System.err.println("Error al crear cliente de prueba: " + e.getMessage());
                        e.printStackTrace();
                        return null;
                }
        }

        public static void createAllHostings() {
                try {
                        System.out.println("Iniciando creación de hostings de prueba...");

                        // Servicios incluidos de ejemplo
                        LinkedList<ServiceIncluded> serviciosBasicos = new LinkedList<>();
                        serviciosBasicos.add(new ServiceIncluded("1", "Wifi", "Internet inalámbrico disponible"));
                        serviciosBasicos.add(new ServiceIncluded("2", "TV", "Televisión por cable incluida"));

                        LinkedList<ServiceIncluded> serviciosPremium = new LinkedList<>();
                        serviciosPremium.addAll(serviciosBasicos);
                        serviciosPremium.add(new ServiceIncluded("3", "Piscina", "Acceso a piscina"));
                        serviciosPremium.add(new ServiceIncluded("4", "Gimnasio", "Acceso a gimnasio"));
                        // Crear y guardar las ciudades primero
                        CityService cityService = CityService.getInstance();
                        cityService.clearAll();

                        // Crear las ciudades y guardar las referencias
                        City armenia = cityService.saveCity("Armenia", "Colombia", "Quindio");
                        City cali = cityService.saveCity("Cali", "Colombia", "Valle");
                        City bogota = cityService.saveCity("Bogotá", "Colombia", "Cundinamarca");
                        City medellin = cityService.saveCity("Medellín", "Colombia", "Antioquia");

                        System.out.println("Ciudades creadas y guardadas con éxito");

                        // Crear habitaciones para hoteles
                        LinkedList<Room> roomsHotelSol = new LinkedList<>();
                        roomsHotelSol.add(new Room("101", 100000, 2, "FotoHotelRelleno.png", "Habitación doble"));
                        roomsHotelSol.add(new Room("102", 150000, 4, "FotoHotelRelleno.png", "Suite familiar"));

                        LinkedList<Room> roomsHotelLuna = new LinkedList<>();
                        roomsHotelLuna.add(new Room("201", 80000, 1, "FotoHotelRelleno.png", "Habitación individual"));
                        roomsHotelLuna.add(
                                        new Room("202", 120000, 2, "FotoHotelRelleno.png", "Habitación doble premium"));

                        LinkedList<Room> roomsHotelEstrella = new LinkedList<>();
                        roomsHotelEstrella.add(new Room("301", 110000, 2, "FotoHotelRelleno.png", "Habitación doble"));
                        roomsHotelEstrella.add(new Room("302", 140000, 3, "FotoHotelRelleno.png", "Habitación triple")); // Crear
                                                                                                                         // hoteles
                                                                                                                         // usando
                                                                                                                         // las
                                                                                                                         // referencias
                                                                                                                         // a
                                                                                                                         // las
                                                                                                                         // ciudades
                        MainController.getInstance().getHostingService().createHotel(
                                        "Hotel Sol", armenia, "Hotel en Armenia", "sol.jpg", 120000, 5,
                                        new LinkedList<>(serviciosPremium), new LinkedList<>(roomsHotelSol),
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        MainController.getInstance().getHostingService().createHotel(
                                        "Hotel Luna", cali, "Hotel en Cali", "FotoHotelRelleno.png", 100000, 4,
                                        new LinkedList<>(serviciosBasicos), new LinkedList<>(roomsHotelLuna),
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        MainController.getInstance().getHostingService().createHotel(
                                        "Hotel Estrella", bogota, "Hotel en Bogotá", "FotoHotelRelleno.png", 110000, 6,
                                        new LinkedList<>(serviciosPremium), new LinkedList<>(roomsHotelEstrella),
                                        LocalDate.now(), LocalDate.now().plusDays(30));// Crear casas usando las
                                                                                       // referencias a las ciudades
                        MainController.getInstance().getHostingService().createHouse(
                                        "Casa Verde", armenia, "Casa familiar", "FotoHotelRelleno.png",
                                        200000, 4, new LinkedList<>(serviciosBasicos), 30000,
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        MainController.getInstance().getHostingService().createHouse(
                                        "Casa Azul", cali, "Casa con piscina", "FotoHotelRelleno.png",
                                        250000, 5, new LinkedList<>(serviciosPremium), 35000,
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        MainController.getInstance().getHostingService().createHouse(
                                        "Casa Roja", bogota, "Casa céntrica", "FotoHotelRelleno.png",
                                        180000, 6, new LinkedList<>(serviciosBasicos), 25000,
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        MainController.getInstance().getAdminService().createApartament(
                                        "Apartamento Moderno", medellin, "Apartamento céntrico",
                                        "FotoApartamentoRelleno.png",
                                        150000, 3, new LinkedList<>(serviciosBasicos),
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        MainController.getInstance().getAdminService().createApartament(
                                        "Apartamento Familiar", bogota, "Apartamento espacioso",
                                        "FotoApartamentoRelleno.png",
                                        200000, 4, new LinkedList<>(serviciosPremium),
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        MainController.getInstance().getAdminService().createApartament(
                                        "Apartamento Vista", armenia, "Apartamento con vista",
                                        "FotoApartamentoRelleno.png",
                                        180000, 2, new LinkedList<>(serviciosBasicos),
                                        LocalDate.now(), LocalDate.now().plusDays(30));

                        // Verificar que los hostings se crearon correctamente
                        List<Hosting> createdHostings = MainController.getInstance().getHostingService()
                                        .findAllHostings();
                        if (createdHostings != null && !createdHostings.isEmpty()) {
                                System.out.println(
                                                "Creación de hostings de prueba completada con éxito. Total creados: "
                                                                + createdHostings.size());
                                // Guardar inmediatamente después de crear
                                XmlSerializationManager.getInstance().saveAllData();
                        } else {
                                throw new RuntimeException(
                                                "No se pudieron crear los hostings de prueba - la lista está vacía después de la creación");
                        }
                } catch (Exception e) {
                        System.err.println("Error al crear los hostings de prueba: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Fallo al crear hostings de prueba", e);
                }
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
