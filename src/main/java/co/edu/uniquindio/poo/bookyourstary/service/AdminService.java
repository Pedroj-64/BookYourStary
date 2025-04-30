package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import co.edu.uniquindio.poo.bookyourstary.repository.AdminRepository;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Observer;
import co.edu.uniquindio.poo.bookyourstary.service.observer.Subject;
import co.edu.uniquindio.poo.bookyourstary.util.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminService implements Subject {

    private final AdminRepository adminRepository;
    private final List<Observer> observers;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        this.observers = new ArrayList<>();
    }

    public void registerAdmin(Admin admin) {
        if (adminRepository.isAdminRegistered()) {
            throw new IllegalStateException("Ya hay un administrador registrado");
        }
        adminRepository.saveAdmin(admin);
        notifyObservers("Bienvenido administrador " + admin.getName(), admin.getEmail());
    }

    public void activateAdmin() {
        Admin admin = getAdmin();
        admin.setActive(true);
        notifyObservers("Su cuenta ha sido activada", admin.getEmail());
    }

    public Admin getAdmin() {
        return adminRepository.getAdmin()
                .orElseThrow(() -> new IllegalStateException("No hay administrador registrado"));
    }

    // Métodos del patrón Subject
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message, String email) {
        for (Observer observer : observers) {
            observer.update(message, email);
        }
    }

    public boolean verifyPassword(Admin admin, String password) {
        String hashedPassword = admin.getPassword();
        return PasswordUtil.verifyPassword(password, hashedPassword);
    }
}
