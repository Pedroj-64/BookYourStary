package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;

public class ServiceIncludedRepository {

    private final List<ServiceIncluded> services;

    public ServiceIncludedRepository() {
        this.services = new LinkedList<>();
    }

    // Guardar un nuevo servicio
    public void save(ServiceIncluded service) {
        services.add(service);
    }

    // Buscar un servicio por ID
    public Optional<ServiceIncluded> findById(String id) {
        return services.stream()
                .filter(service -> service.getId().equals(id))
                .findFirst();
    }

    // Obtener todos los servicios
    public List<ServiceIncluded> findAll() {
        return services;
    }

    // Eliminar un servicio
    public void delete(ServiceIncluded service) {
        services.remove(service);
    }

    // Buscar un servicio por nombre
    public Optional<ServiceIncluded> findByName(String name) {
        return services.stream()
                .filter(service -> service.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
