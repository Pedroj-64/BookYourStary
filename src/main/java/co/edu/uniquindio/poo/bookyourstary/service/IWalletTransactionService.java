package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.model.WalletTransaction;
import java.util.List;

public interface IWalletTransactionService {
    /**
     * Registra una nueva transacción
     */
    void registerTransaction(String type, double amount, String description);

    /**
     * Obtiene el historial de transacciones de una billetera
     */
    List<WalletTransaction> getTransactionHistory(VirtualWallet wallet);

    /**
     * Obtiene todas las transacciones
     */
    List<WalletTransaction> getAllTransactions();

    /**
     * Elimina todas las transacciones
     */
    void clearAll();
}
