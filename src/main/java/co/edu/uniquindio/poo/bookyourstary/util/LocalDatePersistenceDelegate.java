package co.edu.uniquindio.poo.bookyourstary.util;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.time.LocalDate;

/**
 * Clase para manejar la serialización de objetos LocalDate en XML
 */
public class LocalDatePersistenceDelegate extends DefaultPersistenceDelegate {
    @Override
    protected Expression instantiate(Object oldInstance, Encoder out) {
        LocalDate date = (LocalDate) oldInstance;
        return new Expression(oldInstance, LocalDate.class, "of",
                new Object[]{date.getYear(), date.getMonthValue(), date.getDayOfMonth()});
    }

    @Override
    protected boolean mutatesTo(Object oldInstance, Object newInstance) {
        return oldInstance == null ? newInstance == null : oldInstance.equals(newInstance);
    }
}
