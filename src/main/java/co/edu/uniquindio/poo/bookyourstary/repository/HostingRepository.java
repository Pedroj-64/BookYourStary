package co.edu.uniquindio.poo.bookyourstary.repository;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.util.LinkedList;
import java.util.List;

public class HostingRepository {
    private final List<Hosting> hostings;
    private static HostingRepository instance;

    private HostingRepository() {
        this.hostings = new LinkedList<>();
    }

    public static HostingRepository getInstance() {
        if (instance == null) {
            instance = new HostingRepository();
        }
        return instance;
    }

    public void save(Hosting hosting) {
        hostings.add(hosting);
    }

    public List<Hosting> findAll() {
        return new LinkedList<>(hostings);
    }

    public void delete(Hosting hosting) {
        hostings.remove(hosting);
    }

    public void saveAll(List<Hosting> hostings2) {
        for (Hosting hosting : hostings2) {
            save(hosting);
        }
    }
    
    public void clearAll() {
        hostings.clear();
    }
}
