package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.model.enums.BookingState;
import co.edu.uniquindio.poo.bookyourstary.service.implementService.BillService;
import co.edu.uniquindio.poo.bookyourstary.util.QrUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para verificar la generación de facturas y códigos QR en
 * BillService
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
        client.setEmail("brandon40890@gmail.com"); // Correo actualizado para la prueba

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
                BookingState.PENDING);

        // Inicializar BillService usando reflexión para evitar dependencias
        billService = BillService.getInstance();
    }

    /**
     * Prueba la generación del código QR verificando su formato
     */
    @Test
    public void testQrContentFormat() throws Exception {
        // Verificar directamente la generación del QR con un monto de prueba
        String billId = "TEST-" + UUID.randomUUID().toString().substring(0, 8);
        double montoTest = 100.0;
        String expectedQrContent = String.format("Factura: %s - Total: $%.2f", billId, montoTest);
        String qrCodeBase64 = QrUtil.generateBase64Qr(expectedQrContent, 300, 300);

        // Verificar el formato del QR
        assertTrue(qrCodeBase64.startsWith("data:image/png;base64,"),
                "El QR generado debe tener el formato correcto para ser mostrado en HTML");
        assertTrue(qrCodeBase64.length() > 100,
                "El código QR debe tener un tamaño razonable");
        assertNotNull(qrCodeBase64, "El código QR no debe ser nulo");

        System.out.println("\nPrueba de generación de QR completada:");
        System.out.println("- ID Factura de prueba: " + billId);
        System.out.println("- Contenido del QR: " + expectedQrContent);
        System.out.println("- Longitud del QR Base64: " + qrCodeBase64.length() + " caracteres");
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
     * Prueba de envío de correo real con QR usando plantilla de factura
     */
    @Test
    public void testEmailWithInvoiceTemplate() {
        try {
            System.out.println("Iniciando prueba de envío de correo real con plantilla de factura...");

            // Configuramos los datos para la prueba
            LocalDate now = LocalDate.now();
            client.setEmail("brandon40890@gmail.com");
            client.setName("Brandon Test");
            hosting.setName("Apartamento de Pruebas");

            // Creamos una reserva de prueba (3 noches)
            Booking testBooking = new Booking(
                    "TEST-" + UUID.randomUUID().toString().substring(0, 8),
                    client,
                    hosting,
                    now.plusDays(3), // Check-in en 3 días
                    now.plusDays(6), // Check-out en 6 días (3 noches)
                    2, // 2 huéspedes
                    hosting.getPricePerNight() * 3, // Precio por 3 noches
                    BookingState.CONFIRMED);

            // Generamos la factura usando el servicio (esto crea la factura, genera el QR y
            // envía el correo)
            Bill generatedBill = billService.generateBill(testBooking);

            // Verificaciones y logs
            System.out.println("\n✅ CORREO ENVIADO EXITOSAMENTE ✅");
            System.out.println("\nDetalles de la factura enviada:");
            System.out.println("- ID: " + generatedBill.getBillId());
            System.out.println("- Fecha: " + generatedBill.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("- Cliente: " + client.getName());
            System.out.println("- Email: " + client.getEmail());
            System.out.println("\nDetalles de la reserva:");
            System.out.println("- ID: " + testBooking.getBookingId());
            System.out.println("- Alojamiento: " + hosting.getName());
            System.out.println("- Check-in: " + testBooking.getStartDate());
            System.out.println("- Check-out: " + testBooking.getEndDate());
            System.out.println("- Noches: " + testBooking.getStartDate().until(testBooking.getEndDate()).getDays());
            System.out.println("\nDetalles del pago:");
            System.out.println("- Precio por noche: $" + String.format("%.2f", hosting.getPricePerNight()));
            System.out.println("- Subtotal: $" + String.format("%.2f", generatedBill.getSubtotal()));
            System.out.println(
                    "- Descuento: $" + String.format("%.2f", generatedBill.getSubtotal() - generatedBill.getTotal()));
            System.out.println("- Total final: $" + String.format("%.2f", generatedBill.getTotal()));

            // Verificaciones
            assertNotNull(generatedBill, "La factura no debe ser nula");
            assertTrue(generatedBill.getTotal() > 0, "El total debe ser mayor que cero");
            assertEquals(client.getEmail(), "brandon40890@gmail.com", "El email debe coincidir");
            assertTrue(generatedBill.getSubtotal() > generatedBill.getTotal(), "Debe haber un descuento aplicado");
            assertEquals(3, testBooking.getStartDate().until(testBooking.getEndDate()).getDays(), "Deben ser 3 noches");
            assertEquals(testBooking, generatedBill.getBooking(), "La reserva debe ser la misma");
            assertEquals(client, generatedBill.getClient(), "El cliente debe ser el mismo");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error al generar factura y enviar correo: " + e.getMessage());
        }
    }
}
