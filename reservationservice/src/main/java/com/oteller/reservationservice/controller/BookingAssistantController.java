package com.oteller.reservationservice.controller;

import com.oteller.reservationservice.services.BookingAssistantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-assistant")
public class BookingAssistantController {

    private final BookingAssistantService bookingAssistantService;

    public BookingAssistantController(BookingAssistantService bookingAssistantService) {
        this.bookingAssistantService = bookingAssistantService;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String message) {
        return bookingAssistantService.chat(message);
    }
}
