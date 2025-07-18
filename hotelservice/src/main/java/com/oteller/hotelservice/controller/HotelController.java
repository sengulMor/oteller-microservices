package com.oteller.hotelservice.controller;

import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.services.HotelService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {           //constructer dependency injection
        this.hotelService = hotelService;
    }

    @PostMapping
    public ResponseEntity<HotelDto> create(@Valid @RequestBody HotelDto dto) {
        log.info("hotel {} saved", dto.getName());
        return new ResponseEntity<>(hotelService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        log.info("get all hotels");
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getById(@PathVariable("id")  Long id) {
        log.info("id= {} hotel fetched", id);
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id")  Long id) {
        log.info("Deleting hotel with id={}", id);
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<HotelDto> update(@Valid @RequestBody HotelDto hotelDto) {
        log.info("Updating hotel with id={}", hotelDto.getId());
        return ResponseEntity.ok(hotelService.update(hotelDto));
    }
}
