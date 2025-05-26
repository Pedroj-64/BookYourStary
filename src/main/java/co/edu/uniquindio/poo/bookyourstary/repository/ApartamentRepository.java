package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Apartament;

public class ApartamentRepository {

    private final List<Apartament> apartaments;
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

    public List<Apartament> findAll() {
        return new LinkedList<>(apartaments);
    }

    /**
     * Actualiza un apartamento existente en la lista, reemplazando el que tiene el
     * mismo nombre.
     */
    public void update(Apartament apartament) {
        for (int i = 0; i < apartaments.size(); i++) {
            if (apartaments.get(i).getName().equals(apartament.getName())) {
                apartaments.set(i, apartament);
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró el apartamento a actualizar: " + apartament.getName());
    }

    public void clearAll() {
        apartaments.clear();
    }
}
