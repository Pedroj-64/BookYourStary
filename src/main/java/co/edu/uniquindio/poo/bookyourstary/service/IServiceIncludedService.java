package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.ServiceIncluded;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.util.List;
import java.util.Optional;

public interface IServiceIncludedService {
    /**
     * Crea un nuevo servicio incluido
     */
    ServiceIncluded createService(String id, String name, String description);

    /**
     * Agrega un servicio a un alojamiento
     */
    void addServiceToHosting(Hosting hosting, ServiceIncluded service);

    /**
     * Elimina un servicio de un alojamiento
     */
    void removeServiceFromHosting(Hosting hosting, String serviceId);

    /**
     * Busca un servicio por su ID
     */
    Optional<ServiceIncluded> findServiceById(String serviceId);

    /**
     * Busca un servicio por su nombre
     */
    Optional<ServiceIncluded> findServiceByName(String serviceName);

    /**
     * Obtiene todos los servicios disponibles
     */
    List<ServiceIncluded> getAllServices();

    /**
     * Obtiene los servicios de un alojamiento
     */
    List<ServiceIncluded> getServicesByHosting(Hosting hosting);

    /**
     * Actualiza la descripción de un servicio
     */
    void updateServiceDescription(String serviceId, String newDescription);

    /**
     * Elimina un servicio del sistema
     */
    void deleteService(String serviceId);

    /**
     * Elimina todos los servicios
     */
    void clearAll();
}
