package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Review;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import java.util.List;

public interface IReviewService {
    /**
     * Crea una nueva reseña
     */
    Review createReview(Client client, Hosting hosting, int rating, String comment);

    /**
     * Obtiene todas las reseñas de un alojamiento
     */
    List<Review> getReviewsByHosting(Hosting hosting);

    /**
     * Obtiene todas las reseñas de un cliente
     */
    List<Review> getReviewsByClient(Client client);

    /**
     * Obtiene la valoración promedio de un alojamiento
     */
    double getAverageRatingForHosting(Hosting hosting);

    /**
     * Actualiza una reseña existente
     */
    void updateReview(Review review, int newRating, String newComment);

    /**
     * Elimina una reseña
     */
    void deleteReview(String reviewId);

    /**
     * Verifica si un cliente ya ha hecho una reseña para un alojamiento
     */
    boolean hasClientReviewedHosting(Client client, Hosting hosting);

    /**
     * Elimina todas las reseñas
     */
    void clearAll();
}
