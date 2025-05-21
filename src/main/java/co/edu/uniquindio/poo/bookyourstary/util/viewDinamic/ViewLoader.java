package co.edu.uniquindio.poo.bookyourstary.util.viewDinamic;

import co.edu.uniquindio.poo.bookyourstary.App;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilidad para cargar vistas dinámicas en contenedores AnchorPane
 */
public class ViewLoader {
    
    private static final Logger LOGGER = Logger.getLogger(ViewLoader.class.getName());

    /**
     * Carga y establece el contenido de un archivo FXML en un AnchorPane
     * @param container El AnchorPane donde se cargará el contenido
     * @param fxmlPath El nombre del archivo FXML (sin extensión)
     */    public static void setContent(AnchorPane container, String fxmlPath) {
        if (container == null) {
            LOGGER.severe("El contenedor AnchorPane es null");
            return;
        }
        
        try {
            // Elimina cualquier extensión .fxml que pudiera tener el nombre
            String normalizedPath = fxmlPath.replace(".fxml", "");
            LOGGER.info("Cargando FXML: " + normalizedPath);
            
            // Construir la ruta completa
            String fullPath = "/" + normalizedPath + ".fxml";
            if (!normalizedPath.startsWith("co/edu/uniquindio/poo/bookyourstary/")) {
                fullPath = "/co/edu/uniquindio/poo/bookyourstary/" + normalizedPath + ".fxml";
            }
            
            LOGGER.info("Ruta completa: " + fullPath);
            
            // Utilizar FXMLLoader directamente para más control
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fullPath));
            if (loader.getLocation() == null) {
                LOGGER.severe("No se pudo encontrar el recurso FXML: " + fullPath);
                MainController.showAlert(
                    "Error de carga", 
                    "No se pudo encontrar el archivo: " + fullPath, 
                    javafx.scene.control.Alert.AlertType.ERROR
                );
                return;
            }
            
            Parent content = loader.load();
            
            // Limpiar cualquier contenido anterior
            container.getChildren().clear();
            
            // Establecer el nuevo contenido
            container.getChildren().add(content);
            
            // Ajustar el tamaño del contenido al contenedor
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            
            LOGGER.info("FXML cargado exitosamente: " + normalizedPath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar FXML: " + fxmlPath, e);
            
            MainController.showAlert(
                "Error al cargar vista",
                "No se pudo cargar el archivo FXML: " + fxmlPath + "\n" + e.getMessage(),
                javafx.scene.control.Alert.AlertType.ERROR
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar FXML: " + fxmlPath, e);
            
            MainController.showAlert(
                "Error inesperado",
                "Ocurrió un error al cargar la vista: " + e.getMessage(),
                javafx.scene.control.Alert.AlertType.ERROR
            );
        }
    }
}
