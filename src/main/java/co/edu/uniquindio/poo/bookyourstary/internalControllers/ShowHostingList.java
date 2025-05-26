package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.util.ImageHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShowHostingList {
    /**
     * Actualiza los componentes gráficos con la información de los alojamientos.
     * 
     * @param hostings      Lista de alojamientos a mostrar (máx 5)
     * @param imageViews    Lista de ImageView para las fotos (tamaño 5)
     * @param titleLabels   Lista de Label para los títulos (tamaño 5)
     * @param descLabels    Lista de Label para las descripciones (tamaño 5)
     * @param priceLabels   Lista de Label para los precios (tamaño 5)
     * @param cityLabels    Lista de Label para las ciudades (tamaño 5)
     * @param serviceLabels Lista de Label para los servicios adicionales (tamaño 5)
     */
    public static void showHostings(List<Hosting> hostings,
            List<ImageView> imageViews,
            List<Label> titleLabels,
            List<Label> descLabels,
            List<Label> priceLabels,
            List<Label> cityLabels,
            List<Label> serviceLabels) {

        // Validaciones de entrada para evitar NullPointerException
        if (imageViews == null || imageViews.isEmpty()) {
            System.err.println("Error: No se proporcionaron ImageViews para mostrar alojamientos");
            return;
        }

        if (hostings == null) {
            System.err.println("Error: Se proporcionó una lista de alojamientos nula");
            hostings = Collections.emptyList();
        }

        // Registro del estado para ayudar en la depuración
        System.out.println(
                "Mostrando " + hostings.size() + " alojamientos en " + imageViews.size() + " slots disponibles");

        // Iteramos por cada slot disponible en la interfaz
        for (int i = 0; i < imageViews.size(); i++) {
            if (i < hostings.size()) {
                Hosting hosting = hostings.get(i);
                // Cargar imagen usando ImageHandler
                Image image = ImageHandler.loadImage(hosting.getImageUrl());
                imageViews.get(i).setImage(image);

                // Establecer otros campos
                titleLabels.get(i).setText(hosting.getName());
                descLabels.get(i).setText(hosting.getDescription());
                priceLabels.get(i).setText("$" + hosting.getPricePerNight());
                cityLabels.get(i).setText(hosting.getCityName());

                // Servicios adicionales
                try {
                    String serviciosTexto = "Sin servicios";
                    if (hosting.getIncludedServices() != null && !hosting.getIncludedServices().isEmpty()) {
                        serviciosTexto = hosting.getIncludedServices().stream()
                                .map(s -> {
                                    try {
                                        return s.getName();
                                    } catch (Exception e) {
                                        return "Servicio";
                                    }
                                })
                                .filter(name -> name != null && !name.isEmpty())
                                .collect(Collectors.joining(", "));

                        if (serviciosTexto.isEmpty()) {
                            serviciosTexto = "Sin servicios";
                        }
                    }
                    serviceLabels.get(i).setText(serviciosTexto);
                } catch (Exception e) {
                    System.err.println("Error al procesar servicios para " + hosting.getName() + ": " + e.getMessage());
                    serviceLabels.get(i).setText("Sin servicios");
                }
            } else {
                // Limpiar campos para slots sin alojamiento
                imageViews.get(i).setImage(ImageHandler.loadImage(null));

                if (titleLabels != null && i < titleLabels.size()) {
                    titleLabels.get(i).setText("");
                }
                if (descLabels != null && i < descLabels.size()) {
                    descLabels.get(i).setText("");
                }
                if (priceLabels != null && i < priceLabels.size()) {
                    priceLabels.get(i).setText("");
                }
                if (cityLabels != null && i < cityLabels.size()) {
                    cityLabels.get(i).setText("");
                }
                if (serviceLabels != null && i < serviceLabels.size()) {
                    serviceLabels.get(i).setText("");
                }
            }
        }
    }
}