package com.oteller.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.dto.HotelDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.oteller.hotelservice.exception.HotelNotFoundException;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.repository.AddressRepository;
import com.oteller.hotelservice.services.HotelService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private HotelService hotelService;

    @MockBean
    private AddressRepository addressRepository;

    @Test
    void shouldCreateHotel_whenValidRequest() throws Exception {
        AddressDto requestAddress = new AddressDto(null, "Alte Post str 37", "Stuttgart", "Deutschland", "34521");
        AddressDto responseAddress = new AddressDto(1L, "Alte Post str 37", "Stuttgart", "Deutschland", "34521");

        HotelDto requestDto = new HotelDto(null, "Hilton", 3, requestAddress, null, null, null);
        HotelDto responseDto = new HotelDto(1L, "Hilton", 3, responseAddress, null, null, null);

        Mockito.when(hotelService.create(any())).thenReturn(responseDto);
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hilton"))
                .andExpect(jsonPath("$.address.id").value(1L));
    }

    @Test
    void shouldReturnBadRequest_whenMissingName() throws Exception {
        AddressDto requestAddress = new AddressDto(null, "Alte Post str 37", "Stuttgart", "Deutschland", "34521");
        HotelDto invalidDto =  new HotelDto(null, "", 3, requestAddress, null, null, null);
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("name"))
                .andExpect(jsonPath("$[0].message").value("Hotel name is required"));
    }

    @Test
    void shouldReturnBadRequest_whenStarRatingIsUnderMinValue() throws Exception {
        AddressDto requestAddress = new AddressDto(null, "Alte Post str 37", "Stuttgart", "Deutschland", "34521");
        HotelDto invalidDto =  new HotelDto(null, "Hotel One", 0, requestAddress, null, null, null);
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("starRating"))
                .andExpect(jsonPath("$[0].message").value("Star Rating can not smaller as 1"));
    }

    @Test
    void shouldReturnBadRequest_whenStarRatingIsAboveMaxValue() throws Exception {
        AddressDto requestAddress = new AddressDto(null, "Alte Post str 37", "Stuttgart", "Deutschland", "34521");
        HotelDto invalidDto =  new HotelDto(null, "Hotel One", 9, requestAddress, null, null, null);
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("starRating"))
                .andExpect(jsonPath("$[0].message").value("Star Rating can not bigger as 5"));
    }

    @Test
    void shouldReturnBadRequest_whenInvalidAddress() throws Exception {
        HotelDto invalidDto = new HotelDto(null, "Hotel Name", 3, new AddressDto(null, "", "", "", ""), null, null, null);
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenHotelAddressIsNotUnique() throws Exception {
        AddressDto duplicateAddress = new AddressDto(null, "Alte Post str 37", "Stuttgart", "Deutschland", "34521");
        HotelDto requestDto = new HotelDto(null, "Hilton", 3, duplicateAddress, null, null, null);

        Address existingAddress = new Address();
        existingAddress.setId(99L);
        Mockito.when(addressRepository.findByStreetAndCityAndZipCodeAndCountry(
                "Alte Post str 37", "Stuttgart", "34521", "Deutschland"
        )).thenReturn(Optional.of(existingAddress));

        // When + Then: Expect validation to fail with message
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("address"))
                .andExpect(jsonPath("$[0].message").value("Address is already assigned to another hotel."));
    }

    @Test
    void shouldReturnHotel_whenValidId() throws Exception {
        Long id = 1L;
        AddressDto address = new AddressDto(1L, "Main St", "Berlin", "Germany", "12345");
        HotelDto hotel = new HotelDto(id, "Intercontinental", 5, address, null, null, null);

        Mockito.when(hotelService.getById(id)).thenReturn(hotel);

        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Intercontinental"))
                .andExpect(jsonPath("$.address.city").value("Berlin"));
    }

    @Test
    void shouldReturnNotFound_whenHotelDoesNotExist() throws Exception {
        Long id = 999L;
        Mockito.when(hotelService.getById(id)).thenThrow(new HotelNotFoundException(id));

        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn_allHotels() throws Exception {
        Long id = 1L;
        AddressDto address = new AddressDto(1L, "Main St", "Berlin", "Germany", "12345");
        HotelDto hotel = new HotelDto(id, "Intercontinental", 5, address, null, null, null);

        Mockito.when(hotelService.getAllHotels()).thenReturn(List.of(hotel));

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateHotel_whenValidRequest() throws Exception {
        Long id = 1L;
        AddressDto address = new AddressDto(1L, "Updated Str", "Munich", "Germany", "11111");
        HotelDto requestDto = new HotelDto(id, "Hotel Name", 4, address, null, null, null);
        HotelDto responseDto = new HotelDto(id, "Updated Name", 4, address, null, null, null);

        Mockito.when(hotelService.update(any())).thenReturn(responseDto);

        mockMvc.perform(put("/hotels", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.address.street").value("Updated Str"));
    }

    @Test
    void shouldDeleteHotel_whenValidId() throws Exception {
        Long id = 1L;
        Mockito.doNothing().when(hotelService).deleteById(id);
        mockMvc.perform(delete("/hotels/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_whenHotelToDeleteDoesNotExist() throws Exception {
        Long id = 1L;
        Mockito.doThrow(new HotelNotFoundException(id)).when(hotelService).deleteById(id);
        mockMvc.perform(delete("/hotels/{id}", id))
                .andExpect(status().isNotFound());
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
