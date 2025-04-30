package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.repository.ClientRepository;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Registrar nuevo cliente
    public void registerClient(Client client, String password) {
        if (clientRepository.findById(client.getId()).isPresent()) {
            throw new IllegalArgumentException("Cliente con ID " + client.getId() + " ya existe.");
        }

        // Encriptar la contraseña antes de guardar
        String encryptedPassword = PasswordUtil.hashPassword(password);
        client.setPassword(encryptedPassword);

        clientRepository.save(client);
    }


    // Obtener un cliente por ID
    public Client getClient(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    // Actualizar información del cliente
    public void updateClient(Client client) {
        if (!clientRepository.findById(client.getId()).isPresent()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        clientRepository.save(client);
    }

    // Eliminar cliente
    public void deleteClient(String clientId) {
        clientRepository.delete(clientId);
    }

    public boolean verifyPassword(Client client, String password) {
        String hashedPassword = client.getPassword();
        return PasswordUtil.verifyPassword(password, hashedPassword);
    }
}
