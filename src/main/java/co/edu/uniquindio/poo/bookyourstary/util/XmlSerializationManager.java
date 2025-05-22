package co.edu.uniquindio.poo.bookyourstary.util;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private static final String BACKUP_DIR = "data/backup";
    private static final Logger logger = Logger.getLogger(XmlSerializationManager.class.getName());
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;
    
    // Nombres de los archivos para cada tipo de datos
    private static final String HOSTINGS_FILE = "hostings.xml";
    private static final String CLIENTS_FILE = "clients.xml";
    private static final String ADMINS_FILE = "admins.xml";
    private static final String BOOKINGS_FILE = "bookings.xml";
    private static final String CITIES_FILE = "cities.xml";
    
    private XmlSerializationManager() {
        initializeDataDirectories();
    }
    
    public static XmlSerializationManager getInstance() {
        if (instance == null) {
            instance = new XmlSerializationManager();
        }
        return instance;
    }
    
    /**
     * Inicializa los directorios de datos y backup si no existen
     */
    private void initializeDataDirectories() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            Files.createDirectories(Paths.get(BACKUP_DIR));
            logger.info("Directorios de datos inicializados correctamente");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al crear los directorios de datos", e);
        }
    }

    private void createBackup() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupFolder = BACKUP_DIR + File.separator + timestamp;
            Files.createDirectory(Paths.get(backupFolder));

            // Copiar todos los archivos XML al directorio de backup
            List<String> xmlFiles = Arrays.asList(HOSTINGS_FILE, CLIENTS_FILE, ADMINS_FILE, BOOKINGS_FILE, CITIES_FILE);
            for (String file : xmlFiles) {
                Path source = Paths.get(DATA_DIR, file);
                if (Files.exists(source)) {
                    Files.copy(source, Paths.get(backupFolder, file), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            logger.info("Backup creado en: " + backupFolder);
        } catch (IOException e) {
            logger.log(Level.WARNING, "No se pudo crear el backup", e);
        }
    }

    /**
     * Maneja la conversión de colecciones inmutables
     */
    private Object convertImmutableCollections(Object obj) {
        if (obj instanceof List) {
            return new ArrayList<>((List<?>) obj);
        } else if (obj instanceof Set) {
            return new HashSet<>((Set<?>) obj);
        } else if (obj instanceof Map) {
            return new HashMap<>((Map<?, ?>) obj);
        }
        return obj;
    }
    
    /**
     * Guarda los datos de la aplicación completa
     */
    public void saveAllData() {
        createBackup();
        boolean success = false;
        int retries = 0;
        
        while (!success && retries < MAX_RETRIES) {
            try {
                saveHostings();
                saveClients();
                saveAdmins();
                saveBookings();
                saveCities();
                success = true;
                logger.info("Todos los datos guardados exitosamente en formato XML");
            } catch (Exception e) {
                retries++;
                if (retries < MAX_RETRIES) {
                    logger.warning("Intento " + retries + " de " + MAX_RETRIES + " falló. Reintentando...");
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    logger.severe("Error al guardar datos después de " + MAX_RETRIES + " intentos: " + e.getMessage());
                }
            }
        }
        
        if (!success) {
            logger.severe("No se pudieron guardar todos los datos después de " + MAX_RETRIES + " intentos");
        }
    }
    
    /**
     * Carga todos los datos de la aplicación
     */
    public void loadAllData() {
        boolean success = false;
        int retries = 0;
        
        while (!success && retries < MAX_RETRIES) {
            try {
                loadCities(); // Primero ciudades, ya que otros objetos dependen de ellas
                loadHostings();
                loadClients();
                loadAdmins();
                loadBookings();
                success = true;
                logger.info("Todos los datos cargados exitosamente desde XML");
            } catch (Exception e) {
                retries++;
                if (retries < MAX_RETRIES) {
                    logger.warning("Intento " + retries + " de " + MAX_RETRIES + " falló. Reintentando...");
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    logger.severe("Error al cargar datos después de " + MAX_RETRIES + " intentos: " + e.getMessage());
                }
            }
        }
        
        if (!success) {
            logger.severe("No se pudieron cargar todos los datos después de " + MAX_RETRIES + " intentos");
        }
    }

    /**
     * Método para guardar un objeto a XML
     */
    private void saveObjectToXml(Object object, String fileName) {
        String filePath = DATA_DIR + File.separator + fileName;
        Path path = Paths.get(filePath);
        
        // Asegurarse que el directorio exista
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al crear directorio para " + filePath, e);
            return;
        }
        
        // Convertir colecciones inmutables a mutables antes de serializar
        Object convertedObject = convertImmutableCollections(object);
        
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath)))) {
            encoder.setExceptionListener(new ExceptionListener() {
                @Override
                public void exceptionThrown(Exception e) {
                    logger.log(Level.SEVERE, "Error durante la serialización XML: " + e.getMessage(), e);
                }
            });
            
            encoder.writeObject(convertedObject);
            logger.info("Datos XML guardados en: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al guardar datos XML en " + filePath, e);
        }
    }
    
    /**
     * Método para cargar un objeto desde XML
     */
    private Object loadObjectFromXml(String fileName) {
        String filePath = DATA_DIR + File.separator + fileName;
        Path file = Paths.get(filePath);
        
        if (!Files.exists(file)) {
            logger.info("El archivo XML " + filePath + " no existe. Se creará uno nuevo.");
            try {
                // Crear un archivo vacío con una lista
                saveObjectToXml(new ArrayList<>(), fileName);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al crear archivo XML nuevo", e);
                return null;
            }
        }
        
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)))) {
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
            
            // Intentar recuperar desde el último backup
            Object recoveredData = tryRecoverFromBackup(fileName);
            if (recoveredData != null) {
                logger.info("Datos recuperados exitosamente desde backup para " + fileName);
                return recoveredData;
            }
            
            return null;
        }
    }

    private Object tryRecoverFromBackup(String fileName) {
        try {
            // Buscar el backup más reciente
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                return null;
            }

            Optional<Path> latestBackup = Files.list(backupDir)
                .filter(Files::isDirectory)
                .max(Comparator.comparing(p -> p.getFileName().toString()));

            if (!latestBackup.isPresent()) {
                return null;
            }

            Path backupFile = latestBackup.get().resolve(fileName);
            if (!Files.exists(backupFile)) {
                return null;
            }

            try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(backupFile.toFile())))) {
                return decoder.readObject();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al intentar recuperar desde backup: " + e.getMessage(), e);
            return null;
        }
    }

    // Métodos específicos para cada tipo de dato
    public void saveHostings() {
        List<Hosting> hostings = MainController.getInstance().getHostingService().findAllHostings();
        saveObjectToXml(new ArrayList<>(hostings), HOSTINGS_FILE);
    }

    @SuppressWarnings("unchecked")
    public void loadHostings() {
        List<Hosting> hostings = (List<Hosting>) loadObjectFromXml(HOSTINGS_FILE);
        if (hostings != null) {
            MainController.getInstance().getHostingService().addHostings(hostings);
            logger.info("Cargados " + hostings.size() + " alojamientos");
        }
    }

    public void saveClients() {
        List<Client> clients = MainController.getInstance().getClientRepository().findAll();
        saveObjectToXml(new ArrayList<>(clients), CLIENTS_FILE);
    }

    @SuppressWarnings("unchecked")
    public void loadClients() {
        List<Client> clients = (List<Client>) loadObjectFromXml(CLIENTS_FILE);
        if (clients != null) {
            clients.forEach(client -> MainController.getInstance().getClientRepository().save(client));
            logger.info("Cargados " + clients.size() + " clientes");
        }
    }

    public void saveAdmins() {
        List<Admin> admins = MainController.getInstance().getAdminRepository().getAdmin();
        if (admins != null && !admins.isEmpty()) {
            // Asegurar que el administrador por defecto esté activado
            for (Admin admin : admins) {
                if (admin != null && admin.getEmail().equalsIgnoreCase("pepito@gmail.com")) {
                    admin.setActive(true);
                }
            }
            saveObjectToXml(new ArrayList<>(admins), ADMINS_FILE);
        } else {
            // Crear administrador por defecto
            Admin defaultAdmin = new Admin("1", "admin", "1234567890", "pepito@gmail.com", "adminpass");
            defaultAdmin.setActive(true);
            List<Admin> defaultAdmins = Collections.singletonList(defaultAdmin);
            saveObjectToXml(new ArrayList<>(defaultAdmins), ADMINS_FILE);
            MainController.getInstance().getAdminRepository().saveAdmin(defaultAdmin);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadAdmins() {
        List<Admin> admins = (List<Admin>) loadObjectFromXml(ADMINS_FILE);
        if (admins != null) {
            admins.forEach(admin -> MainController.getInstance().getAdminRepository().saveAdmin(admin));
            logger.info("Cargados " + admins.size() + " administradores");
        }
    }

    public void saveBookings() {
        List<Booking> bookings = MainController.getInstance().getBookingRepository().findAll();
        saveObjectToXml(new ArrayList<>(bookings), BOOKINGS_FILE);
    }

    @SuppressWarnings("unchecked")
    public void loadBookings() {
        List<Booking> bookings = (List<Booking>) loadObjectFromXml(BOOKINGS_FILE);
        if (bookings != null) {
            bookings.forEach(booking -> MainController.getInstance().getBookingRepository().save(booking));
            logger.info("Cargadas " + bookings.size() + " reservas");
        }
    }

    public void saveCities() {
        List<City> cities = new ArrayList<>(MainController.getInstance().getCityService().findAllCities());
        saveObjectToXml(cities, CITIES_FILE);
    }

    @SuppressWarnings("unchecked")
    public void loadCities() {
        List<City> cities = (List<City>) loadObjectFromXml(CITIES_FILE);
        if (cities != null) {
            CityService cityService = MainController.getInstance().getCityService();
            cities.forEach(city -> 
                cityService.saveCity(city.getName(), city.getCountry(), city.getDepartament())
            );
            logger.info("Cargadas " + cities.size() + " ciudades");
        }
    }

    /**
     * Verifica si existen archivos de datos XML guardados
     * @return true si al menos existe un archivo de datos XML
     */
    public boolean hasStoredData() {
        try {
            Path hostingsFile = Paths.get(DATA_DIR, HOSTINGS_FILE);
            Path clientsFile = Paths.get(DATA_DIR, CLIENTS_FILE);
            Path adminsFile = Paths.get(DATA_DIR, ADMINS_FILE);
            
            boolean hasHostings = Files.exists(hostingsFile) && Files.size(hostingsFile) > 100;
            boolean hasClients = Files.exists(clientsFile) && Files.size(clientsFile) > 100;
            boolean hasAdmins = Files.exists(adminsFile) && Files.size(adminsFile) > 100;
            
            return hasHostings || hasClients || hasAdmins;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error verificando datos almacenados: " + e.getMessage());
            return false;
        }
    }
}
