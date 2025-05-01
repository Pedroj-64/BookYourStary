package co.edu.uniquindio.poo.bookyourstary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public BillService(BillRepository billRepository, EmailNotifier emailNotifier,
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

        double pricePerNight = booking.getHosting().getPricePerNight();
        int numberOfNights = (int) bookingService.calculateNumberOfNights(booking.getStartDate(), booking.getEndDate());
        double subtotal = pricePerNight * numberOfNights;

        double total = offerController.applyApplicableOffers(subtotal, numberOfNights, LocalDate.now());

        if (wallet.getBalance() < total) {
            throw new IllegalArgumentException("Saldo insuficiente en la billetera virtual.");
        }

        wallet.setBalance(wallet.getBalance() - total);

        LocalDate billDate = LocalDate.now();
        Bill bill = new Bill(null, total, billDate, client, booking, subtotal);

        billRepository.save(bill);

        sendQrEmail(bill);

        return bill;
    }

    private void sendQrEmail(Bill bill) {
        Client client = bill.getClient();
        Booking booking = bill.getBooking();

        String contenidoQR = "Reserva confirmada\nID Reserva: " + booking.getBookingId()
                + "\nID Factura: " + bill.getBillId()
                + "\nTotal: $" + bill.getTotal();

        String qrCodeBase64 = QrUtil.generateBase64Qr(contenidoQR, 300, 300);

        String emailContent = emailTemplateService.buildReservationConfirmationEmail(
                client.getName(),
                booking.getHosting().getName(),
                booking.getStartDate().toString(),
                booking.getEndDate().toString(),
                (int) bookingService.calculateNumberOfNights(booking.getStartDate(), booking.getEndDate()),

                qrCodeBase64);

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
