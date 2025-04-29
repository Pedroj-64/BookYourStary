package co.edu.uniquindio.poo.bookyourstary.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualWallet {

    private final String idWallet;
    private double balance;
    private final Client propertyClient;

    public VirtualWallet(Client propertyClient){
        this.idWallet = UUID.randomUUID().toString();
        this.propertyClient = propertyClient;
        this.balance = 0.0;
    }
}
