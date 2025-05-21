package co.edu.uniquindio.poo.bookyourstary.util;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.service.CityService;

/**
 * Clase para manejar la serialización y deserialización XML de los datos del sistema.
 * Implementa el patrón Singleton para garantizar consistencia en el guardado/carga de datos.
 */
public class XmlSerializationManager {
    
    private static XmlSerializationManager instance;
    private static final String DATA_DIR = "data";
    private static final Logger logger = Logger.getLogger(XmlSerializationManager.class.getName());
    
    // Nombres de los archivos para cada tipo de datos
    private static final String HOSTINGS_FILE = "hostings.xml";
    private static final String CLIENTS_FILE = "clients.xml";
    private static final String ADMINS_FILE = "admins.xml";
    private static final String BOOKINGS_FILE = "bookings.xml";
    private static final String CITIES_FILE = "cities.xml";
    
    private XmlSerializationManager() {
        // Constructor privado - patrón Singleton
        initializeDataDirectory();
    }
    
    public static XmlSerializationManager getInstance() {
        if (instance == null) {
            instance = new XmlSerializationManager();
        }
        return instance;
    }
    
    /**
     * Inicializa el directorio de datos si no existe
     */
    private void initializeDataDirectory() {
        Path dataDir = Paths.get(DATA_DIR);
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
                logger.info("Directorio de datos creado: " + dataDir.toAbsolutePath());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error al crear el directorio de datos", e);
            }
        }
    }
    
    /**
     * Guarda los datos de la aplicación completa
     */
    public void saveAllData() {
        saveHostings();
        saveClients();
        saveAdmins();
        saveBookings();
        saveCities();
        logger.info("Todos los datos guardados exitosamente en formato XML");
    }
    
    /**
     * Carga todos los datos de la aplicación
     */
    public void loadAllData() {
        loadCities(); // Primero ciudades, ya que otros objetos dependen de ellas
        loadHostings();
        loadClients();
        loadAdmins();
        loadBookings();
        logger.info("Todos los datos cargados exitosamente desde XML");
    }
    
    /**
     * Guarda la lista de alojamientos en formato XML
     */
    public void saveHostings() {
        List<Hosting> hostings = MainController.getInstance().getHostingService().findAllHostings();
        saveObjectToXml(hostings, HOSTINGS_FILE);
    }
    
    /**
     * Carga la lista de alojamientos desde el archivo XML
     */
    @SuppressWarnings("unchecked")
    public void loadHostings() {
        List<Hosting> hostings = (List<Hosting>) loadObjectFromXml(HOSTINGS_FILE);
        if (hostings != null && !hostings.isEmpty()) {
            MainController.getInstance().getHostingService().addHostings(hostings);
            logger.info("Cargados " + hostings.size() + " alojamientos");
        }
    }
    
    /**
     * Guarda la lista de clientes en formato XML
     */
    public void saveClients() {
        List<Client> clients = MainController.getInstance().getClientRepository().findAll();
        saveObjectToXml(clients, CLIENTS_FILE);
    }
    
    /**
     * Carga la lista de clientes desde el archivo XML
     */
    @SuppressWarnings("unchecked")
    public void loadClients() {
        List<Client> clients = (List<Client>) loadObjectFromXml(CLIENTS_FILE);
        if (clients != null && !clients.isEmpty()) {
            for (Client client : clients) {
                MainController.getInstance().getClientRepository().save(client);
            }
            logger.info("Cargados " + clients.size() + " clientes");
        }
    }
    
    /**
     * Guarda la lista de administradores en formato XML
     */    public void saveAdmins() {
        List<Admin> admins = MainController.getInstance().getAdminRepository().getAdmin();
        if (admins != null && !admins.isEmpty()) {
            // Asegurarse de que el administrador esté activado antes de guardar
            for (Admin admin : admins) {
                if (admin != null && admin.getEmail().equalsIgnoreCase("pepito@gmail.com")) {
                    admin.setActive(true);
                }
            }
            saveObjectToXml(admins, ADMINS_FILE);
            logger.info("Guardados " + admins.size() + " administradores");
        } else {
            // Si no hay admins, crear uno por defecto
            Admin defaultAdmin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
            defaultAdmin.setActive(true);
            List<Admin> defaultAdmins = List.of(defaultAdmin);
            saveObjectToXml(defaultAdmins, ADMINS_FILE);
            logger.info("Guardado administrador por defecto");
            
            // Y también guardarlo en el repositorio
            MainController.getInstance().getAdminRepository().saveAdmin(defaultAdmin);
        }
    }
    
    /**
     * Carga la lista de administradores desde el archivo XML
     */
    @SuppressWarnings("unchecked")
    public void loadAdmins() {
        List<Admin> admins = (List<Admin>) loadObjectFromXml(ADMINS_FILE);
        if (admins != null && !admins.isEmpty()) {
            for (Admin admin : admins) {
                MainController.getInstance().getAdminRepository().saveAdmin(admin);
            }
            logger.info("Cargados " + admins.size() + " administradores");
        }
    }
    
    /**
     * Guarda la lista de reservas en formato XML
     */
    public void saveBookings() {
        List<Booking> bookings = MainController.getInstance().getBookingRepository().findAll();
        saveObjectToXml(bookings, BOOKINGS_FILE);
    }
    
    /**
     * Carga la lista de reservas desde el archivo XML
     */
    @SuppressWarnings("unchecked")
    public void loadBookings() {
        List<Booking> bookings = (List<Booking>) loadObjectFromXml(BOOKINGS_FILE);
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                MainController.getInstance().getBookingRepository().save(booking);
            }
            logger.info("Cargadas " + bookings.size() + " reservas");
        }
    }
    
    /**
     * Guarda la lista de ciudades en formato XML
     */
    public void saveCities() {
        List<City> cities = new ArrayList<>();
        MainController.getInstance().getCityService().findAllCities().forEach(cities::add);
        saveObjectToXml(cities, CITIES_FILE);
    }
    
    /**
     * Carga la lista de ciudades desde el archivo XML
     */
    @SuppressWarnings("unchecked")
    public void loadCities() {
        List<City> cities = (List<City>) loadObjectFromXml(CITIES_FILE);
        if (cities != null && !cities.isEmpty()) {
            CityService cityService = MainController.getInstance().getCityService();
            for (City city : cities) {
                cityService.saveCity(city.getName(), city.getCountry(), city.getDepartament());
            }
            logger.info("Cargadas " + cities.size() + " ciudades");
        }
    }
      /**
     * Método para guardar un objeto a XML
     */
    private void saveObjectToXml(Object object, String fileName) {
        String filePath = DATA_DIR + File.separator + fileName;
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath)))) {
            // Establecer un listener para errores de serialización
            encoder.setExceptionListener(new ExceptionListener() {
                @Override
                public void exceptionThrown(Exception e) {
                    logger.log(Level.SEVERE, "Error durante la serialización XML: " + e.getMessage(), e);
                }
            });
            
            encoder.writeObject(object);
            logger.info("Datos XML guardados en: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al guardar datos XML en " + filePath, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al guardar XML: " + e.getMessage(), e);
        }
    }
      /**
     * Método para cargar un objeto desde XML
     */
    private Object loadObjectFromXml(String fileName) {
        String filePath = DATA_DIR + File.separator + fileName;
        Path file = Paths.get(filePath);
        if (!Files.exists(file)) {
            logger.info("El archivo XML " + filePath + " no existe. Se omite la carga.");
            return null;
        }
        
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)))) {
            // Establecer un listener para errores de deserialización
            decoder.setExceptionListener(new ExceptionListener() {
                @Override
                public void exceptionThrown(Exception e) {
                    logger.log(Level.SEVERE, "Error durante la deserialización XML: " + e.getMessage(), e);
                }
            });
            
            Object obj = decoder.readObject();
            logger.info("Datos XML cargados desde: " + filePath);
            return obj;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al cargar datos XML desde " + filePath, e);
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al cargar XML: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Verifica si existen archivos de datos XML guardados
     * @return true si al menos existe un archivo de datos XML
     */
    public boolean hasStoredData() {
        Path hostingsFile = Paths.get(DATA_DIR, HOSTINGS_FILE);
        Path clientsFile = Paths.get(DATA_DIR, CLIENTS_FILE);
        Path adminsFile = Paths.get(DATA_DIR, ADMINS_FILE);
        
        return Files.exists(hostingsFile) || 
               Files.exists(clientsFile) || 
               Files.exists(adminsFile);
    }
}
