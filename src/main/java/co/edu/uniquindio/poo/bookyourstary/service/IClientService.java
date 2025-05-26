package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Subject;
import java.util.List;
import java.util.Optional;

public interface IClientService extends Subject {
    /**
     * Registra un nuevo cliente
     */
    void registerClient(Client client);

    /**
     * Activa un cliente
     */
    void activateClient(String email);

    /**
     * Busca un cliente por email
     */
    Optional<Client> findByEmail(String email);

    /**
     * Verifica si existe un cliente con el email dado
     */
    boolean existsClientWithEmail(String email);

    /**
     * Verifica la contraseña de un cliente
     */
    boolean verifyPassword(Client client, String password);

    /**
     * Obtiene un cliente por su ID
     */
    Optional<Client> getClientById(String id);

    /**
     * Obtiene todos los clientes
     */
    List<Client> getAllClients();

    /**
     * Actualiza un cliente existente
     */
    void updateClient(Client client);

    /**
     * Desactiva un cliente
     */
    void deactivateClient(String email);

    /**
     * Asigna una billetera virtual a un cliente
     */
    void assignVirtualWallet(Client client, VirtualWallet wallet);

    /**
     * Elimina todos los clientes
     */
    void clearAll();
}
