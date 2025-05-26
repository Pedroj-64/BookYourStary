package co.edu.uniquindio.poo.bookyourstary.service;

public interface ICodeActivationService {
    /**
     * Genera un código de activación
     */
    String generateActivationCode();

    /**
     * Valida un código de activación
     */
    boolean validateActivationCode(String code);

    /**
     * Guarda un código de activación para un email
     */
    void saveActivationCode(String email, String code);

    /**
     * Obtiene el código de activación para un email
     */
    String getActivationCode(String email);

    /**
     * Elimina el código de activación para un email
     */
    void removeActivationCode(String email);

    /**
     * Limpia todos los códigos de activación
     */
    void clearAll();
}
