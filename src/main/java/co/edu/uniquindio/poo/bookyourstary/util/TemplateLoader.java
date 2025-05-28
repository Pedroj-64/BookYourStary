package co.edu.uniquindio.poo.bookyourstary.util;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

import co.edu.uniquindio.poo.bookyourstary.App;

public class TemplateLoader {

    public static String loadTemplate(String fileName, Map<String, String> values) {
        String template = null;
        boolean usedEmergencyTemplate = false;

        // Lista de posibles rutas para buscar
        String[] possiblePaths = {
                "/co/edu/uniquindio/poo/bookyourstary/emailTemplates/" + fileName,
                "/emailTemplates/" + fileName,
                "emailTemplates/" + fileName,
                "co/edu/uniquindio/poo/bookyourstary/emailTemplates/" + fileName,
                "/" + fileName,
                fileName
        };

        // Intentar cargar desde cada posible ruta
        for (String path : possiblePaths) {
            try {
                // Método 1: Class.getResource (para rutas absolutas)
                URL resourceUrl = App.class.getResource(path);
                if (resourceUrl != null) {
                    try (InputStream inputStream = resourceUrl.openStream();
                            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                        if (scanner.hasNext()) {
                            template = scanner.useDelimiter("\\A").next();
                            System.out.println("Plantilla encontrada en: " + path);
                            break;
                        }
                    }
                }

                // Método 2: ClassLoader.getResource (para rutas relativas)
                URL classLoaderUrl = App.class.getClassLoader().getResource(path);
                if (classLoaderUrl != null) {
                    try (InputStream inputStream = classLoaderUrl.openStream();
                            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                        if (scanner.hasNext()) {
                            template = scanner.useDelimiter("\\A").next();
                            System.out.println("Plantilla encontrada en (ClassLoader): " + path);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // Ignorar excepciones y seguir con la siguiente ruta
            }
        }

        // Si no se pudo cargar la plantilla, usar la de emergencia
        if (template == null) {
            System.out.println("No se pudo encontrar la plantilla: " + fileName + ". Usando plantilla de emergencia.");
            template = generateEmergencyTemplate(fileName);
            usedEmergencyTemplate = true;
        }

        // Reemplazar variables usando solo formato {{variable}}
        if (template != null) {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                String key = "{{" + entry.getKey() + "}}";
                String value = entry.getValue();
                if (value == null) {
                    value = "";  // Prevenir NullPointerException
                }
                template = template.replace(key, value);
            }
        }

        if (usedEmergencyTemplate) {
            System.out.println("ADVERTENCIA: Se utilizó una plantilla de emergencia para: " + fileName);
        }

        return template;
    }

    // Método auxiliar para verificar y listar los recursos disponibles (útil para
    // depuración)
    public static void listAvailableResources() {
        try {
            URL resourceUrl = App.class.getResource("/");
            if (resourceUrl != null) {
                System.out.println("Directorio de recursos encontrado en: " + resourceUrl.getPath());
            } else {
                System.out.println("No se pudo encontrar el directorio raíz de recursos.");
            }
        } catch (Exception e) {
            System.err.println("Error al listar recursos: " + e.getMessage());
        }
    }

    private static String generateEmergencyTemplate(String templateName) {
        if ("activateAccount.html".equals(templateName)) {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Activación de cuenta</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Bienvenido a BookYourStary</h1>\n" +
                    "    <p>Hola ${userName},</p>\n" +
                    "    <p>Gracias por registrarte en BookYourStary. Para activar tu cuenta, por favor haz clic en el siguiente enlace:</p>\n"
                    +
                    "    <p><a href=\"${activationLink}\">Activar mi cuenta</a></p>\n" +
                    "    <p>Si el enlace no funciona, copia y pega esta URL en tu navegador: ${activationLink}</p>\n" +
                    "    <p>Atentamente,<br>El equipo de BookYourStary</p>\n" +
                    "</body>\n" +
                    "</html>";
        } else if ("ConfirmBooking.html".equals(templateName)) {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Confirmación de Reserva</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Reserva Confirmada</h1>\n" +
                    "    <p>Hola ${userName},</p>\n" +
                    "    <p>Tu reserva ha sido confirmada. Detalles:</p>\n" +
                    "    <p>Alojamiento: ${hostingName}<br>\n" +
                    "    Fecha de llegada: ${checkInDate}<br>\n" +
                    "    Fecha de salida: ${checkOutDate}<br>\n" +
                    "    Número de huéspedes: ${guestCount}</p>\n" +
                    "    <p>Atentamente,<br>El equipo de BookYourStary</p>\n" +
                    "</body>\n" +
                    "</html>";
        } else if ("passwordRecovery.html".equals(templateName)) {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Recuperación de Contraseña</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Recuperación de contraseña</h1>\n" +
                    "    <p>Hola ${userName},</p>\n" +
                    "    <p>Has solicitado recuperar tu contraseña. Haz clic en el siguiente enlace:</p>\n" +
                    "    <p><a href=\"${resetLink}\">Restablecer mi contraseña</a></p>\n" +
                    "    <p>Si el enlace no funciona, copia y pega esta URL en tu navegador: ${resetLink}</p>\n" +
                    "    <p>Atentamente,<br>El equipo de BookYourStary</p>\n" +
                    "</body>\n" +
                    "</html>";
        } else {
            return "<!DOCTYPE html><html><body><h1>Plantilla de emergencia</h1><p>Esta es una plantilla generada automáticamente porque no se pudo encontrar: "
                    + templateName + "</p></body></html>";
        }
    }
}
