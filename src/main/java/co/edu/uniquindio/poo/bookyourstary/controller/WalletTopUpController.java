package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WalletTopUpController {
    public void updateWalletInfo(Label saldoLabel, Label walletIdLabel) {
        Object user = MainController.getInstance().getSessionManager().getUsuarioActual();
        if (user instanceof Client) {
            Client client = (Client) user;
            VirtualWallet wallet = client.getVirtualWallet();
            if (wallet != null) {
                saldoLabel.setText(String.format("$%.2f", wallet.getBalance()));
                walletIdLabel.setText(wallet.getIdWallet());
            } else {
                saldoLabel.setText("$0.00");
                walletIdLabel.setText("No disponible");
            }
        } else {
            saldoLabel.setText("$0.00");
            walletIdLabel.setText("No disponible");
        }
    }

    public void recargarSaldo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/bookyourstary/walletTopUp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Recargar Billetera");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista de recarga de billetera: " + e.getMessage());
        }
    }

    public void cancelar() {
        // Logic to handle cancel action
        System.out.println("Cancelar action triggered.");
    }
}
