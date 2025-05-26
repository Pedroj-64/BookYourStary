package co.edu.uniquindio.poo.bookyourstary.service.implementService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import co.edu.uniquindio.poo.bookyourstary.model.Review;
import co.edu.uniquindio.poo.bookyourstary.repository.ReviewRepository;

public class ReviewService {

    private final ReviewRepository reviewRepository;
    private static ReviewService instance;

    public static ReviewService getInstance() {
        if (instance == null) {
            instance = new ReviewService(ReviewRepository.getInstance());
        }
        return instance;
    }

    private ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(String userId, String hostingId, int score, String comment) {
        validateScore(score);
        String id = UUID.randomUUID().toString();
        LocalDate date = LocalDate.now();

        Review review = new Review(id, userId, hostingId, score, comment, date);
        reviewRepository.save(review);
        return review;
    }

    private void validateScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }
    }

    public Optional<Review> findById(String id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findByHosting(String hostingId) {
        return reviewRepository.findByHostingId(hostingId);
    }

    public void deleteReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("La reseña no existe."));
        reviewRepository.delete(review);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public double getAverageScore(String hostingId) {
        List<Review> reviews = findByHosting(hostingId);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0.0);
    }
}
