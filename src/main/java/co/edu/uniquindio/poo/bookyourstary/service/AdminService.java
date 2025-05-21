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

    private static AdminService instance;    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService(AdminRepository.getInstance(), HostingService.getInstance());
            
            // Verificar si existe un administrador, y si no, crear uno por defecto
            try {
                Admin admin = instance.getAdmin();
                if (admin == null) {
                    admin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
                    admin.setActive(true);
                    instance.adminRepository.saveAdmin(admin);
                    System.out.println("Administrador por defecto creado en singleton AdminService");
                } else {
                    admin.setActive(true);
                    instance.adminRepository.saveAdmin(admin);
                    System.out.println("Administrador existente activado en singleton AdminService: " + admin.getEmail());
                }
            } catch (Exception e) {
                System.err.println("Error al verificar/crear administrador en AdminService: " + e.getMessage());
            }
        }
        return instance;
    }

    private AdminService(AdminRepository adminRepository, HostingService hostingService) {
        this.adminRepository = adminRepository;
        this.hostingService = hostingService;
        this.observers = new ArrayList<>();
    }
    public void registerAdmin(Admin admin) {
        try {
            // Si ya hay un administrador y es el mismo (por email), actualizarlo
            Admin existingAdmin = getAdmin();
            if (existingAdmin != null && existingAdmin.getEmail().equalsIgnoreCase(admin.getEmail())) {
                admin.setActive(true);
                adminRepository.saveAdmin(admin);
                System.out.println("Administrador actualizado: " + admin.getEmail());
                return;
            }
            
            // Si no hay un administrador, registrar el nuevo
            if (!adminRepository.isAdminRegistered()) {
                admin.setActive(true);
                adminRepository.saveAdmin(admin);
                System.out.println("Nuevo administrador registrado: " + admin.getEmail());
                notifyObservers("Bienvenido administrador " + admin.getName(), admin.getEmail()); 
            } else {
                System.out.println("Ya existe un administrador, no se puede registrar uno nuevo");
                throw new IllegalStateException("Ya hay un administrador registrado");
            }
        } catch (Exception e) {
            System.err.println("Error al registrar administrador: " + e.getMessage());
            // Si falla por cualquier motivo, asegurarnos de que tenemos al menos un admin
            if (!adminRepository.isAdminRegistered()) {
                Admin defaultAdmin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
                defaultAdmin.setActive(true);
                adminRepository.saveAdmin(defaultAdmin);
                System.out.println("Se creó un administrador por defecto debido a un error");
            }
        }
    }

    public void activateAdmin() {
        Admin admin = getAdmin();
        admin.setActive(true);
        notifyObservers("Su cuenta ha sido activada", admin.getEmail()); // Solo aquí
    }    public Admin getAdmin() {
        List<Admin> admins = adminRepository.getAdmin();
        if (admins == null || admins.isEmpty()) {
            // Crear un administrador por defecto si no existe
            System.out.println("No se encontró administrador, creando uno por defecto");
            Admin defaultAdmin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
            defaultAdmin.setActive(true);
            adminRepository.saveAdmin(defaultAdmin);
            return defaultAdmin;
        }
        return admins.get(0);
    }    public boolean verifyPassword(Admin admin, String password) {
        if (admin == null) {
            return false;
        }
        
        // Para el administrador por defecto, permitir la verificación directa para facilitar el acceso
        if (admin.getId().equals("1") && admin.getEmail().equals("pepito@gmail.com") && password.equals("adminpass")) {
            return true;
        }
        
        return PasswordUtil.verifyPassword(password, admin.getPassword());
    }private void validateAdminIsActive() {
        Admin admin = getAdmin();
        if (admin == null || !admin.isActive()) {
            throw new IllegalStateException("No hay administrador activo");
        }
    }


    public void createHouse(String name, City city, String description, String imageUrl,
                            double priceForNight, int maxGuests,
                            LinkedList<ServiceIncluded> services, double cleaningFee,LocalDate availableFrom,
                            LocalDate availableTo) {
        validateAdminIsActive();
        hostingService.createHouse(name, city, description, imageUrl, priceForNight, maxGuests, services, cleaningFee,
                availableFrom, availableTo);
    }    public void createApartament(String name, City city, String description, String imageUrl,
                                 double priceForNight, int maxGuests,
                                 LinkedList<ServiceIncluded> services,LocalDate availableFrom,
                                 LocalDate availableTo) {
        validateAdminIsActive();
        // Utilizar ApartmentCreator en lugar del método problemático
        try {
            Apartament newApartment = new Apartament(
                name, city, description, imageUrl,
                priceForNight, maxGuests, services, 0.0, // Sin tarifa de limpieza por defecto
                availableFrom, availableTo
            );
            hostingService.getApartamentRepository().save(newApartment);
            System.out.println("Apartamento creado correctamente: " + name);
        } catch (Exception e) {
            System.err.println("Error al crear apartamento: " + e.getMessage());
            throw e; // Relanzar para mantener la consistencia con el comportamiento original
        }
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
