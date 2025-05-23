package co.edu.uniquindio.poo.bookyourstary.util;

import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.service.CityService;

/**
 * Clase para manejar la serialización y deserialización XML de los datos del sistema.
 * Implementa el patrón Singleton para garantizar consistencia en el guardado/carga de datos.
 */
public class XmlSerializationManager {
    private static volatile XmlSerializationManager instance;
    private static final String DATA_DIR = "src/main/resources/data";
    private static final String BACKUP_DIR = "src/main/resources/data/backup";
    private static final Logger logger = Logger.getLogger(XmlSerializationManager.class.getName());
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;
    private static final int MAX_BACKUPS = 5; // Mantener solo los últimos 5 backups
    
    // Nombres de los archivos para cada tipo de datos
    private static final String HOSTINGS_FILE = "hostings.xml";
    private static final String CLIENTS_FILE = "clients.xml";
    private static final String ADMINS_FILE = "admins.xml";
    private static final String BOOKINGS_FILE = "bookings.xml";
    private static final String CITIES_FILE = "cities.xml";
    
    private XmlSerializationManager() {
        if (instance != null) {
            throw new IllegalStateException("Ya existe una instancia de XmlSerializationManager");
        }
        initializeDataDirectories();
    }
    
    public static XmlSerializationManager getInstance() {
        XmlSerializationManager result = instance;
        if (result == null) {
            synchronized (XmlSerializationManager.class) {
                result = instance;
                if (result == null) {
                    instance = result = new XmlSerializationManager();
                }
            }
        }
        return result;
    }
    
    /**
     * Inicializa los directorios de datos y backup si no existen
     */
    private void initializeDataDirectories() {
        try {
            // Asegurar que los directorios existan
            Files.createDirectories(Paths.get(DATA_DIR));
            Files.createDirectories(Paths.get(BACKUP_DIR));
            
            // Copiar archivos de recursos si no existen
            copyResourceIfNotExists(HOSTINGS_FILE);
            copyResourceIfNotExists(CLIENTS_FILE);
            copyResourceIfNotExists(ADMINS_FILE);
            copyResourceIfNotExists(BOOKINGS_FILE);
            copyResourceIfNotExists(CITIES_FILE);
            
            logger.info("Directorios de datos inicializados correctamente");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al crear los directorios de datos", e);
        }
    }

    /**
     * Copia un archivo de recursos al directorio de datos si no existe
     */
    private void copyResourceIfNotExists(String fileName) throws IOException {
        Path destPath = Paths.get(DATA_DIR, fileName);
        if (!Files.exists(destPath)) {
            // Intentar cargar desde recursos
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("data/" + fileName);
            if (resourceStream != null) {
                Files.copy(resourceStream, destPath);
                resourceStream.close();
            } else {
                // Si el recurso no existe, crear un archivo vacío
                saveObjectToXml(new ArrayList<>(), fileName);
            }
        }
    }

