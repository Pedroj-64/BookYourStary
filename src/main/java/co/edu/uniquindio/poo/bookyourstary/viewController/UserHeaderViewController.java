package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.UserHeaderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;

public class UserHeaderViewController {

    @FXML
    private Button btn_Carrito;

    @FXML
    private Label lbl_Nombre;

    @FXML
    private ImageView img_Carrito;

    @FXML
    private Label lbl_Saldo;

    @FXML
    private Button btn_RecargarBilletera;

    UserHeaderController controller = new UserHeaderController();

    @FXML
    void abrirCarrito(ActionEvent event) {
        // Navigate to the screen where orders/cart are managed
        // Assuming "ManageOrder.fxml" is the correct FXML file for this.
        // Adjust width and height as needed for that scene.
        co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.loadScene("ManageOrder", 900, 600);
    }

    @FXML
    void initialize() {
        updateHeader();

    }

    private void updateHeader() {
        controller.updateUserHeader(lbl_Nombre, lbl_Saldo);
        // controller.updateHeaderImageAndButton(img_Carrito, btn_Carrito); // Removed
        // this call
    }

    @FXML
    void abrirMenuAdmin(ActionEvent event) {
        try {
            System.out.println("Intentando cargar la pantalla del MenuAdmin...");
            // Navigate to the main admin menu screen
            // La ruta correcta es "MenuAdmin" (con M mayúscula)
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.loadScene("MenuAdmin", 900, 600);
            System.out.println("Pantalla de MenuAdmin cargada exitosamente");
        } catch (Exception e) {
            System.err.println("Error al cargar MenuAdmin: " + e.getMessage());
            e.printStackTrace();
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.showAlert(
                    "Error",
                    "No se pudo abrir el menú de administrador: " + e.getMessage(),
                    javafx.scene.control.Alert.AlertType.ERROR);
        }
    }

    @FXML
    void abrirRecargarBilletera(ActionEvent event) {
        try {
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.loadScene("walletTopUp", 600, 400);
        } catch (Exception e) {
            e.printStackTrace();
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.showAlert(
                    "Error", "No se pudo abrir la ventana de recarga de billetera.", AlertType.ERROR);
        }
    }

    public void actualizarSaldo(double monto) {
        double saldoActual = Double.parseDouble(lbl_Saldo.getText().replace("Saldo: $", "").replace(",", ""));
        saldoActual += monto;
        lbl_Saldo.setText("Saldo: $" + String.format("%.2f", saldoActual));
    }

    @FXML
    void abrirEditarPerfil(ActionEvent event) {
        try {
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.loadScene("userProfileEdit", 900,
                    600);
        } catch (Exception e) {
            e.printStackTrace();
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.showAlert(
                    "Error", "No se pudo abrir la ventana de edición de perfil.", AlertType.ERROR);
        }
    }
}
