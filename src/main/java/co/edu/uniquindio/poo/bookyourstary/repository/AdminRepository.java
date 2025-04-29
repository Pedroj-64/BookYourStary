package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Admin;

public class AdminRepository {

    private Admin admin;

    public AdminRepository(){

    }

    public void saveAdmin(Admin admin) {
        this.admin = admin;
    }

    public Optional<Admin> getAdmin() {
        return Optional.ofNullable(admin);
    }

    public void deleteAdmin() {
        this.admin = null;
    }

    public boolean isAdminRegistered(){
        return admin != null;
    }

}
