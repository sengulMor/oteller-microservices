package com.oteller.hotelservice.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.services.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class ConditionalNotBlankValidationTest {

    public static final String GUEST_NAME_IS_REQUIRED_WHEN_ROOM_IS_NOT_AVAILABLE = "Guest name is required when room is not available";
    public static final String CHECK_IN_DATE_IS_REQUIRED_WHEN_ROOM_IS_NOT_AVAILABLE = "Check-in date is required when room is not available";
    public static final String CHECK_OUT_DATE_IS_REQUIRED_WHEN_ROOM_IS_NOT_AVAILABLE = "Check-out date is required when room is not available";
    public static final String CHECK_OUT_DATE_MUST_BE_EMPTY_WHEN_ROOM_IS_AVAILABLE = "Check-out date must be empty when room is available";
    public static final String CHECK_IN_DATE_MUST_BE_EMPTY_WHEN_ROOM_IS_AVAILABLE = "Check-in date must be empty when room is available";
    public static final String GUEST_NAME_MUST_BE_EMPTY_WHEN_ROOM_IS_AVAILABLE = "Guest name must be empty when room is available";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomService roomService;

    private String asJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o); // ✅ uses injected mapper
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReject_whenUnavailableRoom_missingGuestAndDates() throws Exception {
        RoomDto invalid = new RoomDto();
        invalid.setAvailable(false);
        invalid.setGuestName(null);
        invalid.setCheckInDate(null);
        invalid.setCheckOutDate(null);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='guestName')].message")
                        .value(GUEST_NAME_IS_REQUIRED_WHEN_ROOM_IS_NOT_AVAILABLE))
                .andExpect(jsonPath("$[?(@.field=='checkInDate')].message")
                        .value(CHECK_IN_DATE_IS_REQUIRED_WHEN_ROOM_IS_NOT_AVAILABLE))
                .andExpect(jsonPath("$[?(@.field=='checkOutDate')].message")
                        .value(CHECK_OUT_DATE_IS_REQUIRED_WHEN_ROOM_IS_NOT_AVAILABLE));
    }

    @Test
    void shouldReject_whenAvailableRoom_hasPopulatedFields() throws Exception {
        RoomDto invalid = new RoomDto();
        invalid.setAvailable(true);
        invalid.setGuestName("Alice");
        invalid.setCheckInDate(LocalDate.of(2025, 8, 1));
        invalid.setCheckOutDate(LocalDate.of(2025, 8, 6));

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='guestName')].message")
                        .value(GUEST_NAME_MUST_BE_EMPTY_WHEN_ROOM_IS_AVAILABLE))
                .andExpect(jsonPath("$[?(@.field=='checkInDate')].message")
                        .value(CHECK_IN_DATE_MUST_BE_EMPTY_WHEN_ROOM_IS_AVAILABLE))
                .andExpect(jsonPath("$[?(@.field=='checkOutDate')].message")
                        .value(CHECK_OUT_DATE_MUST_BE_EMPTY_WHEN_ROOM_IS_AVAILABLE));
    }
}
