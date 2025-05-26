package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import java.util.Optional;

public interface IVirtualWalletService {
    /**
     * Crea una billetera virtual para un cliente
     */
    VirtualWallet createWalletForClient(Client client);

    /**
     * Recarga una billetera virtual
     */
    void topUpWallet(String walletId, double amount);

    /**
     * Obtiene una billetera por su ID
     */
    Optional<VirtualWallet> findWalletById(String walletId);

    /**
     * Obtiene la billetera de un cliente
     */
    Optional<VirtualWallet> findWalletByClient(Client client);

    /**
     * Verifica si una billetera tiene fondos suficientes
     */
    boolean hasSufficientFunds(String walletId, double amount);

    /**
     * Deduce fondos de una billetera
     */
    void deductFunds(String walletId, double amount);

    /**
     * Elimina todas las billeteras
     */
    void clearAll();
}
