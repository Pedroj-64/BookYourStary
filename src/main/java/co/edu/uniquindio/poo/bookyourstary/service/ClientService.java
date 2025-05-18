package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.*;
import co.edu.uniquindio.poo.bookyourstary.repository.ClientRepository;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;

import java.time.LocalDate;
import java.util.List;

public class ClientService {

    private final ClientRepository clientRepository;
    private final VirtualWalletService virtualWalletService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    private static ClientService instance;

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService(ClientRepository.getInstance(),
                VirtualWalletService.getInstance(),
                BookingService.getInstance(),
                ReviewService.getInstance());
        }
        return instance;
    }

    private ClientService(ClientRepository clientRepository,
                         VirtualWalletService virtualWalletService,
                         BookingService bookingService,
                         ReviewService reviewService) {
        this.clientRepository = clientRepository;
        this.virtualWalletService = virtualWalletService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
    }


    public void registerClient(Client client, String password) {
        if (clientRepository.findById(client.getId()).isPresent()) {
            throw new IllegalArgumentException("Cliente con ID " + client.getId() + " ya existe.");
        }
        String encryptedPassword = PasswordUtil.hashPassword(password);
        client.setPassword(encryptedPassword);

        VirtualWallet wallet = virtualWalletService.createWalletForClient(client);
        client.setVirtualWallet(wallet);

        clientRepository.save(client);
    }

    public Client getClient(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public void updateClient(Client client) {
        if (!clientRepository.findById(client.getId()).isPresent()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        clientRepository.save(client);
    }

    public void deleteClient(String clientId) {
        clientRepository.delete(clientId);
    }

    public boolean verifyPassword(Client client, String password) {
        return PasswordUtil.verifyPassword(password, client.getPassword());
    }

    public double getWalletBalance(String clientId) {
        Client client = getClient(clientId);
        return virtualWalletService.getBalance(client.getVirtualWallet().getIdWallet());
    }

    public void topUpWallet(String clientId, double amount) {
        Client client = getClient(clientId);
        virtualWalletService.topUpWallet(client.getVirtualWallet().getIdWallet(), amount);
    }

    public Booking reserveHosting(String clientId, Hosting hosting,
                                  LocalDate startDate, LocalDate endDate, int guests) {
        Client client = getClient(clientId);
        return bookingService.createBooking(client, hosting, startDate, endDate, guests);
    }

    public void confirmBooking(String bookingId) {
        bookingService.confirmBooking(bookingId);
    }

    public void cancelBooking(String bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    public List<Booking> getClientBookings(String clientId) {
        return bookingService.getBookingsByClient(clientId);
    }

    public Review createReview(String clientId, String hostingId, int score, String comment) {
        return reviewService.createReview(clientId, hostingId, score, comment);
    }

    public List<Review> getClientReviews(String clientId) {
        return reviewService.findAll().stream()
                .filter(review -> review.getUserId().equals(clientId))
                .toList();
    }

    public void deleteReview(String reviewId) {
        reviewService.deleteReview(reviewId);
    }

    public List<Review> getReviewsForHosting(String hostingId) {
        return reviewService.findByHosting(hostingId);
    }

    public double getAverageScoreForHosting(String hostingId) {
        return reviewService.getAverageScore(hostingId);
    }
}
