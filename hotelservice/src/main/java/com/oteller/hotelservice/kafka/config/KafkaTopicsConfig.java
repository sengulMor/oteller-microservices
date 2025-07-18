package com.oteller.hotelservice.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka.topic")
public class KafkaTopicsConfig {
    private String roomReserved;
}
