package com.oteller.reservationservice.Controller;

import com.oteller.reservationservice.controller.ReservationController;
import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.services.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;


    @Test
    void shouldCreateHotelSuccessfully() {
        ReservationDTO inputDto = getReservationDTO();
        when(reservationService.finishReservation(inputDto)).thenReturn("Reservation successful.");

        // When
        ResponseEntity<String> response = reservationController.create(inputDto);

        // Then
        verify(reservationService, times(1)).finishReservation(inputDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation successful.", response.getBody());
    }

    @Test
    void shouldGetByIdSuccessfully() {
        ReservationDTO inputDto = getReservationDTO();
        when(reservationService.getById(1L)).thenReturn(inputDto);

        // When
        ResponseEntity<ReservationDTO> response = reservationController.getById(1L);

        // Then
        verify(reservationService, times(1)).getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputDto, response.getBody());
    }


    @Test
    void shouldGetAllReservationsSuccessfully() {
        ReservationDTO inputDto = getReservationDTO();
        when(reservationService.getAll()).thenReturn(Arrays.asList(inputDto));

        // When
        ResponseEntity<List<ReservationDTO>> response = reservationController.getAllReservations();

        // Then
        verify(reservationService, times(1)).getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(inputDto), response.getBody());
    }

    private ReservationDTO getReservationDTO() {
        return ReservationDTO.builder()
                .roomId(1L)
                .guestName("John")
                .startDate(LocalDate.now().plusMonths(1))
                .endDate(LocalDate.now().plusMonths(2).plusWeeks(1))
                .build();
    }
}
