package co.edu.uniquindio.poo.bookyourstary.service;

public interface ICodeRecoveryService {
    /**
     * Genera un código de recuperación
     */
    String generateRecoveryCode();

    /**
     * Valida un código de recuperación
     */
    boolean validateRecoveryCode(String code);

    /**
     * Guarda un código de recuperación para un email
     */
    void saveRecoveryCode(String email, String code);

    /**
     * Obtiene el código de recuperación para un email
     */
    String getRecoveryCode(String email);

    /**
     * Elimina el código de recuperación para un email
     */
    void removeRecoveryCode(String email);

    /**
     * Limpia todos los códigos de recuperación
     */
    void clearAll();
}
