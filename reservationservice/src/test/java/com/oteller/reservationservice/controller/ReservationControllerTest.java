package com.oteller.reservationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.exception.ReservationNotFoundException;
import com.oteller.reservationservice.services.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    void shouldCreateReservation_whenValidRequest() throws Exception {
        LocalDate checkInDate = LocalDate.now().plusMonths(1);
        LocalDate checkOutDate = checkInDate.plusWeeks(1);

        ReservationDTO requestDto = new ReservationDTO(null, 1L, 5L, "John", checkInDate, checkOutDate);
        String response = "Reservation successful.";

        Mockito.when(reservationService.finishReservation(any())).thenReturn(response);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    void shouldReturnBadRequest_whenMissingHotelId() throws Exception {
        LocalDate checkInDate = LocalDate.now().plusMonths(1);
        LocalDate checkOutDate = checkInDate.plusWeeks(1);
        ReservationDTO invalidDto = new ReservationDTO(null, null, 1L, "John", checkInDate, checkOutDate);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Hotel Id is required"));
    }

    @Test
    void shouldReturnBadRequest_whenMissingRoomId() throws Exception {
        LocalDate checkInDate = LocalDate.now().plusMonths(1);
        LocalDate checkOutDate = checkInDate.plusWeeks(1);
        ReservationDTO invalidDto = new ReservationDTO(null, 1L, null, "John", checkInDate, checkOutDate);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Room Id is required"));
    }

    @Test
    void shouldReturnBadRequest_whenMissingGuestName() throws Exception {
        LocalDate checkInDate = LocalDate.now().plusMonths(1);
        LocalDate checkOutDate = checkInDate.plusWeeks(1);
        ReservationDTO invalidDto = new ReservationDTO(null, 1L, 1L, null, checkInDate, checkOutDate);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Guest name is required"));
    }

    @Test
    void shouldReturnBadRequest_whenMissingCheckInDate() throws Exception {
        ReservationDTO invalidDto = new ReservationDTO(null, 1L, 1L, "John", null, LocalDate.now());

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Check-In date is required"));
    }

    @Test
    void shouldReturnBadRequest_whenMissingCheckOutDate() throws Exception {
        ReservationDTO invalidDto = new ReservationDTO(null, 1L, 1L, "John", LocalDate.now(), null);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Check-Out date is required"));
    }


    @Test
    void shouldReturnReservation_whenValidId() throws Exception {
        Long id = 1L;
        LocalDate checkInDate = LocalDate.now().plusMonths(1);
        LocalDate checkOutDate = checkInDate.plusWeeks(1);
        ReservationDTO response = new ReservationDTO(id, 1L, 5L, "John", checkInDate, checkOutDate);

        Mockito.when(reservationService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("John"))
                .andExpect(jsonPath("$.checkInDate").value(checkInDate.toString()));
    }

    @Test
    void shouldReturnNotFound_whenHotelDoesNotExist() throws Exception {
        Long id = 1L;
        Mockito.when(reservationService.getById(id)).thenThrow(new ReservationNotFoundException(id));
        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldReturn_allReservations() throws Exception {
        Mockito.when(reservationService.getAllReservations()).thenReturn(List.of(new ReservationDTO()));
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk());
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
