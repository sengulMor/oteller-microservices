package com.oteller.hotelservice.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.model.Room;
import com.oteller.hotelservice.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class RoomUniqueValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    void shouldReject_whenRoomNumberAlreadyExistsForSameHotel() throws Exception {
        Room existingRoom = new Room();
        existingRoom.setId(123L);
        existingRoom.setRoomNumber("101");

        Mockito.when(roomRepository.findByHotel_IdAndRoomNumber(1L, "101"))
                .thenReturn(Optional.of(existingRoom));

        RoomDto invalid = new RoomDto();
        invalid.setId(999L); // Different ID → not same room → validator should fail
        invalid.setRoomNumber("101");
        invalid.setHotelId(1L);
        invalid.setAvailable(true); // other required fields as needed

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='roomNumber')].message")
                        .value("Room number already exists for hotel by id: " +  invalid.getHotelId())); // assuming the message is resolved like this
    }
}

