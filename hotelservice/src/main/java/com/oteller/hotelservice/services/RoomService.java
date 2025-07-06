package com.oteller.hotelservice.services;

import com.oteller.events.RoomReservedEvent;
import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.model.Room;
import com.oteller.hotelservice.repository.HotelRepository;
import com.oteller.hotelservice.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final KafkaProducer kafkaProducer;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository, KafkaProducer kafkaProducer,  HotelRepository hotelRepository){
        this.roomRepository = roomRepository;
        this.kafkaProducer = kafkaProducer;
        this.hotelRepository = hotelRepository;
    }

    /**
     * Checks if the room is available before reserve the room
     * In this transaction happens two things, first the room will be locked by fetching the room,
     * to avoid other threads access it and second room will be saved here
     * Other threads can only access the data after finishing the transaction,
     * which happens by leaving the method
     * After saving the room, a kafka event will be sent,
     * so that other services(consumer is here Reservation Service)
     * can listen to this event to finish the reservation
     */
    @Transactional
    public Boolean isRoomAvailable(RoomAvailabilityDTO request) {
        Room room = roomRepository.findRoomForUpdate(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        if (!room.isAvailable()) {
            return false;
        }
        room.setReserved(true);
        roomRepository.save(room);

        RoomReservedEvent event = new RoomReservedEvent(request.getRoomId(), "CREATED");
        kafkaProducer.sendRoomReservedEvent(event);
        return true;
    }

    @Transactional
    public RoomDto create(RoomDto roomDto){
        Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        Room room = getRoom(roomDto);
        room.setHotel(hotel);
        roomRepository.save(room);
        return getRoomDto(room);
    }

    @Transactional
    public RoomDto getById(Long id){
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        return getRoomDto(room);
    }

    @Transactional
    public List<RoomDto> getAllRoomsOfHotel(Long hotelId){
        List<Room> rooms = roomRepository.findAllByHotelId(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        return rooms.stream().map(this::getRoomDto).collect(Collectors.toList());
    }

    private RoomDto getRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .capacity(room.getCapacity())
                .pricePerNight(room.getPricePerNight())
                .available(room.isAvailable())
                .hotelId(room.getHotel().getId())
                .reserved(room.isReserved()).build();
    }

    private Room getRoom(RoomDto roomDto) {
        Room room = new Room();
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setCapacity(roomDto.getCapacity());
        room.setPricePerNight(roomDto.getPricePerNight());
        room.setAvailable(true);
        room.setReserved(false);
        return room;
    }
}
