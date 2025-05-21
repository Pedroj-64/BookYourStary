package co.edu.uniquindio.poo.bookyourstary.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que representa un servicio incluido en un alojamiento.
 * Se añade NoArgsConstructor para compatibilidad con la serialización XML.
 */
@Getter
@Setter
@NoArgsConstructor
public class ServiceIncluded implements Serializable {

    private String id;
    private String name;
    private String description;

    public ServiceIncluded(String id, String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }
}
