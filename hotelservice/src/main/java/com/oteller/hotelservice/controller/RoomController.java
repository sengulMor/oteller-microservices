package com.oteller.hotelservice.controller;

import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomDto>  create(@Valid @RequestBody RoomDto roomDto) {
        RoomDto saved = roomService.create(roomDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getById(@PathVariable("id")  Long id) {
        RoomDto roomDto = roomService.getById(id);
        return ResponseEntity.ok(roomDto);
    }

    @GetMapping("/byHotel/{hotelId}")
    public ResponseEntity<List<RoomDto>> getAllRoomsOfHotel(@PathVariable("hotelId")  Long hotelId) {
        List<RoomDto> roomDtos = roomService.getAllRoomsOfHotel(hotelId);
        return ResponseEntity.ok(roomDtos);
    }

    @PostMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestBody RoomAvailabilityDTO request) {
        boolean available = roomService.isRoomAvailable(request);
        return ResponseEntity.ok(available);
    }
}
