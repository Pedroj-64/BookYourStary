package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.CodeActivation;
import co.edu.uniquindio.poo.bookyourstary.repository.CodeActivationRepository;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class CodeActivationService {

    private final CodeActivationRepository repository;
    private final EmailNotifier emailNotifier;
    private final EmailTemplateService emailTemplateService;

    public CodeActivationService(CodeActivationRepository repository,
            EmailNotifier emailNotifier,
            EmailTemplateService emailTemplateService) {
        this.repository = repository;
        this.emailNotifier = emailNotifier;
        this.emailTemplateService = emailTemplateService;
    }

    public UUID generateActivationCode(String userEmail) throws MessagingException {
        UUID code = UUID.randomUUID();
        CodeActivation activation = new CodeActivation(code, userEmail, LocalDateTime.now(), false);
        repository.save(activation);

        String html = emailTemplateService.buildActivationEmail(activation);
        emailNotifier.sendHtmlEmail(userEmail, "Activa tu cuenta en BookYourStary", html);

        return code;
    }

    public boolean validateCode(UUID code) {
        Optional<CodeActivation> optional = repository.findById(code);
        if (optional.isEmpty())
            return false;

        CodeActivation activation = optional.get();
        if (activation.isUsed())
            return false;

        activation.setUsed(true);
        return true;
    }
}
