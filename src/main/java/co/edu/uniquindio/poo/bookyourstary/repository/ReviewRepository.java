package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Review;

public class ReviewRepository {

    private final List<Review> reviews;
    private static ReviewRepository instance;

    private ReviewRepository() {
        this.reviews = new LinkedList<>();
    }

    public static ReviewRepository getInstance() {
        if (instance == null) {
            instance = new ReviewRepository();
        }
        return instance;
    }

    public void save(Review review) {
        findById(review.getId()).ifPresent(reviews::remove); 
        reviews.add(review);
    }
    
    public Optional<Review> findById(String id) {
        return reviews.stream().filter(review -> review.getId().equals(id)).findFirst();
    }

    public List<Review> findByHostingId(String hostingId){
        LinkedList<Review> hostingReviews = new LinkedList<>();
        for (Review review : reviews) {
            if (review.getHostingId().equals(hostingId)) {
                hostingReviews.add(review);
            }
        }
        return hostingReviews;
        
    }
    public void delete(Review review) {
        reviews.removeIf(r -> r.getId().equals(review.getId()));
    }
    

    public List<Review> findAll() {
        return new LinkedList<>(reviews);
    }
}
