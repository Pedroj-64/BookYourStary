package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletTransaction {

    private LocalDateTime date;
    private String type;
    private double amount;
    private String description;

    public WalletTransaction(LocalDateTime date, String type, double amount, String description) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return "WalletTransaction{" +
                "date=" + date +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}