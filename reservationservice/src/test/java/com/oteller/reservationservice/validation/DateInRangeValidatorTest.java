package com.oteller.reservationservice.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oteller.reservationservice.dto.ReservationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class DateInRangeValidatorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReject_whenCheckOutDateBeforeCheckInDate() throws Exception {
        LocalDate checkInDate = LocalDate.now().plusMonths(1);
        LocalDate checkOutDate = checkInDate.minusDays(5);
        ReservationDTO invalidDto = new ReservationDTO(null, 1L, 1L, "John", checkInDate, checkOutDate, "johntest@gmail.com");

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("checkInDate"))
                .andExpect(jsonPath("$[0].message").value("Check-In date must be before Check-Out Date."));


    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

