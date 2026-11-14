package com.oteller.hotelservice.services;

import com.oteller.hotelservice.dto.ReviewAnalysisDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiReviewService {

    private final ChatClient chatClient;

    public AiReviewService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are an expert AI for hotel management and guest feedback analysis. 
                        Your task is to analyze hotel reviews, determine the sentiment (e.g. POSITIVE, NEGATIVE, NEUTRAL), 
                        extract a concise summary, and list key points.
                        """)
                .build();
    }

    public ReviewAnalysisDto analyzeReview(String comment) {
        // Nutzen der modernen .entity(...) Fluent-API von Spring AI für automatisches JSON-Parsing
        return chatClient.prompt()
                .user(u -> u.text("Please analyze this hotel review comment: \"{comment}\"")
                        .param("comment", comment))
                .call()
                .entity(ReviewAnalysisDto.class);
    }
}
