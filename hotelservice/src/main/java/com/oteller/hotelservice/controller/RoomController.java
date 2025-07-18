package com.oteller.hotelservice.controller;

import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.services.RoomService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomDto>  create(@Valid @RequestBody RoomDto roomDto) {
        log.info("room {} saved", roomDto.getRoomNumber());
        return new ResponseEntity<>(roomService.create(roomDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getById(@PathVariable("id")  Long id) {
        log.info("id= {} room fetched", id);
        return ResponseEntity.ok(roomService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsOfHotel(@RequestParam(name = "hotelId") Long hotelId) {
        log.info("All rooms of hotel hotelId= {} are fetched", hotelId);
        return ResponseEntity.ok(roomService.getAllRoomsOfHotel(hotelId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id")  Long id) {
        log.info("Deleting room with id={}", id);
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<RoomDto> update(@Valid @RequestBody RoomDto roomDto) {
        log.info("Updating room with id={}", roomDto.getId());
        return ResponseEntity.ok(roomService.update(roomDto));
    }

    @PostMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestBody RoomAvailabilityDTO dto) {
        log.info("Check availability for room with id={}", dto.getRoomId());
        return ResponseEntity.ok(roomService.reserveIfAvailable(dto));
    }
}
