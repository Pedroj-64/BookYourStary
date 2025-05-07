package co.edu.uniquindio.poo.bookyourstary.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    private String id;
    private String userId;
    private String hostingId;
    private int score; 
    private String comment;
    private LocalDate date;

    public Review(String id, String userId, String hostingId, int score, String comment, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.hostingId = hostingId;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }
}
