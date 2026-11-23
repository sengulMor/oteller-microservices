package com.oteller.reservationservice.services;

import com.oteller.reservationservice.tool.RoomAvailabilityTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class BookingAssistantService {

    private final ChatClient chatClient;
    private final RoomAvailabilityTools roomAvailabilityTools;

    public BookingAssistantService(
            ChatClient.Builder chatClientBuilder,
            RoomAvailabilityTools roomAvailabilityTools
    ) {
        this.roomAvailabilityTools = roomAvailabilityTools;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        Du bist ein freundlicher Buchungs-Assistent für unser Hotel.
                        Nutze verfügbare Tools, um aktuelle Zimmerverfügbarkeiten zu prüfen, statt Zahlen zu raten.
                        Antworte klar, hilfreich und auf Deutsch.
                        """)
                .build();
    }

    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .tools(roomAvailabilityTools)
                .call()
                .content();
    }
}
