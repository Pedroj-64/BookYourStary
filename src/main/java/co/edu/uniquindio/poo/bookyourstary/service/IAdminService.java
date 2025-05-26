package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Subject;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public interface IAdminService extends Subject {
    /**
     * Registra un nuevo administrador o actualiza uno existente
     */
    void registerAdmin(Admin admin);

    /**
     * Activa la cuenta del administrador
     */
    void activateAdmin();

    /**
     * Obtiene el administrador actual
     */
    Admin getAdmin();

    /**
     * Obtiene el administrador actual o crea uno por defecto si no existe
     */
    Admin getOrCreateAdmin();

    /**
     * Verifica la contraseña del administrador
     */
    boolean verifyPassword(Admin admin, String password);

    /**
     * Crea una nueva casa
     */
    void createHouse(String name, City city, String description, String imageUrl,
                    double priceForNight, int maxGuests,
                    LinkedList<ServiceIncluded> services, double cleaningFee,
                    LocalDate availableFrom, LocalDate availableTo);

    /**
     * Crea un nuevo apartamento
     */
    void createApartament(String name, City city, String description, String imageUrl,
                         double priceForNight, int maxGuests,
                         LinkedList<ServiceIncluded> services,
                         LocalDate availableFrom, LocalDate availableTo);

    /**
     * Crea un nuevo hotel
     */
    void createHotel(String name, City city, String description, String imageUrl,
                     double basePrice, int maxGuests,
                     LinkedList<ServiceIncluded> services, LinkedList<Room> rooms,
                     LocalDate availableFrom, LocalDate availableTo);

    /**
     * Elimina un alojamiento por su nombre
     */
    void deleteHosting(String name);

    /**
     * Obtiene todos los alojamientos
     */
    List<Hosting> getAllHostings();

    /**
     * Obtiene los alojamientos por ciudad
     */
    List<Hosting> getHostingsByCity(City city);

    /**
     * Elimina todos los administradores y crea uno por defecto
     */
    void clearAll();
}
