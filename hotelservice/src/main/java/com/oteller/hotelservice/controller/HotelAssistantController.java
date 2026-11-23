package com.oteller.hotelservice.controller;

import com.oteller.hotelservice.services.HotelAssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assistant")
public class HotelAssistantController {

    private final HotelAssistantService assistantService;

    public HotelAssistantController(HotelAssistantService assistantService) {
        this.assistantService = assistantService;
    }

    public record AskRequest(String message) {
    }

    public record AskResponse(String reply) {
    }

    @PostMapping
    public ResponseEntity<AskResponse> ask(@RequestBody AskRequest request) {
        String answer = assistantService.askAssistant(request.message());
        return ResponseEntity.ok(new AskResponse(answer));
    }
}
