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
            // Ensure wallet and balance are not null to avoid NPE
            if (client.getVirtualWallet() != null) {
                balanceLabel.setText(String.format("Saldo: $%.2f", client.getVirtualWallet().getBalance()));
            } else {
                balanceLabel.setText("Saldo: N/A");
            }
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            nameLabel.setText("Admin: " + admin.getName());
            balanceLabel.setText(""); // Admins don't have a balance display
            balanceLabel.setVisible(false); // Hide balance label for admin
        } else {
            nameLabel.setText("Invitado");
            balanceLabel.setText("");
            balanceLabel.setVisible(false); // Hide balance label for guest
        }
    }

    // The updateHeaderImageAndButton method is removed.
    // Icon and button actions should be defined in their respective FXMLs (UserHeader.fxml, AdminHeader.fxml)
    // and handled by their view controllers.
}
