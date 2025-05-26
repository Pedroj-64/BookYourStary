package co.edu.uniquindio.poo.bookyourstary.service.implementService;

import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.repository.VirtualWalletRepository;

public class VirtualWalletService {

    private final VirtualWalletRepository virtualWalletRepository;
    private final WalletTransactionService walletTransactionService;

    private static VirtualWalletService instance;

    public static VirtualWalletService getInstance() {
        if (instance == null) {
            instance = new VirtualWalletService(VirtualWalletRepository.getInstance(), WalletTransactionService.getInstance());
        }
        return instance;
    }

    private VirtualWalletService(VirtualWalletRepository virtualWalletRepository, WalletTransactionService walletTransactionService) {
        this.virtualWalletRepository = virtualWalletRepository;
        this.walletTransactionService = walletTransactionService;
    }

    public VirtualWallet createWalletForClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        VirtualWallet wallet = new VirtualWallet(client);
        virtualWalletRepository.save(wallet);
        return wallet;
    }

    public void topUpWallet(String walletId, double amount) {
        VirtualWallet wallet = getWalletByIdOrThrow(walletId);
        if (amount <= 0) {
            throw new IllegalArgumentException("La cantidad a recargar debe ser mayor a cero.");
        }

        wallet.setBalance(wallet.getBalance() + amount);
        virtualWalletRepository.save(wallet);

        // Registrar la transacción
        walletTransactionService.registerTransaction("RECARGA", amount, "Recarga de saldo a la billetera");
    }

    public boolean hasSufficientBalance(String walletId, double amount) {
        VirtualWallet wallet = getWalletByIdOrThrow(walletId);
        return wallet.getBalance() >= amount;
    }

    public void deductFromWallet(String walletId, double amount) {
        VirtualWallet wallet = getWalletByIdOrThrow(walletId);

        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a descontar debe ser mayor a cero.");
        }

        if (wallet.getBalance() < amount) {
            throw new IllegalStateException("Saldo insuficiente en la billetera.");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        virtualWalletRepository.save(wallet);

        // Registrar la transacción
        walletTransactionService.registerTransaction("PAGO", amount, "Descuento de saldo de la billetera");
    }

    public double getBalance(String walletId) {
        return getWalletByIdOrThrow(walletId).getBalance();
    }

    public void deleteWallet(String walletId) {
        VirtualWallet wallet = getWalletByIdOrThrow(walletId);
        virtualWalletRepository.delete(wallet);
    }

    private VirtualWallet getWalletByIdOrThrow(String walletId) {
        return virtualWalletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la billetera con ID: " + walletId));
    }

    public Optional<VirtualWallet> findById(String walletId) {
        return virtualWalletRepository.findById(walletId);
    }

    public List<VirtualWallet> findAll() {
        return virtualWalletRepository.findAll();
    }

    public void save(VirtualWallet wallet) {
        virtualWalletRepository.save(wallet);
    }

    public void registerWallet(VirtualWallet wallet) {
        if (wallet == null) {
            throw new IllegalArgumentException("La billetera no puede ser null");
        }
        
        // Verificar si ya existe una billetera para este cliente
        if (wallet.getPropertyClient() != null && wallet.getPropertyClient().getVirtualWallet() != null) {
            String existingWalletId = wallet.getPropertyClient().getVirtualWallet().getIdWallet();
            System.out.println("Cliente ya tiene billetera registrada con ID: " + existingWalletId);
            return;
        }

        // Registrar la nueva billetera
        virtualWalletRepository.save(wallet);
        System.out.println("Nueva billetera registrada con ID: " + wallet.getIdWallet());
    }
}
