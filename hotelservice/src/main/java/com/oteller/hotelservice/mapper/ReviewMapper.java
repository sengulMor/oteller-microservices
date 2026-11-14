package com.oteller.hotelservice.mapper;

import com.oteller.enums.ReviewStatus;
import com.oteller.enums.Sentiment;
import com.oteller.hotelservice.dto.ReviewAnalysisDto;
import com.oteller.hotelservice.dto.ReviewDto;
import com.oteller.hotelservice.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = HotelMapper.class)
public interface ReviewMapper {

    ReviewDto toDto(Review review);

    @Mapping(source = "reviewDto.id", target = "id")
    @Mapping(source = "reviewDto.guestName", target = "guestName")
    @Mapping(source = "reviewDto.rating", target = "rating")
    @Mapping(source = "reviewDto.comment", target = "comment")
    @Mapping(source = "analysis.sentiment", target = "sentiment", qualifiedByName = "toSentimentEnum")
    @Mapping(source = "analysis.sentimentScore", target = "sentimentScore")
    @Mapping(source = "analysis.summary", target = "summary")
    @Mapping(source = "analysis.keyPoints", target = "keyPoints")
    @Mapping(target = "status", expression = "java(mapStatus(analysis))")
    Review toEntity(ReviewDto reviewDto, ReviewAnalysisDto analysis);

    @Named("toSentimentEnum")
    default Sentiment toSentimentEnum(String sentimentStr) {
        if (sentimentStr == null) {
            return null;
        }
        try {
            return Sentiment.valueOf(sentimentStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Sentiment.NEUTRAL;
        }
    }

    default ReviewStatus mapStatus(ReviewAnalysisDto analysis) {
        if (analysis == null) {
            return ReviewStatus.PENDING;
        }
        if (analysis.isAppropriate()) {
            return ReviewStatus.PUBLISHED;
        } else {
            return ReviewStatus.FLAGGED;
        }
    }
}
