package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.App;
import co.edu.uniquindio.poo.bookyourstary.model.CodeActivation;
import co.edu.uniquindio.poo.bookyourstary.model.CodeRecovery;
import co.edu.uniquindio.poo.bookyourstary.util.TemplateLoader;

import java.util.HashMap;
import java.util.Map;

public class EmailTemplateService {
    private static EmailTemplateService instance;

    private EmailTemplateService() {
        // Constructor privado para singleton
    }

    public static EmailTemplateService getInstance() {
        if (instance == null) {
            instance = new EmailTemplateService();
        }
        return instance;
    }

    public String buildActivationEmail(CodeActivation activation) {
        Map<String, String> values = new HashMap<>();
        values.put("code", activation.getCode().toString());
        values.put("date", activation.getCreationDate().toString());
        // Ahora usamos la ruta completa:
        return TemplateLoader.loadTemplate("co/edu/uniquindio/poo/bookyourstary/emailTemplates/activateAccount.html", values);
    }

    public String buildRecoveryEmail(CodeRecovery recovery, String tempPassword) {
        Map<String, String> values = new HashMap<>();
        values.put("password", tempPassword);
        values.put("date", recovery.getCreationDate().toString());
        // ruta correcta para el template de recuperación:
        return TemplateLoader.loadTemplate("co/edu/uniquindio/poo/bookyourstary/emailTemplates/passwordRecovery.html", values);
    }

    public String buildReservationConfirmationEmail(
            String nombreUsuario,
            String nombreAlojamiento,
            String fechaInicio,
            String fechaFin,
            int numeroNoches,
            String qrCodeUrl) {
        Map<String, String> values = new HashMap<>();
        values.put("nombreUsuario", nombreUsuario);
        values.put("nombreAlojamiento", nombreAlojamiento);
        values.put("fechaInicio", fechaInicio);
        values.put("fechaFin", fechaFin);
        values.put("numeroNoches", String.valueOf(numeroNoches));
        values.put("qrCodeUrl", qrCodeUrl);

        // Ajusta la ruta si tienes este template en una subcarpeta
        return TemplateLoader.loadTemplate("co/edu/uniquindio/poo/bookyourstary/emailTemplates/ConfirmBooking.html", values);
    }
}
