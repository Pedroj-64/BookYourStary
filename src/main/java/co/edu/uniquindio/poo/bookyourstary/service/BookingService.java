package co.edu.uniquindio.poo.bookyourstary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uniquindio.poo.bookyourstary.model.Bill;
import co.edu.uniquindio.poo.bookyourstary.model.Booking;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.model.enums.BookingState;
import co.edu.uniquindio.poo.bookyourstary.repository.BookingRepository;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;

public class BookingService {

    private final BookingRepository bookingRepository;
    private final VirtualWalletService virtualWalletService;
    private final WalletTransactionService walletTransactionService;

    // private final BillService billService; // Removed to break circular dependency

    private static BookingService instance;

    public static BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService(BookingRepository.getInstance(),
                VirtualWalletService.getInstance(),
                WalletTransactionService.getInstance()); // BillService.getInstance() removed
        }
        return instance;
    }

    private BookingService(BookingRepository bookingRepository, VirtualWalletService virtualWalletService,
                          WalletTransactionService walletTransactionService) { // BillService billService removed
        this.bookingRepository = bookingRepository;
        this.virtualWalletService = virtualWalletService;
        this.walletTransactionService = walletTransactionService;
        // this.billService = billService; // Removed
    }
    

    public Booking createBooking(Client client, Hosting hosting, LocalDate startDate, LocalDate endDate, int numberOfGuests) {
        validateDates(startDate, endDate);
        validateGuestCapacity(hosting, numberOfGuests);

        // Calcular precio base
        double costPerNight = hosting.getPricePerNight();
        long nights = startDate.until(endDate).getDays();
        double basePrice = costPerNight * nights;
        
        // Aplicar ofertas disponibles al precio
        double totalPrice = co.edu.uniquindio.poo.bookyourstary.internalControllers.OfferController
                .getInstance()
                .applyApplicableOffers(basePrice, (int)nights, startDate);

        String bookingId = UUID.randomUUID().toString();

        Booking booking = new Booking(
            bookingId,
            client,
            hosting,
            startDate,
            endDate,
            numberOfGuests,
            totalPrice,
            BookingState.PENDING
        );

        bookingRepository.save(booking);
        XmlSerializationManager.getInstance().saveAllData(); // Guardar cambios en XML
        return booking;
    }

    public Optional<Booking> findById(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public void deleteBooking(String bookingId) {
        bookingRepository.delete(bookingId);
    }

    public void updateBookingState(String bookingId, BookingState newState) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        booking.setBookingState(newState);
    }

    public void confirmBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        Client client = booking.getClient();
        if (client == null) {
            throw new IllegalStateException("La reserva no tiene un cliente asociado.");
        }

        // Acceder directamente a la wallet del cliente
        if (client.getVirtualWallet() == null) {
            throw new IllegalStateException("El cliente no tiene una billetera virtual asociada.");
        }

        double totalPrice = booking.getTotalPrice();
        
        // Verificar el balance directamente en la wallet del cliente
        if (client.getVirtualWallet().getBalance() < totalPrice) {
            throw new IllegalStateException("Fondos insuficientes para confirmar la reserva.");
        }
        
        // Deducir el pago directamente de la wallet del cliente
        client.getVirtualWallet().setBalance(client.getVirtualWallet().getBalance() - totalPrice);
        
        // Registrar la transacción
        walletTransactionService.registerTransaction("RESERVA", totalPrice, "Pago por reserva: " + bookingId);

        // Actualizar estado de la reserva
        updateBookingState(bookingId, BookingState.CONFIRMED);
        
        // Generar factura
        Bill bill = BillService.getInstance().generateBill(booking);
        
        // Guardar todos los cambios
        XmlSerializationManager.getInstance().saveAllData();
        
        System.out.println("Reserva confirmada exitosamente. Factura generada: " + bill.getBillId());
    }

    public void cancelBooking(String bookingId) {
        updateBookingState(bookingId, BookingState.CANCELLED);
    }

    public List<Booking> getBookingsByClient(String clientId) {
        return bookingRepository.findByClientId(clientId);
    }

    public List<Booking> getBookingsByHosting(String hostingName) {
        return bookingRepository.findByHostingName(hostingName);
    }

    public boolean isHostingAvailable(String hostingName, LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = bookingRepository.findByHostingName(hostingName);
        return bookings.stream().noneMatch(booking ->
                (startDate.isBefore(booking.getEndDate()) && endDate.isAfter(booking.getStartDate())));
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la de fin.");
        }
        if (start.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser futura.");
        }
    }

    private void validateGuestCapacity(Hosting hosting, int guests) {
        if (guests <= 0 || guests > hosting.getMaxGuests()) {
            throw new IllegalArgumentException("Cantidad de huéspedes inválida para este alojamiento.");
        }
    }

    public long calculateNumberOfNights(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
    
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }
    
        return startDate.until(endDate).getDays(); 
    }
    
    /**
     * Elimina todas las reservas del sistema.
     */
    public void clearAll() {
        bookingRepository.clearAll();
        XmlSerializationManager.getInstance().saveAllData();
    }
}
