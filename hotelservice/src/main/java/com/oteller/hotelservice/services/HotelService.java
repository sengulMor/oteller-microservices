package com.oteller.hotelservice.services;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }



    @Transactional
    public HotelDto getById(Long id){
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        return getHotelDto(hotel);
    }

    @Transactional
    public HotelDto create(HotelDto dto){
        Address address = getAddress(dto);
        Hotel hotel = getHotel(dto, address);
        hotelRepository.save(hotel);
        return getHotelDto(hotel);
    }

    @Transactional
    public List<HotelDto> getAllHotels() {
           List<Hotel> hotels = hotelRepository.findAll();
           return hotels.stream()
                   .map(this::getHotelDto)
                   .collect(Collectors.toList());
    }

    private Address getAddress(HotelDto dto) {
        Address address = new Address();
        address.setStreet(dto.getAddressDto().getStreet());
        address.setCity(dto.getAddressDto().getCity());
        address.setCountry(dto.getAddressDto().getCountry());
        return address;
    }

    private Hotel getHotel(HotelDto dto, Address address) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setStarRating(dto.getStarRating());
        hotel.setAddress(address);
        return hotel;
    }

    private HotelDto getHotelDto(Hotel hotel) {
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .starRating(hotel.getStarRating())
                .addressDto(getAddressDto(hotel)).build();
    }

    private AddressDto getAddressDto(Hotel hotel){
        return AddressDto.builder()
                .id(hotel.getAddress().getId())
                .street(hotel.getAddress().getStreet())
                .city(hotel.getAddress().getCity())
                .country(hotel.getAddress().getCountry())
                .build();
    }
}
