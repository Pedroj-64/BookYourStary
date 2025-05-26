package co.edu.uniquindio.poo.bookyourstary.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VirtualWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idWallet;
    private double balance;
    private Client propertyClient;

    public VirtualWallet(Client propertyClient) {
        this.idWallet = UUID.randomUUID().toString();
        this.propertyClient = propertyClient;
        this.balance = 0.0;
    }
}
