package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.repository.ClientRepository;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;

public class ClientService {

    private final ClientRepository clientRepository;
    private final VirtualWalletService virtualWalletService;

    public ClientService(ClientRepository clientRepository, VirtualWalletService virtualWalletService) {
        this.clientRepository = clientRepository;
        this.virtualWalletService = virtualWalletService;
    }

    // Registrar nuevo cliente
    public void registerClient(Client client, String password) {
        if (clientRepository.findById(client.getId()).isPresent()) {
            throw new IllegalArgumentException("Cliente con ID " + client.getId() + " ya existe.");
        }

        // Encriptar contraseña
        String encryptedPassword = PasswordUtil.hashPassword(password);
        client.setPassword(encryptedPassword);

        // Crear billetera virtual asociada
        VirtualWallet virtualWallet=virtualWalletService.createWalletForClient(client);
        client.setVirtualWallet(virtualWallet);

        clientRepository.save(client);
    }

    public Client getClient(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public void updateClient(Client client) {
        if (!clientRepository.findById(client.getId()).isPresent()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        clientRepository.save(client);
    }

    public void deleteClient(String clientId) {
        clientRepository.delete(clientId);
    }

    public boolean verifyPassword(Client client, String password) {
        String hashedPassword = client.getPassword();
        return PasswordUtil.verifyPassword(password, hashedPassword);
    }
}
