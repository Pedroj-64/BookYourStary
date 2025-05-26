package co.edu.uniquindio.poo.bookyourstary.service;

public interface IEmailTemplateService {
    /**
     * Obtiene la plantilla de email de activación
     */
    String getActivationEmailTemplate(String name, String activationCode);

    /**
     * Obtiene la plantilla de email de recuperación
     */
    String getRecoveryEmailTemplate(String name, String recoveryCode);

    /**
     * Obtiene la plantilla de email de confirmación de reserva
     */
    String getBookingConfirmationTemplate(String name, String bookingId);

    /**
     * Obtiene la plantilla de email de cancelación de reserva
     */
    String getBookingCancellationTemplate(String name, String bookingId);

    /**
     * Obtiene la plantilla de email de bienvenida
     */
    String getWelcomeEmailTemplate(String name);
}
