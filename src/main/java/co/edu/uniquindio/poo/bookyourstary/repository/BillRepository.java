package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Bill;

public class BillRepository {

    private final LinkedList<Bill> bills;
    private static BillRepository instance;

    private BillRepository() {
        this.bills = new LinkedList<>();
    }

    public static BillRepository getInstance() {
        if (instance == null) {
            instance = new BillRepository();
        }
        return instance;
    }

    public void save(Bill bill){
        bills.add(bill);
    }

    public Optional<Bill> findById(String billId){
        return bills.stream().filter(bill -> bill.getBillId().equals(billId)).findFirst();
    }
    public LinkedList<Bill> findAll(){
        return new LinkedList<>(bills);
    }

    public void delete(String billId){
        bills.removeIf(bill -> bill.getBillId().equals(billId));
    }

}
