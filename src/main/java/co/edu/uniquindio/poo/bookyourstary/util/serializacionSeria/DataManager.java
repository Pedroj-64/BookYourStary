package co.edu.uniquindio.poo.bookyourstary.util.serializacionSeria;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import java.io.IOException;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private final SerializationUtil serializationUtil;
    private final ClientesContainer clientesContainer;
    private final HostingsContainer hostingsContainer;
    
    private DataManager() {
        this.serializationUtil = SerializationUtil.getInstance();
        this.clientesContainer = ClientesContainer.getInstance();
        this.hostingsContainer = HostingsContainer.getInstance();
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    public void saveAllData() {
        try {
            // Obtener datos actuales de los repositorios
            List<Client> clients = MainController.getInstance().getClientService().getAllClients();
            List<Hosting> hostings = MainController.getInstance().getHostingService().getAllHostings();
            
            // Actualizar contenedores
            clientesContainer.setClients(clients);
            hostingsContainer.setHostings(hostings);
            
            // Serializar contenedores
            serializationUtil.serialize(clientesContainer, "clients");
            serializationUtil.serialize(hostingsContainer, "hostings");
            
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void loadAllData() {
        try {
            // Deserializar contenedores
            ClientesContainer loadedClients = (ClientesContainer) serializationUtil.deserialize("clients");
            HostingsContainer loadedHostings = (HostingsContainer) serializationUtil.deserialize("hostings");
            
            // Actualizar repositorios
            if (loadedClients != null) {
                MainController.getInstance().getClientService().loadClients(loadedClients.getClients());
            }
            
            if (loadedHostings != null) {
                MainController.getInstance().getHostingService().loadHostings(loadedHostings.getHostings());
            }
            
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            // En caso de error, no hacemos nada - los datos se mantendrán vacíos
            // y el sistema creará datos de prueba si es necesario
        }
    }
}
