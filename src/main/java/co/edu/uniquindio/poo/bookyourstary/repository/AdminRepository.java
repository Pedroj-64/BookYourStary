package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Admin;

public class AdminRepository {

    private Admin admin;
    private static AdminRepository instance;

    private AdminRepository() {

    }

    public static AdminRepository getInstance() {
        if (instance == null) {
            instance = new AdminRepository();
        }
        return instance;
    }

    public void saveAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Admin> getAdmin() {
        return admin != null ? List.of(admin) : List.of();
    }

    public void deleteAdmin() {
        this.admin = null;
    }

    public boolean isAdminRegistered() {
        return admin != null;
    }

}
