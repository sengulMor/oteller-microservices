package com.oteller.notificationservice.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiNotificationService {

    private final ChatClient chatClient;

    public AiNotificationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a friendly customer service representative for a hotel reservation system. " +
                        "Write a short, professional confirmation email based on the event data.")
                .build();
    }

    public String generateWelcomeMessage(String guestName, String hotelName, String roomType) {
        String userPrompt = String.format("Gast: %s, Hotel: %s, Zimmer: %s", guestName, hotelName, roomType);

        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }
}
