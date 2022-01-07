package fact.it.reviewservice.controller;

import fact.it.reviewservice.model.Review;
import fact.it.reviewservice.model.ReviewRequestModel;
import fact.it.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    ReviewRepository reviewRepository;

    @PostConstruct
    public void fillDB(){
        if(reviewRepository.count()==0){
            reviewRepository.save(new Review(1, 1, "Very nice!", 5));
            reviewRepository.save(new Review( 2, 1, "Not bad...", 3));
            reviewRepository.save(new Review( 3, 1, "Expected better", 2));
            reviewRepository.save(new Review( 1, 2, "Looks good...", 3));
        }
        System.out.println("Database filled");
    }
    @GetMapping("")
    public List<Review> getReviews(){
        return reviewRepository.findAll();
    }
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable Integer userId){
        return reviewRepository.findReviewsByUserId(userId);
    }
    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProductId(@PathVariable Integer productId){
        return reviewRepository.findReviewsByProductId(productId);
    }
    @GetMapping("/user/{userId}/product/{productId}")
    Review getReviewByUserIdAndProductId(@PathVariable Integer userId, @PathVariable Integer productId){
        return reviewRepository.findReviewByUserIdAndProductId(userId, productId);
    }
    @PostMapping("")
    public Review addReview(@RequestBody ReviewRequestModel reviewRequest){
        Review review = new Review(reviewRequest);
        reviewRepository.save(review);
        return review;
    }
    @PutMapping("")
    public Review updateReview(@RequestBody ReviewRequestModel reviewRequest){
        Review toUpdateReview = reviewRepository.findReviewByUserIdAndProductId(reviewRequest.getUserId(), reviewRequest.getProductId());

        toUpdateReview.setUserId(reviewRequest.getUserId());
        toUpdateReview.setProductId(reviewRequest.getProductId());
        toUpdateReview.setScore(reviewRequest.getScore());
        toUpdateReview.setComment(reviewRequest.getComment());
        toUpdateReview.setUpdatedAt(new Date());

        reviewRepository.save(toUpdateReview);

        return toUpdateReview;
    }
    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity deleteReview(@PathVariable Integer userId, @PathVariable Integer productId){
        Review review = reviewRepository.findReviewByUserIdAndProductId(userId, productId);
        if (review == null) return ResponseEntity.notFound().build();
        reviewRepository.delete(review);
        return ResponseEntity.ok().build();
    }
}
