package co.edu.uniquindio.poo.bookyourstary.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una ciudad en el sistema.
 * Se añade NoArgsConstructor para compatibilidad con la serialización XML.
 */
@Getter
@NoArgsConstructor
public class City implements Serializable {

    private String name;
    private String country;
    private String departament;

    public City(String name, String country, String departament) {
        this.name = name;
        this.country = country;
        this.departament = departament;
    }

    // Setters necesarios para la serialización XML
    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Override
    public String toString() {
        return name;
    }
}
