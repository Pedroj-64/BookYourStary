package co.edu.uniquindio.poo.bookyourstary.model;

import lombok.Getter;

@Getter
public class City {

    private final String name;
    private final String country;
    private final String departament;

    public City(String name, String country, String departament) {
        this.name = name;
        this.country = country;
        this.departament = departament;
    }

    @Override
    public String toString() {
        return name;
    }

}
