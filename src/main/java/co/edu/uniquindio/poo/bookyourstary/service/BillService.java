package co.edu.uniquindio.poo.bookyourstary.service;

import java.time.LocalDate;

import co.edu.uniquindio.poo.bookyourstary.model.Bill;
import co.edu.uniquindio.poo.bookyourstary.model.Booking;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.repository.BillRepository;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;
import co.edu.uniquindio.poo.bookyourstary.util.QRUtils;

public class BillService {

    private final BillRepository billRepository;
    private final EmailNotifier emailNotifier;
    private final EmailTemplateService emailTemplateService;

    public BillService(BillRepository billRepository, EmailNotifier emailNotifier, EmailTemplateService emailTemplateService) {
        this.billRepository = billRepository;
        this.emailNotifier = emailNotifier;
        this.emailTemplateService = emailTemplateService;
    }

    public Bill generateBill(Booking booking) {
        double subtotal = booking.getCostPerNight() * booking.getNumberOfNights(); // o como lo manejes
        double total = subtotal; // aquí puedes aplicar impuestos, comisiones, etc.

        Client client = booking.getClient();
        LocalDate date = LocalDate.now();

        Bill bill = new Bill(null, total, date, client, booking, subtotal);
        billRepository.save(bill);

        sendConfirmationEmail(bill);

        return bill;
    }

    private void sendConfirmationEmail(Bill bill) {
        Booking booking = bill.getBooking();
        Client client = bill.getClient();

        String enlaceReserva = "https://bookyourstary.com/reserva/" + booking.getBookingId(); // o el ID que uses
        String qrCodeUrl = QRUtils.generarQRBase64(enlaceReserva);

        String html = emailTemplateService.buildReservationConfirmationEmail(
                client.getName(),
                booking.getLodging().getName(),
                booking.getStartDate().toString(),
                booking.getEndDate().toString(),
                booking.getNumberOfNights(),
                qrCodeUrl,
                enlaceReserva
        );

        emailNotifier.update(html, client.getEmail());
    }
}
