package co.edu.uniquindio.poo.bookyourstary.service.implementService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.OfferController;
import co.edu.uniquindio.poo.bookyourstary.model.Bill;
import co.edu.uniquindio.poo.bookyourstary.model.Booking;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.repository.BillRepository;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;
import co.edu.uniquindio.poo.bookyourstary.util.QrUtil;

public class BillService {

    private final BillRepository billRepository;
    private final EmailNotifier emailNotifier;
    private final EmailTemplateService emailTemplateService;
    private final BookingService bookingService;
    private final OfferController offerController;

    private static BillService instance;

    public static BillService getInstance() {
        if (instance == null) {
            instance = new BillService(BillRepository.getInstance(),
                co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier.getInstance(),
                EmailTemplateService.getInstance(), // Correctly get instance
                BookingService.getInstance(),
                co.edu.uniquindio.poo.bookyourstary.internalControllers.OfferController.getInstance());
        }
        return instance;
    }

    private BillService(BillRepository billRepository, EmailNotifier emailNotifier,
            EmailTemplateService emailTemplateService, BookingService bookingService, OfferController offerController) {
        this.bookingService = bookingService;
        this.billRepository = billRepository;
        this.emailNotifier = emailNotifier;
        this.emailTemplateService = emailTemplateService;
        this.offerController = offerController;
    }

    public Bill generateBill(Booking booking) {
        validateBooking(booking);

        Client client = booking.getClient();
        VirtualWallet wallet = client.getVirtualWallet();

        if (wallet == null) {
            throw new IllegalStateException("El cliente no tiene una billetera virtual asociada.");
        }

        // Calculate prices and apply discounts
        double pricePerNight = booking.getHosting().getPricePerNight();
        int numberOfNights = (int) bookingService.calculateNumberOfNights(booking.getStartDate(), booking.getEndDate());
        double subtotal = pricePerNight * numberOfNights;
        double total = offerController.applyApplicableOffers(subtotal, numberOfNights, LocalDate.now());

        if (wallet.getBalance() < total) {
            throw new IllegalStateException("Saldo insuficiente en la billetera virtual.");
        }

        // Update wallet balance
        wallet.setBalance(wallet.getBalance() - total);

        // Create bill with all required data
        Bill bill = new Bill(
            UUID.randomUUID().toString(), // Fix the typo in UUID.randon
            total,
            LocalDate.now(),
            client,
            booking,
            subtotal
        );

        // Save bill before sending email
        billRepository.save(bill);

        // Send email with complete bill information
        try {
            sendQrEmail(bill);
            System.out.println("Factura generada y enviada por correo. ID: " + bill.getBillId());
        } catch (Exception e) {
            System.err.println("Error al enviar el correo de la factura: " + e.getMessage());
            e.printStackTrace();
        }

        return bill;
    }

    private void sendQrEmail(Bill bill) {
        Client client = bill.getClient();
        Booking booking = bill.getBooking();

        // Generate detailed QR content
        String contenidoQR = String.format(
            "Factura: %s\nCliente: %s\nAlojamiento: %s\nFecha: %s\nTotal: $%.2f",
            bill.getBillId(),
            client.getName(),
            booking.getHosting().getName(),
            bill.getDate(),
            bill.getTotal()
        );

        String qrCodeBase64 = QrUtil.generateBase64Qr(contenidoQR, 300, 300);

        // Build complete email content
        String emailContent = emailTemplateService.buildInvoiceEmail(bill, qrCodeBase64);

        // Log email content for debugging
        System.out.println("Enviando factura por correo a: " + client.getEmail());
        System.out.println("Contenido del QR: " + contenidoQR);

        // Send email
        emailNotifier.update(emailContent, client.getEmail());
    }

    private void validateBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula.");
        }
        if (booking.getTotalPrice() < 0) {
            throw new IllegalArgumentException("El costo por noche y el número de noches deben ser válidos.");
        }
        if (booking.getClient() == null) {
            throw new IllegalArgumentException("La reserva debe tener un cliente asociado.");
        }
    }

    public void save(Bill bill) {
        billRepository.save(bill);
    }

    public Optional<Bill> findById(String billId) {
        return billRepository.findById(billId);
    }

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public void delete(String billId) {
        billRepository.delete(billId);
    }
}
