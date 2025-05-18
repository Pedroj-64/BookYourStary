package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class ShowHostingList {
    /**
     * Actualiza los componentes gráficos con la información de los alojamientos.
     * @param hostings Lista de alojamientos a mostrar (máx 5)
     * @param imageViews Lista de ImageView para las fotos (tamaño 5)
     * @param titleLabels Lista de Label para los títulos (tamaño 5)
     * @param descLabels Lista de Label para las descripciones (tamaño 5)
     * @param priceLabels Lista de Label para los precios (tamaño 5)
     * @param cityLabels Lista de Label para las ciudades (tamaño 5)
     * @param serviceLabels Lista de Label para los servicios adicionales (tamaño 5)
     */
    public static void showHostings(List<Hosting> hostings,
                                    List<ImageView> imageViews,
                                    List<Label> titleLabels,
                                    List<Label> descLabels,
                                    List<Label> priceLabels,
                                    List<Label> cityLabels,
                                    List<Label> serviceLabels) {
        for (int i = 0; i < imageViews.size(); i++) {
            if (i < hostings.size()) {
                Hosting hosting = hostings.get(i);
                // Imagen
                try {
                    imageViews.get(i).setImage(new Image(hosting.getImageUrl()));
                } catch (Exception e) {
                    imageViews.get(i).setImage(null); // O una imagen por defecto
                }
                // Título
                titleLabels.get(i).setText(hosting.getName());
                // Descripción
                descLabels.get(i).setText(hosting.getDescription());
                // Precio
                priceLabels.get(i).setText("$" + hosting.getPricePerNight());
                // Ciudad
                cityLabels.get(i).setText(hosting.getCityName());
                // Servicios adicionales (como una cadena separada por comas)
                serviceLabels.get(i).setText(
                    hosting.getIncludedServices() != null && !hosting.getIncludedServices().isEmpty() ?
                        hosting.getIncludedServices().stream().map(s -> s.getName()).reduce((a, b) -> a + ", " + b).orElse("") :
                        "Sin servicios"
                );
            } else {
                // Si no hay alojamiento para este slot, limpia los campos
                imageViews.get(i).setImage(null);
                titleLabels.get(i).setText("");
                descLabels.get(i).setText("");
                priceLabels.get(i).setText("");
                cityLabels.get(i).setText("");
                serviceLabels.get(i).setText("");
            }
        }
    }
}