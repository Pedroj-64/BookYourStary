package co.edu.uniquindio.poo.bookyourstary.service.implementService;

import co.edu.uniquindio.poo.bookyourstary.model.CodeRecovery;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.repository.ClientRepository;
import co.edu.uniquindio.poo.bookyourstary.repository.CodeRecoveryRepository;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordGenerator;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class CodeRecoveryService {

    private static CodeRecoveryService instance;

    private final ClientRepository clientRepository;
    private final EmailTemplateService emailTemplateService;
    private final EmailNotifier emailNotifier;
    private final CodeRecoveryRepository codeRecoveryRepository;

    private CodeRecoveryService(ClientRepository clientRepository, CodeRecoveryRepository codeRecoveryRepository,EmailTemplateService emailTemplateService,
            EmailNotifier emailNotifier) {
        this.clientRepository = clientRepository;
        this.emailTemplateService = emailTemplateService;
        this.emailNotifier = emailNotifier;
        this.codeRecoveryRepository = codeRecoveryRepository;
    }

    public static CodeRecoveryService getInstance() {
        if (instance == null) {
            instance = new CodeRecoveryService(
                ClientRepository.getInstance(),
                CodeRecoveryRepository.getInstance(),
                EmailTemplateService.getInstance(),
                EmailNotifier.getInstance()
            );
        }
        return instance;
    }

    public void recoverPassword(String email) throws MessagingException {
        Optional<Client> optionalClient = clientRepository.findAll()
                .stream()
                .filter(client -> client.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();

            UUID code = UUID.randomUUID();
            String tempPassword = PasswordGenerator.generateTempPassword();

            client.setPassword(PasswordUtil.hashPassword(tempPassword));

           
            CodeRecovery recovery = new CodeRecovery(code, email, LocalDateTime.now(), false);
            codeRecoveryRepository.save(recovery);

            
            String content = emailTemplateService.buildRecoveryEmail(recovery, tempPassword);
            emailNotifier.sendHtmlEmail(email, "Recuperación de contraseña", content);

        } else {
            throw new IllegalArgumentException("No se encontró un cliente con ese correo");
        }
    }
}
