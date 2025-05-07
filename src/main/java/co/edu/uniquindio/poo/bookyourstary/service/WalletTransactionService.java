package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.WalletTransaction;
import co.edu.uniquindio.poo.bookyourstary.repository.WalletTransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

public class WalletTransactionService {

    private final WalletTransactionRepository transactionRepository;

    public WalletTransactionService(WalletTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void registerTransaction(String type, double amount, String description) {
        WalletTransaction transaction = new WalletTransaction(LocalDateTime.now(), type, amount, description);
        transactionRepository.save(transaction);
    }

    public List<WalletTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<WalletTransaction> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }
}