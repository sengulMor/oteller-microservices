package com.oteller.hotelservice.dto;

import java.util.List;


//To give a summary of the review by using Spring AI
public record ReviewAnalysisDto(
        String sentiment,
        int sentimentScore,
        String summary,
        List<String> keyPoints,
        boolean isAppropriate
) {
}