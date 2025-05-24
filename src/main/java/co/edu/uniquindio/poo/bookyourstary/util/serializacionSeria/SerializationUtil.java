package co.edu.uniquindio.poo.bookyourstary.util.serializacionSeria;

import java.io.*;

public class SerializationUtil {
    
    private static SerializationUtil instance;
    private static final String SERIALIZE_PATH = "src/main/resources/data/serialized/";
    
    private SerializationUtil() {
        // Crear el directorio si no existe
        ensureDirectoryExists();
    }
    
    public static SerializationUtil getInstance() {
        if (instance == null) {
            instance = new SerializationUtil();
        }
        return instance;
    }
    
    private void ensureDirectoryExists() {
        File directory = new File(SERIALIZE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void serialize(Object obj, String fileName) throws IOException {
        ensureDirectoryExists();
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(SERIALIZE_PATH + fileName + ".ser"))) {
            out.writeObject(obj);
        }
    }
    
    public Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        ensureDirectoryExists();
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(SERIALIZE_PATH + fileName + ".ser"))) {
            return in.readObject();
        }
    }
}
