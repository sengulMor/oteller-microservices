package com.oteller.reservationservice.controller;

import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.services.ReservationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody ReservationDTO dto) {
        log.info("Reservation for Guest {} create", dto.getGuestName());
        return ResponseEntity.ok(reservationService.finishReservation(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getById(@PathVariable("id") Long id) {
        log.info("id= {} reservation fetch", id);
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        log.info("get all reservations");
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    /*TODO: Implement update reservation endpoint  */

    /*TODO: Implement delete reservation endpoint*/
}