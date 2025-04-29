package co.edu.uniquindio.poo.bookyourstary.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {

    protected String id;
    protected String name;
    protected String phoneNumber;
    protected String email;
    protected String password;
    protected boolean isActive;

    public User(String id, String name, String phoneNumber, String email, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isActive = false;
    }
    
} 


