package com.oteller.hotelservice.model;

import com.oteller.enums.ReviewStatus;
import com.oteller.enums.Sentiment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reviews", indexes = {
        @Index(name = "idx_hotel_id", columnList = "hotel_id"),
        @Index(name = "idx_sentiment", columnList = "sentiment")
})
@Entity
public class Review extends BaseEntity {


    @ManyToOne()
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @NotBlank(message = "Guest Name can not be blank")
    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment")
    private Sentiment sentiment;

    @Column(name = "sentiment_score")
    private int sentimentScore;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @ElementCollection
    @CollectionTable(name = "review_key_points", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "point")
    private List<String> keyPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReviewStatus status;
}
