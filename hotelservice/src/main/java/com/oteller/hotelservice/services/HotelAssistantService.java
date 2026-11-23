package com.oteller.hotelservice.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class HotelAssistantService {

    private final ChatClient chatClient;

    public HotelAssistantService(ChatClient.Builder chatClientBuilder,
                                 HotelAvailabilityTools availabilityTools) {
        this.chatClient = chatClientBuilder
                .defaultTools(availabilityTools)
                .build();
    }

    public String askAssistant(String userPrompt) {
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }
}
