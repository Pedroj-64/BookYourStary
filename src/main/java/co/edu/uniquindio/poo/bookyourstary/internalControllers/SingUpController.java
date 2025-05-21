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
    // private final Map<UUID, String> pendingPasswords = new HashMap<>(); // Removed, password will be hashed and stored in Client object

    public SingUpController(CodeActivationService codeActivationService, ClientService clientService) {
        this.codeActivationService = codeActivationService;
        this.clientService = clientService;
    }


    public UUID initiateSignUp(Client client, String plainPassword) throws MessagingException {
        // Hash the password immediately
        String hashedPassword = co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil.hashPassword(plainPassword);
        client.setPassword(hashedPassword); // Set the hashed password on the client object

        UUID activationCode = codeActivationService.generateActivationCode(client.getEmail());
        pendingClients.put(activationCode, client); // Store client with hashed password

        return activationCode;
    }


    public void completeSignUp(UUID activationCode) {
        if (!codeActivationService.validateCode(activationCode)) {
            // It's good practice to remove the invalid/used code attempt from pending maps if it exists
            pendingClients.remove(activationCode);
            throw new IllegalArgumentException("Código de activación inválido o ya utilizado.");
        }

        Client client = pendingClients.remove(activationCode);

        if (client == null) {
            // This case implies the code was valid but somehow the client data was lost from pending,
            // or the code was guessed/reused after its data was cleared.
            throw new IllegalStateException("No se encontró información de registro para este código de activación. Puede que haya expirado o ya fue utilizado.");
        }

        // Now client.getPassword() already returns the hashed password.
        // We need a method in ClientService that accepts a Client object
        // with an already hashed password.
        clientService.finalizeClientRegistration(client); // Assuming this new method exists/will be created
    }
}
