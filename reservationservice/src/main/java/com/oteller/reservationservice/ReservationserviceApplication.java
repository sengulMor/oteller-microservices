package com.oteller.reservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReservationserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationserviceApplication.class, args);
    }

}
