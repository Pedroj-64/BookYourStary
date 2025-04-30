package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;
import co.edu.uniquindio.poo.bookyourstary.repository.ServiceIncludedRepository;

import java.util.List;
import java.util.Optional;

public class ServiceIncludedService {

    private final ServiceIncludedRepository serviceIncludedRepository;

    public ServiceIncludedService(ServiceIncludedRepository serviceIncludedRepository) {
        this.serviceIncludedRepository = serviceIncludedRepository;
    }

    public void saveService(String name, String description, double extraCost) {
        ServiceIncluded service = new ServiceIncluded(null, name, description);
        serviceIncludedRepository.save(service);
    }

    public Optional<ServiceIncluded> findServiceById(String id) {
        return serviceIncludedRepository.findById(id);
    }

    public Optional<ServiceIncluded> findServiceByName(String name) {
        return serviceIncludedRepository.findByName(name);
    }

    public List<ServiceIncluded> findAllServices() {
        return serviceIncludedRepository.findAll();
    }

    public void deleteService(String id) {
        ServiceIncluded service = findServiceById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el servicio con ese ID"));
        serviceIncludedRepository.delete(service);
    }
}
