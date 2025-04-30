package co.edu.uniquindio.poo.bookyourstary.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client extends User {

    public Client(String id, String name, String phoneNumber, String email, String password) {
        super(id, name, phoneNumber, email, password);

    }

}
