package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.CodeActivation;

public class CodeActivationRepository {

    private final LinkedList<CodeActivation> activations;

    public CodeActivationRepository() {
        this.activations = new LinkedList<>();
    }

    public void save(CodeActivation codeActivation) {
        activations.add(codeActivation);
    }

    public Optional<CodeActivation> findById(String code) {
        return activations.stream().filter(activation -> activation.getCode().equals(code)).findFirst();
    }

    public LinkedList<CodeActivation> findAll() {
        return new LinkedList<>(activations);
    }

    public void delete(CodeActivation codeActivation) {
       activations.remove(codeActivation);
    }   

}
