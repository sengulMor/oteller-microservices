package com.oteller.hotelservice.controller;

import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.services.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/allHotels")
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        List<HotelDto> hotelDtos = hotelService.getAllHotels();
        return ResponseEntity.ok(hotelDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getById(@PathVariable("id")  Long id) {
        HotelDto hotelDto = hotelService.getById(id);
        return ResponseEntity.ok(hotelDto);
    }

    @PostMapping
    public ResponseEntity<HotelDto> create(@Valid @RequestBody HotelDto hotelDto) {
        HotelDto saved = hotelService.create(hotelDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
