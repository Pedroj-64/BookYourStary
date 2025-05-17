package co.edu.uniquindio.poo.bookyourstary.model;

public class Admin extends User {

    public Admin(String id, String name, String phoneNumber, String email, String password) {
        super(id, name, phoneNumber, email, password);
        this.isActive=true;

    }

}
