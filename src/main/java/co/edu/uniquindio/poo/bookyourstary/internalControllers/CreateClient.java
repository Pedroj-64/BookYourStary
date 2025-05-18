package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.service.ClientService;

/**
 * Utilidad para crear un cliente completamente funcional con todos los servicios y repositorios necesarios.
 */
public class CreateClient {

    /**
     * Crea un cliente con todos los servicios y repositorios necesarios para su funcionamiento.
     * No activa el cliente ni realiza lógica de verificación de email.
     * @param id ID del cliente
     * @param name Nombre del cliente
     * @param phoneNumber Teléfono
     * @param email Correo electrónico
     * @param password Contraseña en texto plano
     * @return Cliente registrado y listo para usarse (no activado)
     */
    public static Client createFullClient(String id, String name, String phoneNumber, String email, String password) {
        // Servicios y repositorios singleton centralizados
        ClientService clientService = MainController.getInstance().getClientService();
        // Crear y registrar el cliente (sin activar ni lógica de email)
        Client client = new Client(id, name, phoneNumber, email, password);
        clientService.registerClient(client, password);
        return client;
    }
}
