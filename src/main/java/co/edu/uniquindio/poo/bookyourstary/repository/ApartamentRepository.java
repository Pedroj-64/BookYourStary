package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Apartament;

public class ApartamentRepository {

    private final LinkedList<Apartament> apartaments;
    private static ApartamentRepository instance;

    private ApartamentRepository() {
        this.apartaments = new LinkedList<>();
    }

    public static ApartamentRepository getInstance() {
        if (instance == null) {
            instance = new ApartamentRepository();
        }
        return instance;
    }

    public void save(Apartament apartament) {
        apartaments.add(apartament);
    }

    public Optional<Apartament> findById(String name) {
        return apartaments.stream().filter(apartament -> apartament.getName().equals(name)).findFirst();
    }

    public void delete(Apartament apartament) {
        apartaments.remove(apartament);
    }

    public LinkedList<Apartament> findAll() {
        return new LinkedList<>(apartaments);
    }

}
