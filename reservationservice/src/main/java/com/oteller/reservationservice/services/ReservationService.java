package com.oteller.reservationservice.services;

import com.oteller.reservationservice.config.ReservationServiceConfig;
import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.exception.ReservationNotFoundException;
import com.oteller.events.RoomReleasedEvent;
import com.oteller.enums.ReservationStatus;
import com.oteller.reservationservice.kafka.producer.RoomReleasedEventProducer;
import com.oteller.reservationservice.mapper.ReservationMapper;
import com.oteller.reservationservice.model.Reservation;
import com.oteller.reservationservice.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class ReservationService {

    private final WebClient webClient;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RoomReleasedEventProducer roomReleasedEventProducer;

    public ReservationService(WebClient.Builder builder,
                              ReservationServiceConfig config,
                              ReservationRepository reservationRepository,
                              ReservationMapper reservationMapper,
                              RoomReleasedEventProducer roomReleasedEventProducer) {
        this.webClient = builder.baseUrl(config.getBaseUrl()).build();
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.roomReleasedEventProducer = roomReleasedEventProducer;
    }

    public String finishReservation(ReservationDTO request) {
        boolean available = false;
        try {
            available = webClient.post()
                    .uri("/rooms/check-availability")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Error from Hotel Service: {}", clientResponse.statusCode());
                        return Mono.error(new IllegalStateException("Hotel Service error"));
                    })
                    .bodyToMono(Boolean.class)
                    .blockOptional()
                    .orElse(false);
        } catch (Exception ex) {
            log.error("Hotel Service call failed", ex);
        }

        if (!available) {
            return "Room is not available for selected dates.";
        }
        return "Reservation successful.";


    }

    @Transactional(readOnly = true)
    public ReservationDTO getById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return reservationMapper.toDTO(reservation);
    }


    @Transactional(readOnly = true)
    public List<ReservationDTO> getAllReservations() {
        return reservationMapper.toDtoList(reservationRepository.findAll());
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        RoomReleasedEvent event = new RoomReleasedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                reservation.getId(),
                reservation.getHotelId(),
                reservation.getRoomId(),
                reservation.getGuestName(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate());
        roomReleasedEventProducer.send(event);
    }
}
