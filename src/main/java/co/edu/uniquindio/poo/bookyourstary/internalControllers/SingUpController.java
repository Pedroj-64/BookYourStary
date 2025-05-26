package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.ClientService;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.CodeActivationService;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;
import jakarta.mail.MessagingException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SingUpController {

    private final CodeActivationService codeActivationService;
    private final ClientService clientService;

    private final Map<UUID, Client> pendingClients = new HashMap<>();
    // private final Map<UUID, String> pendingPasswords = new HashMap<>(); //
    // Removed, password will be hashed and stored in Client object

    public SingUpController(CodeActivationService codeActivationService, ClientService clientService) {
        this.codeActivationService = codeActivationService;
        this.clientService = clientService;
    }

    public UUID initiateSignUp(Client client, String plainPassword) throws MessagingException {
        // Hash the password immediately
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        client.setPassword(hashedPassword); // Set the hashed password on the client object

        UUID activationCode = codeActivationService.generateActivationCode(client.getEmail());
        pendingClients.put(activationCode, client); // Store client with hashed password

        return activationCode;
    }

    public void completeSignUp(UUID activationCode) {
        if (!codeActivationService.validateCode(activationCode)) {
            pendingClients.remove(activationCode);
            throw new IllegalArgumentException("Código de activación inválido o ya utilizado.");
        }

        Client client = pendingClients.remove(activationCode);

        if (client == null) {
            throw new IllegalStateException(
                    "No se encontró información de registro para este código de activación. Puede que haya expirado o ya fue utilizado.");
        }

        // Create wallet using VirtualWalletService through MainController
        VirtualWallet wallet = MainController.getInstance()
                .getVirtualWalletService()
                .createWalletForClient(client);

        client.setVirtualWallet(wallet);
        client.setActive(true);

        // Now register the client with their wallet
        clientService.finalizeClientRegistration(client);

        System.out.println("Cliente activado exitosamente. ID de billetera: " + wallet.getIdWallet());
    }
}
