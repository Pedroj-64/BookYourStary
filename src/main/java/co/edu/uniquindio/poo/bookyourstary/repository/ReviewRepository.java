package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Review;

public class ReviewRepository {

    private final LinkedList<Review> reviews;

    public ReviewRepository() {
        this.reviews = new LinkedList<>();
    }

    public void save(Review review) {
        findById(review.getId()).ifPresent(reviews::remove); 
        reviews.add(review);
    }
    
    public Optional<Review> findById(String id) {
        return reviews.stream().filter(review -> review.getId().equals(id)).findFirst();
    }

    public LinkedList<Review> findByHostingId(String hostingId){
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
    

    public LinkedList<Review> findAll() {
        return new LinkedList<>(reviews);
    }
}
