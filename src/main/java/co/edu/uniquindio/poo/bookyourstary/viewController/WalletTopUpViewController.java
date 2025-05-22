package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.service.VirtualWalletService;
import co.edu.uniquindio.poo.bookyourstary.util.viewDinamic.ViewLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador para la vista de recarga de billetera virtual.
 * Permite a los usuarios recargar su billetera con diferentes métodos de pago.
 */
public class WalletTopUpViewController {

    @FXML
    private AnchorPane UserHeader0;
    
    @FXML
    private Label lbl_saldoActual;
    
    @FXML
    private Label lbl_walletId;
    
    @FXML
    private ComboBox<String> combo_metodoPago;
    
    @FXML
    private TextField txt_monto;
    
    @FXML
    private Button btn_recargar;
    
    @FXML
    private Button btn_cancelar;
    
    private final VirtualWalletService walletService = VirtualWalletService.getInstance();
    private VirtualWallet currentWallet;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    
    @FXML
    void initialize() {
        // Cargar el encabezado de usuario
        ViewLoader.setContent(UserHeader0, "UserHeader");
        
        // Configurar opciones de método de pago
        combo_metodoPago.getItems().addAll(
            "Tarjeta de Crédito",
            "Tarjeta de Débito",
            "PayPal",
            "Transferencia Bancaria"
        );
        combo_metodoPago.setValue("Tarjeta de Crédito");
        
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
            currentWallet = cliente.getVirtualWallet();
            
            if (currentWallet != null) {
                lbl_walletId.setText(currentWallet.getIdWallet());
                actualizarSaldoMostrado();
            } else {
                lbl_walletId.setText("No disponible");
                lbl_saldoActual.setText("$0.00");
                MainController.showAlert(
                    "Error", 
                    "No se encontró una billetera asociada a su cuenta.",
                    AlertType.ERROR
                );
            }
        } else {
            lbl_walletId.setText("No disponible");
            lbl_saldoActual.setText("$0.00");
            MainController.showAlert(
                "Acceso no permitido", 
                "Debe iniciar sesión como cliente para acceder a esta funcionalidad.",
                AlertType.WARNING
            );
        }
    }
    
    /**
     * Actualiza el saldo mostrado en la interfaz.
     */
    private void actualizarSaldoMostrado() {
        if (currentWallet != null) {
            lbl_saldoActual.setText(currencyFormat.format(currentWallet.getBalance()));
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
     */
    @FXML
    void recargarSaldo(ActionEvent event) {
        try {
            // Validar que haya una billetera actual
            if (currentWallet == null) {
                MainController.showAlert(
                    "Error", 
                    "No hay una billetera disponible para recargar.",
                    AlertType.ERROR
                );
                return;
            }
            
            // Validar que se haya seleccionado un método de pago
            if (combo_metodoPago.getValue() == null || combo_metodoPago.getValue().isEmpty()) {
                MainController.showAlert(
                    "Advertencia", 
                    "Por favor seleccione un método de pago.",
                    AlertType.WARNING
                );
                return;
            }
            
            // Validar y obtener el monto
            String montoTexto = txt_monto.getText().trim().replace(",", ".");
            if (montoTexto.isEmpty()) {
                MainController.showAlert(
                    "Advertencia", 
                    "Por favor ingrese un monto para recargar.",
                    AlertType.WARNING
                );
                return;
            }
            
            double monto;
            try {
                monto = Double.parseDouble(montoTexto);
            } catch (NumberFormatException e) {
                MainController.showAlert(
                    "Error", 
                    "El monto ingresado no es válido. Por favor ingrese un número.",
                    AlertType.ERROR
                );
                return;
            }
            
            // Validar que el monto sea positivo
            if (monto <= 0) {
                MainController.showAlert(
                    "Advertencia", 
                    "El monto a recargar debe ser mayor a cero.",
                    AlertType.WARNING
                );
                return;
            }
            
            // Realizar la recarga
            walletService.topUpWallet(currentWallet.getIdWallet(), monto);
            
            // Actualizar UI
            actualizarSaldoMostrado();
            
            // Mostrar mensaje de éxito
            MainController.showAlert(
                "Éxito", 
                "Se ha recargado " + currencyFormat.format(monto) + " a su billetera.",
                AlertType.INFORMATION
            );
            
            // Limpiar campo de monto
            txt_monto.clear();
            
        } catch (Exception e) {
            MainController.showAlert(
                "Error", 
                "Ha ocurrido un error al recargar la billetera: " + e.getMessage(),
                AlertType.ERROR
            );
        }
    }
    
    /**
     * Cancela la operación y regresa a la pantalla principal.
     * @param event Evento de acción.
     */
    @FXML
    void cancelar(ActionEvent event) {
        MainController.loadScene("home", 900, 600);
    }
}
