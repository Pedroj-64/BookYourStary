package co.edu.uniquindio.poo.bookyourstary.service.implementService;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.repository.AdminRepository;
import co.edu.uniquindio.poo.bookyourstary.service.IAdminService;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Observer;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Subject;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AdminService implements IAdminService {

    private final AdminRepository adminRepository;
    private final HostingService hostingService;
    private final List<Observer> observers;
    
    private static final String DEFAULT_ADMIN_ID = "1";
    private static final String DEFAULT_ADMIN_NAME = "admin";
    private static final String DEFAULT_ADMIN_PHONE = "1234567890";
    private static final String DEFAULT_ADMIN_EMAIL = "pepito@gmail.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "adminpass";

    private static AdminService instance;
    private static boolean isCreatingDefaultAdmin = false;

    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService(AdminRepository.getInstance(), HostingService.getInstance());
            instance.ensureDefaultAdminExists();
        }
        return instance;
    }

    private AdminService(AdminRepository adminRepository, HostingService hostingService) {
        this.adminRepository = adminRepository;
        this.hostingService = hostingService;
        this.observers = new ArrayList<>();
    }

    /**
     * Asegura que existe un administrador por defecto en el sistema
     */
    private void ensureDefaultAdminExists() {
        if (isCreatingDefaultAdmin) {
            return;
        }
        
        try {
            isCreatingDefaultAdmin = true;
            List<Admin> admins = adminRepository.getAdmin();
            
            if (admins == null || admins.isEmpty()) {
                createDefaultAdmin();
            } else {
                Admin admin = admins.get(0);
                admin.setActive(true);
                adminRepository.saveAdmin(admin);
                System.out.println("Administrador existente activado: " + admin.getEmail());
            }
        } catch (Exception e) {
            System.err.println("Error al verificar/crear administrador: " + e.getMessage());
            if (!isCreatingDefaultAdmin) { // Evitar recursión en caso de error
                createDefaultAdmin();
            }
        } finally {
            isCreatingDefaultAdmin = false;
        }
    }

    /**
     * Crea y guarda el administrador por defecto
     */
    private void createDefaultAdmin() {
        Admin defaultAdmin = new Admin(
            DEFAULT_ADMIN_ID, 
            DEFAULT_ADMIN_NAME,
            DEFAULT_ADMIN_PHONE,
            DEFAULT_ADMIN_EMAIL,
            DEFAULT_ADMIN_PASSWORD
        );
        defaultAdmin.setActive(true);
        adminRepository.saveAdmin(defaultAdmin);
        System.out.println("Administrador por defecto creado");
    }

    /**
     * Registra un nuevo administrador o actualiza uno existente
     */
    public void registerAdmin(Admin admin) {
        try {
            Optional<Admin> existingAdmin = adminRepository.findByEmail(admin.getEmail());
            
            if (existingAdmin.isPresent()) {
                // Si es el mismo admin (por email), actualizarlo
                admin.setActive(true);
                adminRepository.saveAdmin(admin);
                System.out.println("Administrador actualizado: " + admin.getEmail());
                return;
            }
            
            // Si ya existe algún admin (diferente), no permitir el registro
            if (!adminRepository.getAdmin().isEmpty()) {
                System.out.println("Ya existe un administrador, no se puede registrar uno nuevo");
                throw new IllegalStateException("Ya hay un administrador registrado");
            }
            
            // Si no hay ningún admin, registrar el nuevo
            admin.setActive(true);
            adminRepository.saveAdmin(admin);
            System.out.println("Nuevo administrador registrado: " + admin.getEmail());
            notifyObservers("Bienvenido administrador " + admin.getName(), admin.getEmail());
            
        } catch (Exception e) {
            System.err.println("Error al registrar administrador: " + e.getMessage());
            if (!isCreatingDefaultAdmin) {
                ensureDefaultAdminExists();
            }
            throw e;
        }
    }

    public void activateAdmin() {
        Admin admin = getAdmin();
        if (admin != null) {
            admin.setActive(true);
            adminRepository.saveAdmin(admin);
            notifyObservers("Su cuenta ha sido activada", admin.getEmail());
        }
    }

    public Admin getAdmin() {
        // Usar directamente el repositorio para evitar recursión
        List<Admin> admins = adminRepository.getAdmin();
        return admins == null || admins.isEmpty() ? null : admins.get(0);
    }

    public Admin getOrCreateAdmin() {
        if (isCreatingDefaultAdmin) {
            return null;
        }
        
        Admin admin = getAdmin();
        if (admin == null) {
            ensureDefaultAdminExists();
            admin = getAdmin();
        }
        return admin;
    }

    public boolean verifyPassword(Admin admin, String password) {
        if (admin == null) {
            return false;
        }
        
        // Para el administrador por defecto, permitir la verificación directa
        if (admin.getId().equals(DEFAULT_ADMIN_ID) && 
            admin.getEmail().equals(DEFAULT_ADMIN_EMAIL) && 
            password.equals(DEFAULT_ADMIN_PASSWORD)) {
            return true;
        }
        
        return PasswordUtil.verifyPassword(password, admin.getPassword());
    }

    private void validateAdminIsActive() {
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

    /**
     * Elimina todos los administradores del sistema y recrea el administrador por defecto.
     */
    public void clearAll() {
        adminRepository.clearAll();
        createDefaultAdmin();
        XmlSerializationManager.getInstance().saveAllData();
    }

    // Observer methods implementation
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
