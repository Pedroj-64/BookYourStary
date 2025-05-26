package co.edu.uniquindio.poo.bookyourstary.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utilidad para el manejo de logs en la aplicación
 */
public class LoggerConfig {

    private static boolean configured = false;

    /**
     * Configura el logger principal de la aplicación
     */
    public static void configureLogger() {
        if (configured) {
            return;
        }

        // Configurar el logger raíz
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);

        // Eliminar manejadores existentes para evitar duplicados
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        // Añadir ConsoleHandler con formato simple
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);

        configured = true;

        Logger logger = Logger.getLogger(LoggerConfig.class.getName());
        logger.info("Sistema de logs configurado correctamente");
    }

    /**
     * Obtiene un logger configurado para una clase específica
     * 
     * @param clazz la clase para la cual se requiere el logger
     * @return el logger configurado
     */
    public static Logger getLogger(Class<?> clazz) {
        configureLogger();
        return Logger.getLogger(clazz.getName());
    }
}
