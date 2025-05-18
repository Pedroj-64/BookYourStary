package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.UserHeaderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    UserHeaderController controller = new UserHeaderController();

    @FXML
    void abrirCarrito(ActionEvent event) {

    }

    @FXML
    void initialize() {
        updateHeader();

    }

    private void updateHeader() {
        controller.updateUserHeader(lbl_Nombre, lbl_Saldo);
        controller.updateHeaderImageAndButton(img_Carrito, btn_Carrito);
    }

}
