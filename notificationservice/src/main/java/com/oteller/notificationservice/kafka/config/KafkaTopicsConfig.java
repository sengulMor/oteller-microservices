package com.oteller.notificationservice.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka.topic")
public class KafkaTopicsConfig {

    private String reservationCreated;
}
