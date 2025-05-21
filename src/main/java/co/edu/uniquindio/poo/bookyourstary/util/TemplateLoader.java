package co.edu.uniquindio.poo.bookyourstary.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class TemplateLoader {

    public static String loadTemplate(String fileName, Map<String, String> values) {
        String fullPath = "co/edu/uniquindio/poo/bookyourstary/emailTemplates/" + fileName;
        InputStream inputStream = TemplateLoader.class.getClassLoader()
                .getResourceAsStream(fullPath);

        if (inputStream == null) {
            throw new IllegalArgumentException("No se encontró la plantilla: " + fullPath);
        }

        String template;
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            template = scanner.useDelimiter("\\A").next();
        }

        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        return template;
    }
}
