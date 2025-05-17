package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;

public class ServiceIncludedRepository {

    private final List<ServiceIncluded> services;

    private static ServiceIncludedRepository instance;

    private ServiceIncludedRepository() {
        this.services = new LinkedList<>();
    }

    public static ServiceIncludedRepository getInstance() {
        if (instance == null) {
            instance = new ServiceIncludedRepository();
        }
        return instance;
    }

    public void save(ServiceIncluded service) {
        services.add(service);
    }

    public Optional<ServiceIncluded> findById(String id) {
        return services.stream()
                .filter(service -> service.getId().equals(id))
                .findFirst();
    }

    public List<ServiceIncluded> findAll() {
        return services;
    }

    public void delete(ServiceIncluded service) {
        services.remove(service);
    }

    public Optional<ServiceIncluded> findByName(String name) {
        return services.stream()
                .filter(service -> service.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
