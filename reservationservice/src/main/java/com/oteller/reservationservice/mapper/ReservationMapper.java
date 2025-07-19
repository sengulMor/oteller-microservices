package com.oteller.reservationservice.mapper;

import com.oteller.events.RoomReservedEvent;
import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.model.Reservation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    RoomReservedEvent toEvent(Reservation reservation);

    Reservation toEntity(RoomReservedEvent roomReservedEvent);

    ReservationDTO toDTO(Reservation reservation);

    List<ReservationDTO> toDtoList(List<Reservation> reservations);

}
