package co.edu.uniquindio.poo.bookyourstary.config.mapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class FestivosColombia {

    public static Set<LocalDate> obtenerFestivos(int año) {
        Set<LocalDate> festivos = new HashSet<>();

        // Festivos fijos
        festivos.add(LocalDate.of(año, Month.JANUARY, 1));    // Año Nuevo
        festivos.add(LocalDate.of(año, Month.MAY, 1));         // Día del Trabajo
        festivos.add(LocalDate.of(año, Month.JULY, 20));       // Independencia
        festivos.add(LocalDate.of(año, Month.AUGUST, 7));      // Batalla de Boyacá
        festivos.add(LocalDate.of(año, Month.DECEMBER, 8));    // Inmaculada Concepción
        festivos.add(LocalDate.of(año, Month.DECEMBER, 25));   // Navidad

        // Festivos que se trasladan al lunes según la Ley Emiliani
        festivos.add(trasladarALunes(LocalDate.of(año, Month.JANUARY, 6))); // Reyes
        festivos.add(trasladarALunes(LocalDate.of(año, Month.MARCH, 19)));  // San José
        festivos.add(trasladarALunes(LocalDate.of(año, Month.JUNE, 29)));   // San Pedro y San Pablo
        festivos.add(trasladarALunes(LocalDate.of(año, Month.AUGUST, 15))); // Asunción
        festivos.add(trasladarALunes(LocalDate.of(año, Month.OCTOBER, 12)));// Día de la Raza
        festivos.add(trasladarALunes(LocalDate.of(año, Month.NOVEMBER, 1)));// Todos los Santos
        festivos.add(trasladarALunes(LocalDate.of(año, Month.NOVEMBER, 11)));// Independencia de Cartagena

        return festivos;
    }

    private static LocalDate trasladarALunes(LocalDate fecha) {
        while (fecha.getDayOfWeek() != DayOfWeek.MONDAY) {
            fecha = fecha.plusDays(1);
        }
        return fecha;
    }
}

