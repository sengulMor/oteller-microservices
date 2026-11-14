package com.oteller.hotelservice.services;


import com.oteller.hotelservice.dto.ReviewAnalysisDto;
import com.oteller.hotelservice.dto.ReviewDto;
import com.oteller.hotelservice.mapper.ReviewMapper;
import com.oteller.hotelservice.model.Review;
import com.oteller.hotelservice.repository.HotelRepository;
import com.oteller.hotelservice.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ChatClient chatClient;
    private final HotelRepository hotelRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
                         ChatClient.Builder chatClientBuilder, HotelRepository hotelRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.chatClient = chatClientBuilder.build();
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public ReviewDto create(ReviewDto reviewDto) {
        ReviewAnalysisDto analysis = getReviewAnalysisDto(reviewDto);
        Review review = reviewMapper.toEntity(reviewDto, analysis);
        review.setHotel(hotelRepository.getReferenceById(reviewDto.getHotelId()));
        Review reviewSaved = reviewRepository.save(review);
        log.info("Review created: {}", review.getId());
        return reviewMapper.toDto(reviewRepository.save(reviewSaved));
    }

    private ReviewAnalysisDto getReviewAnalysisDto(ReviewDto reviewDto) {
        return chatClient.prompt()
                .user(u -> u.text("Analyse diesen Hotel-Kommentar: {comment}")
                        .param("comment", reviewDto.getComment()))
                .call()
                .entity(ReviewAnalysisDto.class);
    }

}
