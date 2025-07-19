package com.oteller.reservationservice.services;

import com.oteller.events.RoomReservedEvent;
import com.oteller.reservationservice.mapper.ReservationMapper;
import com.oteller.reservationservice.model.Reservation;
import com.oteller.reservationservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationTransactionalService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public ReservationTransactionalService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    public Long createReservation(RoomReservedEvent roomReservedEvent) {
        Reservation reservation = reservationMapper.toEntity(roomReservedEvent);
        reservation = reservationRepository.save(reservation);
        return reservation.getId();
    }
}
