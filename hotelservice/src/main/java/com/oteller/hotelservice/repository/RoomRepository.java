package com.oteller.hotelservice.repository;

import com.oteller.hotelservice.model.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.id = :roomId AND r.hotel.id = :hotelId")
    Optional<Room> findRoomForUpdate(@Param("roomId") Long roomId, @Param("hotelId") Long hotelId);

    List<Room> findAllByHotelId(Long hotelId);

    Optional<Room> findByHotel_IdAndRoomNumber(Long hotelId, String roomNumber);

    Optional<Room> findByIdAndHotel_Id(Long id, Long hotelId);


}
