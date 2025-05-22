package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Client;

public class ClientRepository {

    private final List<Client> clients;

    private static ClientRepository instance;

    public static ClientRepository getInstance() {
        if (instance == null) {
            instance = new ClientRepository();
        }
        return instance;
    }

    private ClientRepository() {
        this.clients = new LinkedList<>();
    }

    public void save(Client client) {
        if (findById(client.getId()).isPresent()) {
            throw new IllegalArgumentException("Client with ID " + client.getId() + " already exists.");
        } else {
            clients.add(client); 
        }
    }

    public Optional<Client> findById(String clientId) {
        return clients.stream().filter(client -> client.getId().equals(clientId)).findFirst();
    }

    public Optional<Client> findByEmail(String email) {
        return clients.stream().filter(client -> client.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public LinkedList<Client> findAll() {
        return new LinkedList<>(clients);
    }

    public void delete(String clientId) {
        clients.removeIf(client -> client.getId().equals(clientId));
    }

}