    /**
     * Método para guardar un objeto a XML
     */
    private void saveObjectToXml(Object object, String fileName) {
        String filePath = DATA_DIR + File.separator + fileName;
        Path path = Paths.get(filePath);
        
        try {
            Files.createDirectories(path.getParent());
            
            Object convertedObject = convertImmutableCollections(object);
            
            try (XMLEncoder encoder = new XMLEncoder(
                    new BufferedOutputStream(
                        new FileOutputStream(filePath)))) {
                
                encoder.setPersistenceDelegate(LocalDate.class, new LocalDatePersistenceDelegate());
                encoder.setExceptionListener(e -> {
                    logger.log(Level.SEVERE, "Error durante la serialización XML: " + e.getMessage(), e);
                    throw new RuntimeException("Error de serialización", e);
                });
                
                encoder.writeObject(convertedObject);
                logger.info("Datos XML guardados en: " + filePath);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al guardar datos XML en " + filePath, e);
            throw new RuntimeException("Error al guardar archivo XML", e);
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
                copyResourceIfNotExists(fileName);
                if (!Files.exists(file)) {
                    saveObjectToXml(new ArrayList<>(), fileName);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al crear archivo XML nuevo", e);
                return null;
            }
        }
        
        try (XMLDecoder decoder = new XMLDecoder(
                new BufferedInputStream(
                    new FileInputStream(filePath)))) {
            
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
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                logger.warning("No existe directorio de backup: " + BACKUP_DIR);
                return null;
            }

            Optional<Path> latestBackup = Files.list(backupDir)
                .filter(Files::isDirectory)
                .max(Comparator.comparing(p -> p.getFileName().toString()));

            if (!latestBackup.isPresent()) {
                logger.warning("No se encontraron backups en " + BACKUP_DIR);
                return null;
            }

            Path backupFile = latestBackup.get().resolve(fileName);
            if (!Files.exists(backupFile)) {
                logger.warning("Archivo " + fileName + " no encontrado en el backup más reciente");
                return null;
            }

            try (XMLDecoder decoder = new XMLDecoder(
                    new BufferedInputStream(
                        new FileInputStream(backupFile.toFile())))) {
                
                decoder.setExceptionListener(e -> {
                    logger.log(Level.SEVERE, "Error al deserializar backup: " + e.getMessage(), e);
                    throw new RuntimeException("Error al deserializar backup", e);
                });
                
                Object obj = decoder.readObject();
                logger.info("Datos recuperados exitosamente desde backup: " + backupFile);
                return obj;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al acceder al backup: " + e.getMessage(), e);
            return null;
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error al deserializar backup: " + e.getMessage(), e);
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
    }    @SuppressWarnings("unchecked")
    public void loadClients() {
        List<Client> clients = (List<Client>) loadObjectFromXml(CLIENTS_FILE);
        if (clients != null) {
            for (Client client : clients) {
                if (MainController.getInstance().getClientRepository().findById(client.getId()) == null) {
                    MainController.getInstance().getClientRepository().save(client);
                } else {
                    logger.warning("Cliente con ID " + client.getId() + " ya existe, ignorando...");
                }
            }
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
    }    @SuppressWarnings("unchecked")
    public void loadCities() {
        List<City> cities = (List<City>) loadObjectFromXml(CITIES_FILE);
        if (cities != null) {
            CityService cityService = MainController.getInstance().getCityService();
            List<City> existingCities = cityService.findAllCities();
            int loadedCount = 0;
            
            for (City city : cities) {
                try {
                    boolean exists = existingCities.stream()
                        .anyMatch(c -> c.getName().equals(city.getName()));
                        
                    if (!exists) {
                        cityService.saveCity(city.getName(), city.getCountry(), city.getDepartament());
                        loadedCount++;
                    } else {
                        logger.fine("Ciudad " + city.getName() + " ya existe, ignorando...");
                    }
                } catch (Exception e) {
                    logger.warning("Error al cargar ciudad " + city.getName() + ": " + e.getMessage());
                }
            }
            
            logger.info("Cargadas " + loadedCount + " ciudades de " + cities.size() + " encontradas");
        }
    }

    /**
     * Verifica si existen archivos de datos XML guardados
     * @return true si al menos existe un archivo de datos XML
     */    public boolean hasStoredData() {
        try {
            // Verificar que los archivos existan y tengan contenido válido
            boolean hasHostings = false;
            boolean hasClients = false;
            boolean hasAdmins = false;

            List<?> hostings = (List<?>) loadObjectFromXml(HOSTINGS_FILE);
            List<?> clients = (List<?>) loadObjectFromXml(CLIENTS_FILE);
            List<?> admins = (List<?>) loadObjectFromXml(ADMINS_FILE);
            
            hasHostings = hostings != null && !hostings.isEmpty();
            hasClients = clients != null && !clients.isEmpty();
            hasAdmins = admins != null && !admins.isEmpty();
            
            logger.info("Verificación de datos XML: Hostings=" + hasHostings + 
                       " (" + (hostings != null ? hostings.size() : 0) + " elementos), " +
                       "Clients=" + hasClients + 
                       " (" + (clients != null ? clients.size() : 0) + " elementos), " +
                       "Admins=" + hasAdmins + 
                       " (" + (admins != null ? admins.size() : 0) + " elementos)");
            
            return hasHostings || hasClients || hasAdmins;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al verificar datos almacenados: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Maneja la conversión de colecciones inmutables a mutables
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
     * Crea un backup de los datos antes de guardar
     */
    private void createBackup() {
        try {
            Path backupPath = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
            }

            // Crear nombre único para el backup con milisegundos para evitar colisiones
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            Path backupFolder = backupPath.resolve(timestamp);
            
            // Si ya existe un backup reciente (menos de 1 segundo), no crear otro
            try (Stream<Path> backups = Files.list(backupPath)) {
                boolean recentBackupExists = backups
                    .filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .anyMatch(name -> name.substring(0, 15).equals(timestamp.substring(0, 15)));
                    
                if (recentBackupExists) {
                    logger.info("Backup reciente encontrado, saltando creación");
                    return;
                }
            }

            Files.createDirectories(backupFolder);

            // Copiar todos los archivos XML al directorio de backup
            List<String> xmlFiles = Arrays.asList(HOSTINGS_FILE, CLIENTS_FILE, ADMINS_FILE, BOOKINGS_FILE, CITIES_FILE);
            for (String file : xmlFiles) {
                Path dataDir = Paths.get(DATA_DIR);
                Path source = dataDir.resolve(file);
                if (Files.exists(source)) {
                    Files.copy(source, backupFolder.resolve(file), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Limpiar backups antiguos
            cleanOldBackups();

            logger.info("Backup creado en: " + backupFolder);
        } catch (IOException e) {
            logger.log(Level.WARNING, "No se pudo crear el backup", e);
        }
    }

    private void cleanOldBackups() {
        try {
            Path backupPath = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupPath)) {
                return;
            }

            // Obtener todos los backups ordenados por fecha
            List<Path> backups = Files.list(backupPath)
                .filter(Files::isDirectory)
                .sorted((a, b) -> b.getFileName().toString().compareTo(a.getFileName().toString()))
                .collect(Collectors.toList());

            // Eliminar backups antiguos si hay más que MAX_BACKUPS
            if (backups.size() > MAX_BACKUPS) {
                for (int i = MAX_BACKUPS; i < backups.size(); i++) {
                    deleteDirectory(backups.get(i));
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error al limpiar backups antiguos", e);
        }
    }

    private void deleteDirectory(Path directory) {
        try {
            Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Error al eliminar archivo/directorio: " + path, e);
                    }
                });
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error al eliminar directorio de backup: " + directory, e);
        }
    }

    /**
     * Clase interna para manejar la serialización/deserialización de objetos LocalDate a XML
     */
    static class LocalDatePersistenceDelegate extends java.beans.PersistenceDelegate {
        @Override
        protected Expression instantiate(Object oldInstance, Encoder encoder) {
            if (oldInstance instanceof LocalDate) {
                LocalDate date = (LocalDate) oldInstance;
                return new Expression(date, date.getClass(), "of", 
                    new Object[] { date.getYear(), date.getMonthValue(), date.getDayOfMonth() });
            }
            return null;
        }
    }

    /**
     * Guarda todos los datos de la aplicación
     */
    public void saveAllData() {
        try {
            createBackup();
            
            // Guardar datos
            saveHostings();
            saveClients();
            saveAdmins();
            saveBookings();
            saveCities();
            
            logger.info("Todos los datos guardados exitosamente en XML");
        } catch (Exception e) {
            logger.severe("Error al guardar datos: " + e.getMessage());
            throw new RuntimeException("Error al guardar datos", e);
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
                // Primero limpiamos los datos existentes para evitar duplicados
                MainController.getInstance().getHostingService().clearAll();
                MainController.getInstance().getClientRepository().clearAll();
                MainController.getInstance().getAdminRepository().clearAll();
                MainController.getInstance().getBookingRepository().clearAll();
                MainController.getInstance().getCityService().clearAll();
                
                // Cargar datos en orden de dependencia
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
                    throw new RuntimeException("Error al cargar datos después de " + MAX_RETRIES + " intentos", e);
                }
            }
        }
    }
}
