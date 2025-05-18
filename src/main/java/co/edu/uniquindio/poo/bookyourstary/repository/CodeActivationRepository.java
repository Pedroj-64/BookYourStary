package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

import co.edu.uniquindio.poo.bookyourstary.model.CodeActivation;

public class CodeActivationRepository {

    private static CodeActivationRepository instance;
    private final LinkedList<CodeActivation> activations;

    private CodeActivationRepository() {
        this.activations = new LinkedList<>();
    }

    public static CodeActivationRepository getInstance() {
        if (instance == null) {
            instance = new CodeActivationRepository();
        }
        return instance;
    }

    public void save(CodeActivation codeActivation) {
        activations.add(codeActivation);
    }

    public Optional<CodeActivation> findById(UUID code) {
        return activations.stream()
                .filter(a -> a.getCode().equals(code))
                .findFirst();
    }

    public LinkedList<CodeActivation> findAll() {
        return new LinkedList<>(activations);
    }

    public void delete(CodeActivation codeActivation) {
        activations.remove(codeActivation);
    }

}
