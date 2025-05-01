package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.VirtualWallet;

public class VirtualWalletRepository {

    private final LinkedList<VirtualWallet> virtualWallets;

    public VirtualWalletRepository() {
        this.virtualWallets = new LinkedList<>();
    }

    public void save(VirtualWallet virtualWallet) {
        virtualWallets.add(virtualWallet);
    }

    public LinkedList<VirtualWallet> findAll() {
        return new LinkedList<>(virtualWallets);
    }

    public Optional<VirtualWallet> findById(String idWallet) {
        return virtualWallets.stream()
                .filter(wallet -> wallet.getIdWallet().equals(idWallet))
                .findFirst();
    }

    public void delete(VirtualWallet virtualWallet) {
        virtualWallets.remove(virtualWallet);
    }

}
