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
            double monto = Double.parseDouble(txt_monto.getText().replace(",", "."));
            if (monto <= 0) {
                lbl_saldoActual.setText("Monto inválido");
                return;
            }
            // Aquí deberías llamar a tu servicio/modelo para recargar la billetera
            // Simulación:
            double saldoActual = Double.parseDouble(lbl_saldoActual.getText().replace("$", "").replace(",", "").replace(" ", ""));
            saldoActual += monto;
            lbl_saldoActual.setText(String.format("$%.2f", saldoActual));
            txt_monto.clear();
        } catch (Exception e) {
            lbl_saldoActual.setText("Error en recarga");
        }
    }

    /**
     * Cancela la operación y regresa a la pantalla principal.
     * @param event Evento de acción.
     */
    @FXML
    void cancelar(ActionEvent event) {
        // Cerrar ventana o volver atrás
        ((javafx.stage.Stage) btn_cancelar.getScene().getWindow()).close();
    }
}
