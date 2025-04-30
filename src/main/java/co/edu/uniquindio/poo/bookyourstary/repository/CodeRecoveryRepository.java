package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import co.edu.uniquindio.poo.bookyourstary.model.CodeRecovery;

public class CodeRecoveryRepository {

    private final LinkedList<CodeRecovery> recoveries;

    public CodeRecoveryRepository() {
        this.recoveries = new LinkedList<>();
    }

    public void save(CodeRecovery codeRecovery) {
        recoveries.add(codeRecovery);
    }

    public Optional<CodeRecovery> findById(UUID code) {
        return recoveries.stream()
                .filter(recovery -> recovery.getCode().equals(code))
                .findFirst();
    }

    public void delete(CodeRecovery codeRecovery) {
        recoveries.remove(codeRecovery);
    }

    public LinkedList<CodeRecovery> findByEmail(String email) {
        return recoveries.stream()
                .filter(recovery -> recovery.getUserEmail().equalsIgnoreCase(email))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void markAsUsed(UUID code) {
        findById(code).ifPresent(c -> c.setUsed(true));
    }

    public LinkedList<CodeRecovery> findAll() {
        return new LinkedList<>(recoveries);
    }

}
