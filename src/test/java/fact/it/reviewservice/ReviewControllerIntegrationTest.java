package fact.it.reviewservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.reviewservice.model.Review;
import fact.it.reviewservice.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    private Review reviewUser1Product1 = new Review("1","1","Nice", 3);
    private Review reviewUser1Product2 = new Review("1","2","Bad", 1);
    private Review reviewUser2Product1 = new Review("2","2","Amazing", 5);
    private Review reviewToBeDeleted = new Review("999","999","-", 2);

    @BeforeEach
    public void beforeAllTests(){
        reviewRepository.deleteAll();
        reviewRepository.save(reviewUser1Product1);
        reviewRepository.save(reviewUser1Product2);
        reviewRepository.save(reviewUser2Product1);
        reviewRepository.save(reviewToBeDeleted);
    }
    @AfterEach
    public void afterEachTest(){
        reviewRepository.deleteAll();
    }
    private ObjectMapper mapper = new ObjectMapper();
    @Test
    public void givenReview_whenGetReviewsByUserIdAndProductId_thenReturnJsonReview() throws Exception{
        mockMvc.perform(get("/reviews/user/{userId}/product/{productId}",1,"1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is("1")))
                .andExpect(jsonPath("$.productId",is("1")))
                .andExpect(jsonPath("$.score",is(3)))
                .andExpect(jsonPath("$.comment",is("Nice"))
                );

    }
    @Test
    public void givenReview_whenGetReviewsByUserId_thenReturnJsonReviews() throws Exception {
        mockMvc.perform(get("/reviews/user/{userId}",1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId",is("1")))
                .andExpect(jsonPath("$[0].productId",is("1")))
                .andExpect(jsonPath("$[0].score",is(3)))
                .andExpect(jsonPath("$[0].comment",is("Nice")))
                .andExpect(jsonPath("$[1].userId",is("1")))
                .andExpect(jsonPath("$[1].productId",is("2")))
                .andExpect(jsonPath("$[1].score",is(1)))
                .andExpect(jsonPath("$[1].comment",is("Bad")));

}
    @Test
    public void givenReview_whenGetReviewsByProductId_thenReturnJsonReviews() throws Exception {


        mockMvc.perform(get("/reviews/product/{productId}","1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId",is("1")))
                .andExpect(jsonPath("$[0].productId",is("1")))
                .andExpect(jsonPath("$[0].score",is(3)))
                .andExpect(jsonPath("$[0].comment",is("Nice")));
    }
    @Test
    public void whenPostReview_thenReturnJsonReview() throws Exception{
        Review testReviewUser3Product1 = new Review("3", "1","Excellent", 5);

        mockMvc.perform(post("/reviews")
                        .content(mapper.writeValueAsString(testReviewUser3Product1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is("3")))
                .andExpect(jsonPath("$.productId",is("1")))
                .andExpect(jsonPath("$.score",is(5)))
                .andExpect(jsonPath("$.comment",is("Excellent")));
    }
    @Test
    public void givenReview_whenPutReview_thenReturnJsonReview() throws Exception{


        Review updatedReview = new Review("1", "1","Very good!", 4);

        mockMvc.perform(put("/reviews")
                        .content(mapper.writeValueAsString(updatedReview))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is("1")))
                .andExpect(jsonPath("$.productId",is("1")))
                .andExpect(jsonPath("$.score",is(4)))
                .andExpect(jsonPath("$.comment",is("Very good!")));
    }
    @Test
    public void givenReview_whenDeleteReview_thenStatusOk() throws Exception{

        mockMvc.perform(delete("/reviews/user/{userId}/product/{productId}","999","999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void givenNoReview_whenDeleteReview_thenStatusNotFound() throws Exception{
        mockMvc.perform(delete("/reviews/user/{userId}/product/{productId}","404","404")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
