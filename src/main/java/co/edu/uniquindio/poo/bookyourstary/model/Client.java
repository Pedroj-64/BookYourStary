package co.edu.uniquindio.poo.bookyourstary.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Client extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private VirtualWallet virtualWallet;

    public Client(String id, String name, String phoneNumber, String email, String password) {
        super(id, name, phoneNumber, email, password);
    }
}
