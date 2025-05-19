package co.edu.uniquindio.poo.bookyourstary.internalControllers;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.util.LinkedList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Hosting> pendingReservations;

    private CartManager() {
        pendingReservations = new LinkedList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addHosting(Hosting hosting) {
        if (hosting != null && !pendingReservations.contains(hosting)) {
            pendingReservations.add(hosting);
        }
    }

    public List<Hosting> getPendingReservations() {
        return pendingReservations;
    }

    public void clear() {
        pendingReservations.clear();
    }

    public void remove(Hosting hosting){
        pendingReservations.remove(hosting);
    }
}
