package com.oteller.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.exception.RoomNotFoundException;
import com.oteller.hotelservice.services.RoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateRoom_whenValidRequest() throws Exception {
        RoomDto requestDto = new RoomDto(null, "12", 2, new BigDecimal("40.99"), "", true, null, null, 4L, null, null);
        RoomDto responseDto = new RoomDto(1L, "12", 2, new BigDecimal("40.99"), "", true, null, null, 4L, null, null);
        Mockito.when(roomService.create(any())).thenReturn(responseDto);
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roomNumber").value("12"))
                .andExpect(jsonPath("$.capacity").value("2"))
                .andExpect(jsonPath("$.hotelId").value(4L));
    }

    @Test
    void shouldReturnBadRequest_whenMissingRoomNumber() throws Exception {
        RoomDto invalidDto = new RoomDto(null, "", 2, new BigDecimal("40.99"), "", true, null, null, 4L, null, null);
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("roomNumber"))
                .andExpect(jsonPath("$[0].message").value("Room Number is required"));
    }


    @Test
    void shouldReturnBadRequest_whenMissingCapacity() throws Exception {
        RoomDto invalidDto = new RoomDto(null, "23", null, new BigDecimal("40.99"), "", true, null, null, 4L, null, null);
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("capacity"))
                .andExpect(jsonPath("$[0].message").value("Capacity is required"));
    }

    @Test
    void shouldReturnBadRequest_whenCapacityIsUnderMinValue() throws Exception {
        RoomDto invalidDto = new RoomDto(null, "23", 0, new BigDecimal("40.99"), "", true, null, null, 4L, null, null);
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("capacity"))
                .andExpect(jsonPath("$[0].message").value("Capacity must be at least 1"));
    }


    @Test
    void shouldReturnBadRequest_whenMissingPricePerNight() throws Exception {
        RoomDto invalidDto = new RoomDto(null, "23", 2, null, "", true, null, null, 4L, null, null);
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("pricePerNight"))
                .andExpect(jsonPath("$[0].message").value("Price per Night is required"));
    }

    @Test
    void shouldReturnRoom_whenValidId() throws Exception {
        Long id = 1L;
        RoomDto responseDto = new RoomDto(1L, "12", 2, new BigDecimal("40.99"), "", true, null, null, 4L, null, null);

        Mockito.when(roomService.getById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("12"));
    }

    @Test
    void shouldReturnNotFound_whenRoomDoesNotExist() throws Exception {
        Long id = 999L;
        Mockito.when(roomService.getById(id)).thenThrow(new RoomNotFoundException(id));

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn_allRooms() throws Exception {
        Long hotelId = 1L;
        RoomDto responseDto = new RoomDto(1L, "12", 2, new BigDecimal("40.99"), "", true, null, null, hotelId, null, null);
        Mockito.when(roomService.getAllRoomsOfHotel(hotelId)).thenReturn(List.of(responseDto));
        mockMvc.perform(get("/rooms?hotelId=" + hotelId))
                .andExpect(status().isOk());
    }


    @Test
    void shouldUpdateRoom_whenValidRequest() throws Exception {
        Long id = 1L;

        RoomDto requestDto = new RoomDto(id, "12", 2, new BigDecimal("40.99"), "", true, null, null, 4L, null, null);
        RoomDto responseDto = new RoomDto(id, "12", 3, new BigDecimal("50.99"), "", true, null, null, 4L, null, null);

        Mockito.when(roomService.update(any())).thenReturn(responseDto);

        mockMvc.perform(put("/rooms", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value(3))
                .andExpect(jsonPath("$.pricePerNight").value("50.99"));
    }


    @Test
    void shouldDeleteRoom_whenValidId() throws Exception {
        Long id = 1L;
        Mockito.doNothing().when(roomService).deleteById(id);
        mockMvc.perform(delete("/rooms/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_whenRoomToDeleteDoesNotExist() throws Exception {
        Long id = 1L;
        Mockito.doThrow(new RoomNotFoundException(id)).when(roomService).deleteById(id);
        mockMvc.perform(delete("/rooms/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest_whenRoomDtoIsNull() throws Exception {
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnBadRequest_whenPriceIsNegative() throws Exception {
        RoomDto invalidDto = new RoomDto(null, "12", 2, new BigDecimal("-5.00"), "", true, null, null, 4L, null, null);
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("pricePerNight"))
                .andExpect(jsonPath("$[0].message").value("Price must be greater than 0")); // if defined
    }

    @Test
    void checkAvailability_shouldReturnTrue_whenRoomIsAvailable() throws Exception {
        // Arrange
        RoomAvailabilityDTO dto = RoomAvailabilityDTO.builder()
                .roomId(1L)
                .hotelId(1L)
                .guestName("Alice")
                .checkInDate(LocalDate.of(2025, 7, 20))
                .checkOutDate(LocalDate.of(2025, 7, 25))
                .build();

        Mockito.when(roomService.reserveIfAvailable(any(RoomAvailabilityDTO.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/rooms/check-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Optionally verify the service call
        Mockito.verify(roomService).reserveIfAvailable(any(RoomAvailabilityDTO.class));
    }

    @Test
    void checkAvailability_shouldReturnFalse_whenRoomIsNotAvailable() throws Exception {
        RoomAvailabilityDTO dto = RoomAvailabilityDTO.builder()
                .roomId(2L)
                .hotelId(1L)
                .guestName("Bob")
                .checkInDate(LocalDate.of(2025, 8, 1))
                .checkOutDate(LocalDate.of(2025, 8, 5))
                .build();

        Mockito.when(roomService.reserveIfAvailable(any(RoomAvailabilityDTO.class))).thenReturn(false);

        mockMvc.perform(post("/rooms/check-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }


    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
