package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.repository.*;
import co.edu.uniquindio.poo.bookyourstary.service.*;
import co.edu.uniquindio.poo.bookyourstary.service.observer.EmailNotifier;

/**
 * Utilidad para crear un cliente completamente funcional con todos los servicios y repositorios necesarios.
 */
public class CreateClient {

    /**
     * Crea un cliente con todos los servicios y repositorios necesarios para su funcionamiento.
     * No activa el cliente ni realiza lógica de verificación de email.
     * @param id ID del cliente
     * @param name Nombre del cliente
     * @param phoneNumber Teléfono
     * @param email Correo electrónico
     * @param password Contraseña en texto plano
     * @return Cliente registrado y listo para usarse (no activado)
     */
    public static Client createFullClient(String id, String name, String phoneNumber, String email, String password) {
        // Repositorios y controladores singleton
        ClientRepository clientRepository = MainController.getInstance().getClientRepository();
        VirtualWalletRepository virtualWalletRepository = MainController.getInstance().getVirtualWalletRepository();
        WalletTransactionRepository walletTransactionRepository = MainController.getInstance().getWalletTransactionRepository();
        BookingRepository bookingRepository = MainController.getInstance().getBookingRepository();
        BillRepository billRepository = MainController.getInstance().getBillRepository();
        ReviewRepository reviewRepository = MainController.getInstance().getReviewRepository();
        EmailNotifier emailNotifier = MainController.getInstance().getEmailNotifier();
        OfferController offerController = MainController.getInstance().getOfferController();
        // Servicios
        WalletTransactionService walletTransactionService = new WalletTransactionService(walletTransactionRepository);
        VirtualWalletService virtualWalletService = new VirtualWalletService(virtualWalletRepository, walletTransactionService);
        ReviewService reviewService = new ReviewService(reviewRepository);
        EmailTemplateService emailTemplateService = new EmailTemplateService();
        // BookingService se crea primero, BillService después y se pasa bookingService como dependencia
        BookingService bookingService = new BookingService(bookingRepository, virtualWalletService, walletTransactionService, null);
        BillService billService = new BillService(billRepository, emailNotifier, emailTemplateService, bookingService, offerController);
        // ClientService
        ClientService clientService = new ClientService(clientRepository, virtualWalletService, bookingService, reviewService);
        // Crear y registrar el cliente (sin activar ni lógica de email)
        Client client = new Client(id, name, phoneNumber, email, password);
        clientService.registerClient(client, password);
        return client;
    }
}
