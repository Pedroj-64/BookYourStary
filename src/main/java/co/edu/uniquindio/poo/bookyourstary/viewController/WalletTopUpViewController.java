package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.ViewLoader;
import co.edu.uniquindio.poo.bookyourstary.util.XmlSerializationManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Controlador para la vista de recarga de billetera virtual.
 * Permite a los usuarios recargar su billetera con diferentes métodos de pago.
 */
public class WalletTopUpViewController {

    @FXML
    private AnchorPane UserHeader0;
    
    @FXML
    private TextField txt_monto;

    private UserHeaderViewController userHeaderController;

    public void setUserHeaderController(UserHeaderViewController controller) {
        this.userHeaderController = controller;
    }

    @FXML
    void initialize() {
        // Cargar el encabezado de usuario
        ViewLoader.setContent(UserHeader0, "UserHeader");
        
        // Obtener cliente y billetera actual
        cargarDatosDeUsuario();
    }
    
    /**
     * Carga los datos del usuario actualmente conectado.
     */
    private void cargarDatosDeUsuario() {
        Object usuarioActual = MainController.getInstance().getSessionManager().getUsuarioActual();
        
        if (usuarioActual instanceof Client) {
            Client cliente = (Client) usuarioActual;
            VirtualWallet currentWallet = cliente.getVirtualWallet();
            
            if (currentWallet == null) {
                MainController.showAlert(
                    "Error", 
                    "No se encontró una billetera asociada a su cuenta.",
                    AlertType.ERROR
                );
            }
        } else {
            MainController.showAlert(
                "Acceso no permitido", 
                "Debe iniciar sesión como cliente para acceder a esta funcionalidad.",
                AlertType.WARNING
            );
        }
    }
    
    /**
     * Maneja la selección de montos predefinidos.
     * @param event Evento de acción.
     */
    @FXML
    void seleccionarMonto(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        Double monto = (Double) sourceButton.getUserData();
        
        if (monto != null) {
            txt_monto.setText(String.format("%.2f", monto));
        }
    }
    
    /**
     * Realiza la recarga de saldo a la billetera.
     * @param event Evento de acción.
     */    @FXML
    void recargarSaldo(ActionEvent event) {
        try {
            double monto = Double.parseDouble(txt_monto.getText().replace(",", "."));
            if (monto <= 0) {
                // Notificar al usuario sobre el monto inválido
                MainController.showAlert("Error", "Monto inválido", AlertType.ERROR);
                return;
            }
            
            // Obtener el cliente actual
            Object usuarioActual = MainController.getInstance().getSessionManager().getUsuarioActual();
            if (!(usuarioActual instanceof Client)) {
                MainController.showAlert("Error", "Debe iniciar sesión como cliente para realizar recargas", AlertType.ERROR);
                return;
            }
            
            Client cliente = (Client) usuarioActual;
            VirtualWallet wallet = cliente.getVirtualWallet();
            
            if (wallet == null) {
                // Si el cliente no tiene billetera, crearla
                wallet = new VirtualWallet(cliente);
                cliente.setVirtualWallet(wallet);
            }
            
            // Actualizar el saldo en el modelo
            wallet.setBalance(wallet.getBalance() + monto);
            
            // Actualizar la vista si el controlador de encabezado está disponible
            if (userHeaderController != null) {
                userHeaderController.actualizarSaldo(monto);
            }
            
            // Guardar los cambios en el sistema usando XmlSerializationManager
            XmlSerializationManager.getInstance().saveClients();
            
            // Mostrar mensaje de éxito
            MainController.showAlert("Éxito", "Se ha recargado $" + String.format("%.2f", monto) + " a su billetera.", AlertType.INFORMATION);
            
            // Regresar a la pantalla de home
            MainController.loadScene("home", 600, 400);
            
        } catch (NumberFormatException e) {
            MainController.showAlert("Error", "Por favor ingrese un monto válido", AlertType.ERROR);
        }
    }
}
