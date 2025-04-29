package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Client;

public class ClientRepository {

    private final LinkedList<Client> clients;

    public ClientRepository() {
        this.clients = new LinkedList<>();
    }

    public void save(Client client) {
        clients.add(client);
    }

    public Optional<Client> findById(String clientId) {
        return clients.stream().filter(client -> client.getId().equals(clientId)).findFirst();
    }

    public LinkedList<Client> findAll() {
        return new LinkedList<>(clients);
    }

    public void delete(String clientId) {
        clients.removeIf(client -> client.getId().equals(clientId));
    }

}
