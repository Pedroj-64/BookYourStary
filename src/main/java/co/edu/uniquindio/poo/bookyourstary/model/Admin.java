package co.edu.uniquindio.poo.bookyourstary.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Admin extends User {

    public Admin(String id, String name, String phoneNumber, String email, String password) {
        super(id, name, phoneNumber, email, password);
        this.setActive(true); // Por defecto, los administradores se crean activos
    }

    @Override
    public String toString() {
        return "Admin{id='" + getId() + "', name='" + getName() + "', email='" + getEmail() + "', active=" + isActive()
                + "}";
    }
}
