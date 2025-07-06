package com.oteller.reservationservice.controller;

import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.repository.ReservationRepository;
import com.oteller.reservationservice.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationService reservationService,
                                 ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/allReservations")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getById(@PathVariable("id") Long id) {
        ReservationDTO reservationDTO = reservationService.getById(id);
        return ResponseEntity.ok(reservationDTO);
    }

   @PostMapping
    public ResponseEntity<String> create(@RequestBody ReservationDTO request) {
        String result = reservationService.finishReservation(request);
        return ResponseEntity.ok(result);
    }
}
