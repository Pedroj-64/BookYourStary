package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Bill;
import co.edu.uniquindio.poo.bookyourstary.model.Booking;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import java.util.List;
import java.util.Optional;

public interface IBillService {
    /**
     * Genera una factura para una reserva
     */
    Bill generateBill(Booking booking);

    /**
     * Busca una factura por su ID
     */
    Optional<Bill> findBillById(String billId);

    /**
     * Obtiene todas las facturas de un cliente
     */
    List<Bill> getBillsByClient(Client client);

    /**
     * Obtiene todas las facturas
     */
    List<Bill> getAllBills();

    /**
     * Elimina una factura
     */
    void deleteBill(String billId);

    /**
     * Elimina todas las facturas
     */
    void clearAll();
}
