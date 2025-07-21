package com.oteller.hotelservice.services;

import com.oteller.enums.ReservationStatus;
import com.oteller.events.RoomReservedEvent;
import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.exception.HotelNotFoundException;
import com.oteller.hotelservice.exception.RoomNotFoundException;
import com.oteller.hotelservice.kafka.producer.RoomReservedEventProducer;
import com.oteller.hotelservice.mapper.RoomMapper;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.model.Room;
import com.oteller.hotelservice.repository.HotelRepository;
import com.oteller.hotelservice.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomReservedEventProducer roomReservedEventProducer;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomReservedEventProducer roomReservedEventProducer,
                       HotelRepository hotelRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomReservedEventProducer = roomReservedEventProducer;
        this.hotelRepository = hotelRepository;
        this.roomMapper = roomMapper;
    }

    @Transactional
    public RoomDto create(RoomDto roomDto) {
        Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                .orElseThrow(() -> new HotelNotFoundException(roomDto.getHotelId()));

        Room room = roomMapper.toEntity(roomDto, hotel);
        Room savedRoom = roomRepository.save(room);
        return roomMapper.toDto(savedRoom);
    }

    @Transactional(readOnly = true)
    public RoomDto getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        return roomMapper.toDto(room);
    }

    @Transactional(readOnly = true)
    public List<RoomDto> getAllRoomsOfHotel(Long hotelId) {
        List<Room> rooms = roomRepository.findAllByHotelId(hotelId);
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException(hotelId);
        }
        return roomMapper.toDtoList(rooms);
    }


    @Transactional
    public void deleteById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        roomRepository.delete(room);
    }

    @Transactional
    public RoomDto update(RoomDto dto) {
        Room existingRoom = roomRepository.findByIdAndHotel_Id(dto.getId(), dto.getHotelId())
                .orElseThrow(() -> new RoomNotFoundException(dto.getId()));
        roomMapper.update(existingRoom, dto);
        Room updatedRoom = roomRepository.save(existingRoom);
        return roomMapper.toDto(updatedRoom);
    }




    /*     * Checks if the room is available before reserve the room
     * In this transaction happens two things, first the room will be locked by fetching the room,
     * to avoid other threads access it and second room will be saved here
     * Other threads can only access the data after finishing the transaction,
     * which happens by leaving the method
     * After saving the room, a kafka event will be sent,
     * so that other services(consumer is here Reservation Service)
     * can listen to this event to finish the reservation*/


    @Transactional
    public boolean reserveIfAvailable(RoomAvailabilityDTO dto) {
        Room room = roomRepository.findRoomForUpdate(dto.getRoomId(), dto.getHotelId())
                .orElseThrow(() -> new RoomNotFoundException(dto.getRoomId()));
        if (!room.isAvailable()) {
            return false;
        }
        reserveRoom(dto, room);
        sendEvent(dto);
        return true;
    }

    private void reserveRoom(RoomAvailabilityDTO dto, Room room) {
        room.setAvailable(false);
        room.setGuestName(dto.getGuestName());
        room.setCheckInDate(dto.getCheckInDate());
        room.setCheckOutDate(dto.getCheckOutDate());
        roomRepository.save(room);
    }

    private void sendEvent(RoomAvailabilityDTO dto) {
        RoomReservedEvent event = getRoomReservedEvent(dto);
        roomReservedEventProducer.sendRoomReservedEvent(event);
    }

    private RoomReservedEvent getRoomReservedEvent(RoomAvailabilityDTO dto) {
        return new RoomReservedEvent(dto.getHotelId(), dto.getRoomId(),
                dto.getGuestName(), dto.getCheckInDate(), dto.getCheckOutDate(), ReservationStatus.CONFIRMED, dto.getEmail());
    }
}
