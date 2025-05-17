package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.repository.AdminRepository;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Observer;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Subject;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdminService implements Subject {

    private final AdminRepository adminRepository;
    private final HostingService hostingService;
    private final List<Observer> observers;

    public AdminService(AdminRepository adminRepository, HostingService hostingService) {
        this.adminRepository = adminRepository;
        this.hostingService = hostingService;
        this.observers = new ArrayList<>();
    }

 
    public void registerAdmin(Admin admin) {
        if (adminRepository.isAdminRegistered()) {
            throw new IllegalStateException("Ya hay un administrador registrado");
        }
        adminRepository.saveAdmin(admin);
        notifyObservers("Bienvenido administrador " + admin.getName(), admin.getEmail()); 
    }

    public void activateAdmin() {
        Admin admin = getAdmin();
        admin.setActive(true);
        notifyObservers("Su cuenta ha sido activada", admin.getEmail()); // Solo aquí
    }

    public Admin getAdmin() {
        return adminRepository.getAdmin()
                .orElseThrow(() -> new IllegalStateException("No hay administrador registrado"));
    }

    public boolean verifyPassword(Admin admin, String password) {
        return PasswordUtil.verifyPassword(password, admin.getPassword());
    }

    private void validateAdminIsActive() {
        Admin admin = getAdmin();
        if (!admin.isActive()) {
            throw new IllegalStateException("El administrador no está activo");
        }
    }


    public void createHouse(String name, City city, String description, String imageUrl,
                            double priceForNight, int maxGuests,
                            LinkedList<ServiceIncluded> services, double cleaningFee,LocalDate availableFrom,
                            LocalDate availableTo) {
        validateAdminIsActive();
        hostingService.createHouse(name, city, description, imageUrl, priceForNight, maxGuests, services, cleaningFee,
                availableFrom, availableTo);
    }

    public void createApartament(String name, City city, String description, String imageUrl,
                                 double priceForNight, int maxGuests,
                                 LinkedList<ServiceIncluded> services,LocalDate availableFrom,
                                 LocalDate availableTo) {
        validateAdminIsActive();
        hostingService.createApartament(name, city, description, imageUrl, priceForNight, maxGuests, services,
                availableFrom, availableTo);
    }

    public void createHotel(String name, City city, String description, String imageUrl,
                            double basePrice, int maxGuests,
                            LinkedList<ServiceIncluded> services, LinkedList<Room> rooms,LocalDate availableFrom,
                            LocalDate availableTo) {
        validateAdminIsActive();
        hostingService.createHotel(name, city, description, imageUrl, basePrice, maxGuests, services, rooms,
                availableFrom, availableTo);
    }

    public void deleteHosting(String name) {
        validateAdminIsActive();
        hostingService.deleteHosting(name);
    }

    public List<Hosting> getAllHostings() {
        validateAdminIsActive();
        return hostingService.findAllHostings();
    }

    public List<Hosting> getHostingsByCity(City city) {
        validateAdminIsActive();
        return hostingService.findHostingsByCity(city);
    }

    // Observer solo para eventos relacionados con correos
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message, String email) {
        for (Observer observer : observers) {
            observer.update(message, email);
        }
    }
}
