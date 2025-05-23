package co.edu.uniquindio.poo.bookyourstary.util;

import co.edu.uniquindio.poo.bookyourstary.App;
import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageHandler {
    private static final String IMAGE_PATH = "co/edu/uniquindio/poo/bookyourstary/image/";
    private static final String DEFAULT_IMAGE = "FotoHotelRelleno.png";

    /**
     * Copia una imagen al directorio de imágenes del proyecto y devuelve la ruta relativa.
     * La imagen copiada se guardará en el directorio de recursos del proyecto con un nombre único.
     * 
     * @param sourceImagePath Ruta absoluta a la imagen a copiar
     * @return Ruta relativa de la imagen copiada (src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/...)
     */
    public static String copyImageToProject(String sourceImagePath) {
        try {
            if (sourceImagePath == null || sourceImagePath.isEmpty()) {
                return getDefaultImagePath();
            }

            File sourceFile = new File(sourceImagePath);
            if (!sourceFile.exists()) {
                System.err.println("La imagen origen no existe: " + sourceImagePath);
                return getDefaultImagePath();
            }

            // Crear el directorio si no existe
            Path targetDir = Paths.get(IMAGE_PATH);
            Files.createDirectories(targetDir);

            // Generar un nombre único para la imagen
            String extension = getFileExtension(sourceImagePath);
            String fileName = generateUniqueFileName(extension);
            Path targetPath = targetDir.resolve(fileName);

            // Copiar el archivo
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Imagen copiada exitosamente a: " + targetPath.toString());

            return IMAGE_PATH + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return getDefaultImagePath();
        }
    }

    /**
     * Carga una imagen para mostrar en la UI.
     * Si la imagen no existe o hay error, carga la imagen por defecto.
     * La ruta puede ser:
     * - Absoluta: file:/C:/ruta/a/imagen.jpg
     * - Relativa al proyecto: src/main/resources/co/edu/uniquindio/poo/bookyourstary/image/imagen.jpg
     * - URL: http://... o https://...
     */
    public static Image loadImage(String imagePath) {
        try {
            if (imagePath == null || imagePath.isEmpty()) {
                return loadDefaultImage();
            }

            // Si es una URL externa, cargarla directamente
            if (imagePath.startsWith("http:") || imagePath.startsWith("https:")) {
                return new Image(imagePath);
            }
            
            // Si es una ruta absoluta con file:, usarla directamente
            if (imagePath.startsWith("file:")) {
                return new Image(imagePath);
            }

            // Primero intenta cargar la imagen desde el directorio de imágenes
            URL resourceUrl = App.class.getResource("/co/edu/uniquindio/poo/bookyourstary/image/" + imagePath);
            if (resourceUrl != null) {
                return new Image(resourceUrl.toExternalForm());
            }
            
            // Si no se encuentra en el directorio de imágenes, intenta la ruta proporcionada
            resourceUrl = App.class.getResource("/" + imagePath);
            if (resourceUrl != null) {
                return new Image(resourceUrl.toExternalForm());
            }
            
            System.err.println("No se pudo encontrar la imagen: " + imagePath);
            return loadDefaultImage();
        } catch (Exception e) {
            System.err.println("Error cargando imagen '" + imagePath + "': " + e.getMessage());
            e.printStackTrace();
            return loadDefaultImage();
        }
    }

    private static Image loadDefaultImage() {
        try {
            URL resourceUrl = App.class.getResource("/co/edu/uniquindio/poo/bookyourstary/image/" + DEFAULT_IMAGE);
            if (resourceUrl != null) {
                return new Image(resourceUrl.toExternalForm());
            }
            throw new IOException("No se pudo encontrar la imagen por defecto");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getDefaultImagePath() {
        return IMAGE_PATH + DEFAULT_IMAGE;
    }

    private static String getFileExtension(String path) {
        int lastDot = path.lastIndexOf('.');
        if (lastDot == -1) return "";
        return path.substring(lastDot);
    }

    private static String generateUniqueFileName(String extension) {
        return "hosting_" + System.currentTimeMillis() + extension;
    }
}
