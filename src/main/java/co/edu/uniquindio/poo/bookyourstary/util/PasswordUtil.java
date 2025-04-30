package co.edu.uniquindio.poo.bookyourstary.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {

    // Devuelve el hash SHA-256 de la contraseña
    public static String hashPassword(String plainPassword) {
        return DigestUtils.sha256Hex(plainPassword);
    }

    // Compara una contraseña sin hash con una ya encriptada
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}
