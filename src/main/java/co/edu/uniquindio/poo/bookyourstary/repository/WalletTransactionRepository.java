package co.edu.uniquindio.poo.bookyourstary.repository;

import co.edu.uniquindio.poo.bookyourstary.model.WalletTransaction;
import java.util.ArrayList;
import java.util.List;

public class WalletTransactionRepository {

    private final List<WalletTransaction> transactions = new ArrayList<>();
    private static WalletTransactionRepository instance;

    public static WalletTransactionRepository getInstance() {
        if (instance == null) {
            instance = new WalletTransactionRepository();
        }
        return instance;
    }

    private WalletTransactionRepository() {
    }

    public void save(WalletTransaction transaction) {
        transactions.add(transaction);
    }

    public List<WalletTransaction> findAll() {
        return new ArrayList<>(transactions);
    }

    public List<WalletTransaction> findByType(String type) {
        List<WalletTransaction> result = new ArrayList<>();
        for (WalletTransaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase(type)) {
                result.add(transaction);
            }
        }
        return result;
    }
}