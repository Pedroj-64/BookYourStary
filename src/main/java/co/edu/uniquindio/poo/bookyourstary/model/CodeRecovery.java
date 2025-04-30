package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeRecovery {

    private UUID code;
    private String userEmail;
    private LocalDateTime creationDate;
    private boolean used;

    public CodeRecovery(UUID code, String userEmail, LocalDateTime creationDate, boolean used) {
        this.code = code;
        this.userEmail = userEmail;
        this.creationDate = creationDate;
        this.used = used;
    }

}
