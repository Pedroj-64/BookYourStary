package co.edu.uniquindio.poo.bookyourstary.util;

import java.time.LocalDate;

/**
 * Helper class to group filter data for hostings search.
 */
public class FilterData {
    public String ciudad, tipo;
    public Double minPrecio, maxPrecio;
    public Integer numHuespedes;
    public Boolean wifi, piscina, desayuno;
    public LocalDate fechaInicio, fechaFin;

    public FilterData(String ciudad, String tipo, Double minPrecio, Double maxPrecio, Integer numHuespedes,
            Boolean wifi, Boolean piscina, Boolean desayuno, LocalDate fechaInicio, LocalDate fechaFin) {
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.minPrecio = minPrecio;
        this.maxPrecio = maxPrecio;
        this.numHuespedes = numHuespedes;
        this.wifi = wifi;
        this.piscina = piscina;
        this.desayuno = desayuno;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}
