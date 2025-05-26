package co.edu.uniquindio.poo.bookyourstary.service.observer;

public interface Subject {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(String message, String email);
}
