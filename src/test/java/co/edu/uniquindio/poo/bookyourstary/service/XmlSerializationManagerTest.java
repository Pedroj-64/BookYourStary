package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;
import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class XmlSerializationManagerTest {
    private XmlSerializationManager xmlManager;

    @BeforeEach
    void setUp() {
        xmlManager = XmlSerializationManager.getInstance();
    }

    @Test
    void testSaveAndLoadAllData() {
        // Prueba simple: solo verifica que los métodos existen y no lanzan excepción si no hay datos
        assertDoesNotThrow(() -> {
            xmlManager.saveAllData();
        });
    }

    @Test
    void testBackupDirectoryExists() {
        xmlManager.saveAllData();
        File backupDir = new File("src/main/resources/data/backup");
        assertTrue(backupDir.exists() && backupDir.isDirectory());
    }
}
