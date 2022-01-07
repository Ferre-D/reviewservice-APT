package fact.it.reviewservice.repository;

import fact.it.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findReviewsByUserId(Integer userId);
    List<Review> findReviewsByProductId(String productId);
    Review findReviewByUserIdAndProductId(Integer userId, String productId);
}
