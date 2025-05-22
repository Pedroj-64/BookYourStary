package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.App;
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
     */    public static void showHostings(List<Hosting> hostings,
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
            hostings = java.util.Collections.emptyList();
        }
        
        // Registro del estado para ayudar en la depuración
        System.out.println("Mostrando " + hostings.size() + " alojamientos en " + imageViews.size() + " slots disponibles");
        
        // Iteramos por cada slot disponible en la interfaz
        for (int i = 0; i < imageViews.size(); i++) {if (i < hostings.size()) {
                Hosting hosting = hostings.get(i);
                // Imagen
                try {
                    String imageUrl = hosting.getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Si la URL no tiene un esquema de protocolo (http:, https:, file:), añadir file:
                        if (!imageUrl.startsWith("http:") && !imageUrl.startsWith("https:") && !imageUrl.startsWith("file:")) {
                            // Comprobar si la ruta existe
                            java.io.File file = new java.io.File(imageUrl);
                            if (!file.exists()) {
                                System.err.println("Advertencia: La imagen no existe en la ruta: " + imageUrl);
                                // Usar imagen por defecto si no existe el archivo
                                imageUrl = App.class.getResource("image/FotoHotelRelleno.png").toExternalForm();
                            } else {
                                imageUrl = "file:" + imageUrl;
                            }
                        }
                        imageViews.get(i).setImage(new Image(imageUrl));
                    } else {
                        // Usar una imagen por defecto desde los recursos
                        String defaultUrl = App.class.getResource("image/FotoHotelRelleno.png").toExternalForm();
                        imageViews.get(i).setImage(new Image(defaultUrl));
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen para " + hosting.getName() + ": " + e.getMessage());
                    try {
                        // Intentar cargar la imagen por defecto desde un camino absoluto si falla el método normal
                        String defaultPath = "src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png";
                        java.io.File defaultFile = new java.io.File(defaultPath);
                        if (defaultFile.exists()) {
                            imageViews.get(i).setImage(new Image("file:" + defaultPath));
                        } else {
                            System.err.println("No se pudo encontrar la imagen por defecto en: " + defaultPath);
                            // No hacer nada, dejar la imagen anterior o vacía
                        }
                    } catch (Exception ex) {
                        System.err.println("Error crítico al cargar imagen por defecto: " + ex.getMessage());
                    }
                }
                // Título
                titleLabels.get(i).setText(hosting.getName());
                // Descripción
                descLabels.get(i).setText(hosting.getDescription());
                // Precio
                priceLabels.get(i).setText("$" + hosting.getPricePerNight());
                // Ciudad
                cityLabels.get(i).setText(hosting.getCityName());                // Servicios adicionales (como una cadena separada por comas)
                try {
                    String serviciosTexto = "Sin servicios";
                    if (hosting.getIncludedServices() != null && !hosting.getIncludedServices().isEmpty()) {
                        // Utilizamos collectors para más seguridad
                        serviciosTexto = hosting.getIncludedServices().stream()
                            .map(s -> {
                                try {
                                    return s.getName();
                                } catch (Exception e) {
                                    return "Servicio";
                                }
                            })
                            .filter(name -> name != null && !name.isEmpty())
                            .collect(java.util.stream.Collectors.joining(", "));
                        
                        if (serviciosTexto.isEmpty()) {
                            serviciosTexto = "Sin servicios";
                        }
                    }
                    serviceLabels.get(i).setText(serviciosTexto);
                } catch (Exception e) {
                    System.err.println("Error al procesar servicios para " + hosting.getName() + ": " + e.getMessage());
                    serviceLabels.get(i).setText("Sin servicios");
                }} else {
                // Si no hay alojamiento para este slot, limpia los campos
                try {
                    // Intentar usar la imagen por defecto desde recursos
                    String defaultUrl = App.class.getResource("image/FotoHotelRelleno.png").toExternalForm();
                    imageViews.get(i).setImage(new Image(defaultUrl));
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen por defecto desde recursos: " + e.getMessage());
                    try {
                        // Segundo intento con ruta absoluta
                        String defaultPath = "src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/FotoHotelRelleno.png";
                        java.io.File defaultFile = new java.io.File(defaultPath);
                        if (defaultFile.exists()) {
                            imageViews.get(i).setImage(new Image("file:" + defaultPath));
                        } else {
                            System.err.println("No se pudo encontrar la imagen por defecto en: " + defaultPath);
                            // Limpiar la imagen actual para evitar mostrar una imagen incorrecta
                            imageViews.get(i).setImage(null);
                        }
                    } catch (Exception ex) {
                        System.err.println("Error crítico al cargar imagen por defecto: " + ex.getMessage());
                        imageViews.get(i).setImage(null);
                    }
                }
                
                // Limpiar todas las etiquetas para este slot vacío
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