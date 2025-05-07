package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.service.ClientService;
import co.edu.uniquindio.poo.bookyourstary.service.CodeActivationService;
import jakarta.mail.MessagingException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SingUpController {

    private final CodeActivationService codeActivationService;
    private final ClientService clientService;


    private final Map<UUID, Client> pendingClients = new HashMap<>();
    private final Map<UUID, String> pendingPasswords = new HashMap<>();

    public SingUpController(CodeActivationService codeActivationService, ClientService clientService) {
        this.codeActivationService = codeActivationService;
        this.clientService = clientService;
    }


    public UUID initiateSignUp(Client client, String password) throws MessagingException {
        UUID activationCode = codeActivationService.generateActivationCode(client.getEmail());

        pendingClients.put(activationCode, client);
        pendingPasswords.put(activationCode, password);

        return activationCode;
    }


    public void completeSignUp(UUID activationCode) {
        if (!codeActivationService.validateCode(activationCode)) {
            throw new IllegalArgumentException("Código de activación inválido o ya utilizado.");
        }

        Client client = pendingClients.remove(activationCode);
        String password = pendingPasswords.remove(activationCode);

        if (client == null || password == null) {
            throw new IllegalStateException("No se encontró un cliente asociado al código.");
        }

        clientService.registerClient(client, password);
    }
}
