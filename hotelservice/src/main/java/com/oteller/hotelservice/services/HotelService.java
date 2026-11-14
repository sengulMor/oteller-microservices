package com.oteller.hotelservice.services;

import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.exception.HotelNotFoundException;
import com.oteller.hotelservice.exception.HotelUpdateException;
import com.oteller.hotelservice.mapper.HotelMapper;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    @Transactional
    public HotelDto create(HotelDto dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toDto(savedHotel);
    }

    @Transactional(readOnly = true)
    public List<HotelDto> getAllHotels() {
        return hotelMapper.toDtoList(hotelRepository.findAll());
    }

    @Transactional(readOnly = true)
    public HotelDto getById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        return hotelMapper.toDto(hotel);
    }

    @Transactional
    public void deleteById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        hotelRepository.delete(hotel);
    }

    @Transactional
    public HotelDto update(HotelDto dto) {
        try {
            Hotel existingHotel = hotelRepository.findById(dto.getId())
                    .orElseThrow(() -> new HotelNotFoundException(dto.getId()));
            hotelMapper.update(existingHotel, dto);
            Hotel updatedHotel = hotelRepository.save(existingHotel);
            log.info("Hotel with id={} successfully updated.", dto.getId());
            return hotelMapper.toDto(updatedHotel);
        } catch (DataIntegrityViolationException ex) {
            log.error("Update failed due to constraint violation: {}", ex.getMessage());
            throw new HotelUpdateException(dto.getId(), ex);
        }
    }
}
