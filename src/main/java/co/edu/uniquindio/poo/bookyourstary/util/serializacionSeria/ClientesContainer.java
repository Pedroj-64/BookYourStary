package co.edu.uniquindio.poo.bookyourstary.util.serializacionSeria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientesContainer implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Client> clients;
    
    private static ClientesContainer instance;
    
    private ClientesContainer() {
        this.clients = new ArrayList<>();
    }
    
    public static ClientesContainer getInstance() {
        if (instance == null) {
            instance = new ClientesContainer();
        }
        return instance;
    }
    
    public void addClient(Client client) {
        if (!clients.contains(client)) {
            clients.add(client);
        }
    }

    public void setClients(List<Client> clients) {
        this.clients = new ArrayList<>(clients);
    }
}
