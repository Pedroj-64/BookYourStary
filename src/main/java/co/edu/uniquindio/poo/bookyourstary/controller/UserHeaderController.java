package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Admin;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserHeaderController {

    public void updateUserHeader(Label nameLabel, Label balanceLabel) {
        Object user = MainController.getInstance().getSessionManager().getUsuarioActual();
        if (user instanceof Client) {
            Client client = (Client) user;
            nameLabel.setText(client.getName());
            balanceLabel.setText("$" + client.getVirtualWallet().getBalance());
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            nameLabel.setText(admin.getName());
            balanceLabel.setText("$9999");
        } else {
            nameLabel.setText("Invitado");
            balanceLabel.setText("$0");
        }
    }

    public void updateHeaderImageAndButton(ImageView imageView, Button button) {
        Object user = MainController.getInstance().getSessionManager().getUsuarioActual();
        if (user instanceof Client) {
            imageView.setImage(new Image("/co/edu/uniquindio/poo/bookyourstary/image/carrito.png"));
            button.setOnAction(e -> {MainController.loadScene("null",0,0);});
        } else if (user instanceof Admin) {
            imageView.setImage(new Image("/co/edu/uniquindio/poo/bookyourstary/image/tuerquita.png"));
            button.setOnAction(e -> {MainController.loadScene("null",0,0);});
        } else {
            imageView.setImage(null);
        }
    }

}
