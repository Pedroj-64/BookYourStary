package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.CreateClient;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientPersistenceTest {
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = MainController.getInstance().getClientService();
        clientService.clearAll();
    }

    @Test
    void testRegisterClient() {
        Client client = CreateClient.createFullClient("123", "Juan", "3001234567", "juan@email.com", "clave123");
        Client found = clientService.getClient("123");
        assertEquals("Juan", found.getName());
        assertEquals("juan@email.com", found.getEmail());
    }

    @Test
    void testUpdateClient() {
        Client client = CreateClient.createFullClient("123", "Juan", "3001234567", "juan@email.com", "clave123");
        client.setName("Pedro");
        clientService.updateClient(client);
        Client updated = clientService.getClient("123");
        assertEquals("Pedro", updated.getName());
    }

    @Test
    void testDeleteClient() {
        Client client = CreateClient.createFullClient("123", "Juan", "3001234567", "juan@email.com", "clave123");
        clientService.deleteClient("123");
        assertThrows(IllegalArgumentException.class, () -> clientService.getClient("123"));
    }

    @Test
    void testListClients() {
        Client client1 = CreateClient.createFullClient("123", "Juan", "3001234567", "juan@email.com", "clave123");
        Client client2 = CreateClient.createFullClient("456", "Ana", "3009876543", "ana@email.com", "clave456");
        List<Client> clients = MainController.getInstance().getClientRepository().findAll();
        assertTrue(clients.stream().anyMatch(c -> c.getId().equals("123")));
        assertTrue(clients.stream().anyMatch(c -> c.getId().equals("456")));
    }
}
