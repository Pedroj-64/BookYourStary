package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.service.CodeRecoveryService;
import jakarta.mail.MessagingException;

public class PasswordRecoveryController {

    private final CodeRecoveryService codeRecoveryService;

    public PasswordRecoveryController(CodeRecoveryService codeRecoveryService) {
        this.codeRecoveryService = codeRecoveryService;
    }

    /**
     * Inicia el proceso de recuperación de contraseña.
     * @param email correo del cliente que quiere recuperar su contraseña
     */
    public void recoverPassword(String email) {
        try {
            codeRecoveryService.recoverPassword(email);
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo de recuperación. Intenta más tarde.", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Correo no registrado en el sistema.");
        }
    }
}
