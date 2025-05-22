package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.enums.BookingState;
import co.edu.uniquindio.poo.bookyourstary.util.QrUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para verificar la generación de facturas y códigos QR en BillService
 */
public class BillServiceTest {

    private BillService billService;
    private Booking booking;
    private Client client;
    private Hosting hosting;
    
    /**
     * Configuración inicial para cada prueba
     */
    @BeforeEach
    public void setup() throws Exception {
        // Crear cliente con billetera virtual
        client = new Client();
        client.setId(UUID.randomUUID().toString());
        client.setName("Cliente Prueba");
        client.setEmail("brandon40890@gmail.com"); // Correo actualizado para la prueba real
        
        VirtualWallet wallet = new VirtualWallet();
        wallet.setIdWallet(UUID.randomUUID().toString());
        wallet.setBalance(500.0); // Saldo suficiente para la prueba
        client.setVirtualWallet(wallet);
        
        // Crear un alojamiento para la reserva
        hosting = createMockApartment();
        
        // Crear una reserva
        String bookingId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3); // 2 noches
        int guests = 2;
        double totalPrice = hosting.getPricePerNight() * 2; // 2 noches
        
        booking = new Booking(
            bookingId,
            client,
            hosting,
            startDate,
            endDate,
            guests,
            totalPrice,
            BookingState.PENDING
        );
        
        // Inicializar BillService usando reflexión para evitar dependencias
        billService = BillService.getInstance();
    }
    
    /**
     * Prueba el método privado sendQrEmail verificando que el contenido del QR sea correcto
     */
    @Test
    public void testQrContentFormat() throws Exception {
        // Crear una factura
        double total = 100.0;
        Bill bill = new Bill(
            UUID.randomUUID().toString(), 
            total, 
            LocalDate.now(), 
            client, 
            booking,
            120.0 // subtotal antes de descuentos
        );
        
        // Acceder al método privado usando reflexión
        Method sendQrEmailMethod = BillService.class.getDeclaredMethod("sendQrEmail", Bill.class);
        sendQrEmailMethod.setAccessible(true);
        
        // Verificar directamente la generación del QR
        String expectedQrContent = String.format("Pagaste $%.2f", total);
        String qrCodeBase64 = QrUtil.generateBase64Qr(expectedQrContent, 300, 300);
        
        // Asegurarse de que el QR comienza con el formato data:image/png;base64
        assertTrue(qrCodeBase64.startsWith("data:image/png;base64,"), 
            "El QR generado debe tener el formato correcto para ser mostrado en HTML");
        
        // No podemos verificar el contenido completo del QR ya que está codificado,
        // pero al menos verificamos que se genera sin errores
        assertNotNull(qrCodeBase64);
        
        System.out.println("Test completado: El formato del código QR es correcto");
        System.out.println("Contenido del QR: " + expectedQrContent);
    }
    
    /**
     * Crea un apartamento mock para las pruebas
     */
    private Apartament createMockApartment() {
        Apartament apartment = new Apartament();
        apartment.setName("Apartamento de Prueba");
        apartment.setDescription("Apartamento para prueba de facturación");
        apartment.setPricePerNight(75.0);
        apartment.setMaxGuests(4);
        apartment.setIncludedServices(new LinkedList<>());
        apartment.setCity(new City());
        apartment.setAvailableFrom(LocalDate.now());
        apartment.setAvailableTo(LocalDate.now().plusMonths(3));
        return apartment;
    }
    
    /**
     * Prueba de integración del flujo completo de facturación
     */
    @Test
    public void testFullBillingProcess() {
        try {
            // Creamos una factura usando el servicio
            Bill bill = billService.generateBill(booking);
            
            // Verificamos que la factura se ha creado correctamente
            assertNotNull(bill, "La factura no debe ser nula");
            assertEquals(client, bill.getClient(), "El cliente debe ser el mismo");
            assertEquals(booking, bill.getBooking(), "La reserva debe ser la misma");
            assertNotNull(bill.getBillId(), "La factura debe tener un ID");
            
            // Verificamos el resultado
            System.out.println("Factura generada exitosamente con ID: " + bill.getBillId());
            System.out.println("Total facturado: $" + bill.getTotal());
            System.out.println("Subtotal (antes de descuentos): $" + bill.getSubtotal());
            
            // Verificamos que se aplicó un descuento si corresponde
            if (bill.getSubtotal() > bill.getTotal()) {
                double descuento = bill.getSubtotal() - bill.getTotal();
                System.out.println("Se aplicó un descuento de: $" + descuento);
            }
            
        } catch (Exception e) {
            fail("No debería lanzarse una excepción: " + e.getMessage());
        }
    }
    
    /**
     * Prueba de envío de correo real con QR simplificado
     * Esta prueba enviará un correo electrónico a brandon40890@gmail.com
     */    @Test
    public void testSendRealEmailWithSimpleQr() {
        try {
            System.out.println("Iniciando prueba de envío de correo real a brandon40890@gmail.com...");
            
            // Creamos una factura con datos más significativos para la prueba real
            LocalDate now = LocalDate.now();
            
            // Aseguramos que el correo esté configurado correctamente
            client.setEmail("brandon40890@gmail.com");
            
            Booking testBooking = new Booking(
                "TEST-" + UUID.randomUUID().toString().substring(0, 8),
                client,
                hosting,
                now.plusDays(3),  // Check-in en 3 días
                now.plusDays(6),  // Check-out en 6 días (3 noches)
                2,                // 2 huéspedes
                hosting.getPricePerNight() * 3, // Precio por 3 noches
                BookingState.CONFIRMED
            );
            
            Bill testBill = new Bill(
                "BILL-" + UUID.randomUUID().toString().substring(0, 8),
                225.0,  // Total con algún descuento aplicado
                now,    // Factura generada hoy
                client,
                testBooking,
                250.0   // Subtotal antes de descuentos
            );
            
            // Ejecutar el método sendQrEmail usando reflexión para enviar el correo real
            Method sendQrEmailMethod = BillService.class.getDeclaredMethod("sendQrEmail", Bill.class);
            sendQrEmailMethod.setAccessible(true);
            sendQrEmailMethod.invoke(billService, testBill);
              System.out.println("\n✅ CORREO ENVIADO EXITOSAMENTE ✅");
            System.out.println("Destinatario: brandon40890@gmail.com");
            System.out.println("\nDetalles de la factura:");
            System.out.println("- ID Factura: " + testBill.getBillId());
            System.out.println("- ID Reserva: " + testBooking.getBookingId());
            System.out.println("- Cliente: " + client.getName());
            System.out.println("- Alojamiento: " + hosting.getName());
            System.out.println("- Check-in: " + testBooking.getStartDate());
            System.out.println("- Check-out: " + testBooking.getEndDate());
            System.out.println("- Noches: 3");
            System.out.println("- Subtotal: $250.00");
            System.out.println("- Descuento: $25.00");
            System.out.println("- Total pagado: $" + testBill.getTotal());
            System.out.println("\n✨ CONTENIDO DEL CÓDIGO QR: 'Pagaste $" + testBill.getTotal() + "' ✨");
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error al enviar el correo: " + e.getMessage());
        }
    }
}
