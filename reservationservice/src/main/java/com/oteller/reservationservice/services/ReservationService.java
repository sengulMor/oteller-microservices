package com.oteller.reservationservice.services;

import com.oteller.events.ReservationCreatedEvent;
import com.oteller.events.RoomReservedEvent;
import com.oteller.reservationservice.config.HotelServiceConfig;
import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.model.Reservation;
import com.oteller.reservationservice.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final KafkaProducer kafkaProducer;
    private final WebClient webClient;

    public ReservationService(ReservationRepository reservationRepository,
                              WebClient.Builder builder,
                              HotelServiceConfig config,
                              KafkaProducer kafkaProducer) {
        this.reservationRepository = reservationRepository;
        this.kafkaProducer = kafkaProducer;
        this.webClient = builder.baseUrl(config.getBaseUrl()).build();
    }

    @Transactional
    public ReservationDTO getById(Long id){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserevation not found"));
        return getReservationDTO(reservation);
    }

    @Transactional
    public List<ReservationDTO> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(this::getReservationDTO).collect(Collectors.toList());
    }

    public String  finishReservation(ReservationDTO request){
        Boolean available = webClient.post()
                .uri("/rooms/check-availability")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        if (Boolean.FALSE.equals(available)) {
            return "Room is not available for selected dates.";
        }
        return "Reservation successful.";
    }

    /**
     * This Listener consumes here the room-reserved-events,
     * after hotel-service as producer created the event by successfully reserving the room,
     * reservation-service listens to this event and create the reservation to finish it.
     * After reservation has been created successfully, a different event the reservation-created-events will send,
     * notification-service acts now as consumer of this event to send an e-mail to the customer,
     * that the room is now reserved for her.
     *
     */
    @KafkaListener(topics = "room-reserved-events", groupId = "reservation-group")
    public void handleRoomReservedEvent(RoomReservedEvent dto) {
        //comsume room-reserved-events
        Reservation reservation = getReservation(dto);

        //produce reservation-created-events
        ReservationCreatedEvent event = new ReservationCreatedEvent(reservation.getId(), "CREATED");
        kafkaProducer.sendReservationCreatedEvent(event);

        log.info("Reservation created for room: {}", dto.getRoomId());
    }

    @Transactional
    protected Reservation getReservation(RoomReservedEvent dto) {
        Reservation reservation = new Reservation();
        reservation.setRoomId(dto.getRoomId());
        reservationRepository.save(reservation);
        return reservation;
    }

    private ReservationDTO getReservationDTO(Reservation reservation) {
        return ReservationDTO.builder().id(reservation.getId()).roomId(reservation.getRoomId()).build();
    }
}
