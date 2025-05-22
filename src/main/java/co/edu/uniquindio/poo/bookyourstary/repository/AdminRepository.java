package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Admin;

public class AdminRepository {
    private final List<Admin> admins;
    private static AdminRepository instance;

    private AdminRepository() {
        this.admins = new LinkedList<>();
    }

    public static AdminRepository getInstance() {
        if (instance == null) {
            instance = new AdminRepository();
        }
        return instance;
    }

    public void saveAdmin(Admin admin) {
        if (findByEmail(admin.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Admin with email " + admin.getEmail() + " already exists.");
        }
        admins.add(admin);
    }

    public Optional<Admin> findByEmail(String email) {
        return admins.stream()
            .filter(admin -> admin.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }

    public List<Admin> getAdmin() {
        return new LinkedList<>(admins);
    }
    
    public void clearAll() {
        admins.clear();
    }
}
