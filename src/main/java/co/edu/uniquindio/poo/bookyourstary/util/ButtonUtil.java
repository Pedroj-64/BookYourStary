package co.edu.uniquindio.poo.bookyourstary.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonUtil {
    /**
     * Cambia la acción de un botón en tiempo de ejecución.
     * 
     * @param button  El botón a modificar
     * @param handler La nueva acción a ejecutar
     */
    public static void setAction(Button button, EventHandler<ActionEvent> handler) {
        button.setOnAction(handler);
    }
}
