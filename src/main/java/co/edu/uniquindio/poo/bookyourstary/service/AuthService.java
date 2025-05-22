package co.edu.uniquindio.poo.bookyourstary.service;

import lombok.Getter;
import lombok.Setter;

public class AuthService {
    /**
     * -- SETTER --
     *  Establece el correo del usuario actual.
     *
     *
     * -- GETTER --
     *  Obtiene el correo del usuario actual.
     *
     @param email Correo del usuario que inició sesión.
      * @return El correo del usuario actual.
     */
    @Getter
    @Setter
    private static String currentUserEmail = ""; // Correo del usuario actual, inicialmente vacío

    /**
     * Verifica si el usuario actual es administrador.
     * Solo el correo "pepito@gmail.com" tiene permisos de administrador.
     *
     * @return true si el usuario es administrador, false en caso contrario.
     */
    public static boolean isAdmin() {
        return "pepito@gmail.com".equals(currentUserEmail);
    }

}
