package co.edu.uniquindio.poo.bookyourstary.service.implementService;

import co.edu.uniquindio.poo.bookyourstary.App;
import co.edu.uniquindio.poo.bookyourstary.model.Bill;
import co.edu.uniquindio.poo.bookyourstary.model.CodeActivation;
import co.edu.uniquindio.poo.bookyourstary.model.CodeRecovery;
import co.edu.uniquindio.poo.bookyourstary.util.QrUtil;
import co.edu.uniquindio.poo.bookyourstary.util.TemplateLoader;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EmailTemplateService {
    private static EmailTemplateService instance;

    private EmailTemplateService() {
       
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
       
        return TemplateLoader.loadTemplate("co/edu/uniquindio/poo/bookyourstary/emailTemplates/activateAccount.html", values);
    }

    public String buildRecoveryEmail(CodeRecovery recovery, String tempPassword) {
        Map<String, String> values = new HashMap<>();
        values.put("password", tempPassword);
        values.put("date", recovery.getCreationDate().toString());
       
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

        
        return TemplateLoader.loadTemplate("co/edu/uniquindio/poo/bookyourstary/emailTemplates/ConfirmBooking.html", values);
    }
    
    public String buildInvoiceEmail(Bill bill, String qrCodeUrl) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Map<String, String> values = new HashMap<>();
        
        // Detalles de la factura
        values.put("billId", bill.getBillId());
        values.put("billDate", bill.getDate().format(formatter));
        values.put("nombreUsuario", bill.getClient().getName());
        
        // Detalles de la reserva
        values.put("bookingId", bill.getBooking().getBookingId());
        values.put("nombreAlojamiento", bill.getBooking().getHosting().getName());
        values.put("fechaInicio", bill.getBooking().getStartDate().format(formatter));
        values.put("fechaFin", bill.getBooking().getEndDate().format(formatter));
        
        // Cálculos
        int numeroNoches = (int) bill.getBooking().getStartDate().until(bill.getBooking().getEndDate()).getDays();
        double precioPorNoche = bill.getBooking().getHosting().getPricePerNight();
        double subtotal = bill.getSubtotal();
        double descuento = subtotal - bill.getTotal();
        
        values.put("numeroNoches", String.valueOf(numeroNoches));
        values.put("precioPorNoche", String.format("%.2f", precioPorNoche));
        values.put("subtotal", String.format("%.2f", subtotal));
        values.put("descuento", String.format("%.2f", descuento));
        values.put("total", String.format("%.2f", bill.getTotal()));
          // QR
        values.put("qrCodeUrl", qrCodeUrl);
        
        return TemplateLoader.loadTemplate("co/edu/uniquindio/poo/bookyourstary/emailTemplates/InvoiceConfirmation.html", values);
    }
  
}
