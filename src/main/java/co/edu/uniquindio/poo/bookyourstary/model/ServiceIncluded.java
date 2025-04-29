package co.edu.uniquindio.poo.bookyourstary.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceIncluded {

    private final String id;
    private final String name;
    private final String description;
    private final double extraCost;

    public ServiceIncluded(String id, String name, String description, double extraCost) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.extraCost = extraCost;
    }


}
