package com.oteller.reservationservice.model;

import com.oteller.enums.ReservationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Reservation extends BaseEntity {

    @NotNull
    private Long hotelId;

    @NotNull
    private Long roomId;

    @NotBlank
    private String guestName;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    private ReservationStatus status;
}

