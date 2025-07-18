package com.oteller.hotelservice;

import com.oteller.hotelservice.kafka.config.KafkaTopicsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableConfigurationProperties(KafkaTopicsConfig.class)
@SpringBootApplication
@EnableJpaAuditing
public class HotelserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelserviceApplication.class, args);
	}

}
