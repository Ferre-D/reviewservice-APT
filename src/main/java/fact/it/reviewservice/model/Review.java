package fact.it.reviewservice.model;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private String productId;
    private String comment;
    private Integer score;
    private Date createdAt;
    private Date updatedAt;

    public Review() {
    }

    public Review(Integer userId, String productId, String comment, Integer score) {
        this.userId = userId;
        this.productId = productId;
        this.comment = comment;
        this.score = score;
        this.createdAt = new Date();
    }

    public Review(ReviewRequestModel reviewRequest) {
        this.userId = reviewRequest.getUserId();
        this.productId = reviewRequest.getProductId();
        this.comment = reviewRequest.getComment();
        this.score = reviewRequest.getScore();
        this.createdAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
