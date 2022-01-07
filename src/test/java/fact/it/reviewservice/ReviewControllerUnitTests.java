package fact.it.reviewservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.reviewservice.model.Review;
import fact.it.reviewservice.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReviewRepository reviewRepository;

    private ObjectMapper mapper = new ObjectMapper();
    @Test

    public void givenReview_whenGetReviewsByProductIdAndUserId_thenReturnJsonReviews() throws Exception{
        Review testReviewUser1Product1 = new Review(1, 1,"Good", 3);
        given(reviewRepository.findReviewByUserIdAndProductId(1,1)).willReturn(testReviewUser1Product1);

        mockMvc.perform(get("/reviews/user/{userId}/product/{productId}",1,1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.userId",is(1)))
                        .andExpect(jsonPath("$.productId",is(1)))
                        .andExpect(jsonPath("$.score",is(3)))
                        .andExpect(jsonPath("$.comment",is("Good"))
                        );
        
    }
    @Test
    public void givenReview_whenGetReviewsByUserId_thenReturnJsonReviews() throws Exception {
        Review testReviewUser1Product1 = new Review(1, 2,"Good", 3);
        Review testReviewUser1Product2 = new Review(1, 3,"Bad", 1);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(testReviewUser1Product1);
        reviewList.add(testReviewUser1Product2);

        given(reviewRepository.findReviewsByUserId(1)).willReturn(reviewList);

        mockMvc.perform(get("/reviews/user/{userId}",1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId",is(1)))
                .andExpect(jsonPath("$[0].productId",is(2)))
                .andExpect(jsonPath("$[0].score",is(3)))
                .andExpect(jsonPath("$[0].comment",is("Good")))
                .andExpect(jsonPath("$[1].userId",is(1)))
                .andExpect(jsonPath("$[1].productId",is(3)))
                .andExpect(jsonPath("$[1].score",is(1)))
                .andExpect(jsonPath("$[1].comment",is("Bad")));
    }
    @Test
    public void givenReview_whenGetReviewsByProductId_thenReturnJsonReviews() throws Exception {
        Review testReviewUser1Product1 = new Review(2, 1,"Good", 3);
        Review testReviewUser2Product1 = new Review(3, 1,"Bad", 1);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(testReviewUser1Product1);
        reviewList.add(testReviewUser2Product1);

        given(reviewRepository.findReviewsByProductId(1)).willReturn(reviewList);

        mockMvc.perform(get("/reviews/product/{productId}",1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId",is(2)))
                .andExpect(jsonPath("$[0].productId",is(1)))
                .andExpect(jsonPath("$[0].score",is(3)))
                .andExpect(jsonPath("$[0].comment",is("Good")))
                .andExpect(jsonPath("$[1].userId",is(3)))
                .andExpect(jsonPath("$[1].productId",is(1)))
                .andExpect(jsonPath("$[1].score",is(1)))
                .andExpect(jsonPath("$[1].comment",is("Bad")));
    }
    @Test
    public void whenPostReview_thenReturnJsonReview() throws Exception{
        Review testReviewUser3Product1 = new Review(3, 1,"Excellent", 5);

        mockMvc.perform(post("/reviews")
                        .content(mapper.writeValueAsString(testReviewUser3Product1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(3)))
                .andExpect(jsonPath("$.productId",is(1)))
                .andExpect(jsonPath("$.score",is(5)))
                .andExpect(jsonPath("$.comment",is("Excellent")));
    }
    @Test
    public void givenReview_whenPutReview_thenReturnJsonReview() throws Exception{
        Review testReviewUser1Product1 = new Review(1, 1,"Good", 3);

        given(reviewRepository.findReviewByUserIdAndProductId(1,1)).willReturn(testReviewUser1Product1);

        Review updatedReview = new Review(1, 1,"Very good!", 4);

        mockMvc.perform(put("/reviews")
                        .content(mapper.writeValueAsString(updatedReview))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(1)))
                .andExpect(jsonPath("$.productId",is(1)))
                .andExpect(jsonPath("$.score",is(4)))
                .andExpect(jsonPath("$.comment",is("Very good!")));
    }
    @Test
    public void givenReview_whenDeleteReview_thenStatusOk() throws Exception{
        Review reviewToBeDeleted = new Review(999, 999,"Good", 3);

        given(reviewRepository.findReviewByUserIdAndProductId(999,999)).willReturn(reviewToBeDeleted);

        mockMvc.perform(delete("/reviews/user/{userId}/product/{productId}",999,999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void givenNoReview_whenDeleteReview_thenStatusNotFound() throws Exception{
        given(reviewRepository.findReviewByUserIdAndProductId(404,404)).willReturn(null);

        mockMvc.perform(delete("/reviews/user/{userId}/product/{productId}",404,404)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
