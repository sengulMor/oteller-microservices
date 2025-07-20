package com.oteller.reservationservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationServiceSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldAllowAllRequests_whenSecurityIsConfigured() throws Exception {
        mockMvc.perform(get("/reservation"))  // or any other endpoint
                .andExpect(status().isOk());  // Since all requests are permitted
    }
}

