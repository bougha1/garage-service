package com.renault.garage.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.kafka.event.VehicleCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VehicleConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "vehicle-created-topic", groupId = "vehicle-group")
    public void consume(String payload) {
        try {
            VehicleCreatedEvent event =
                    objectMapper.readValue(payload, VehicleCreatedEvent.class);

            System.out.println("Received event: " + event);

        } catch (Exception e) {
            throw new RuntimeException("Kafka deserialization error", e);
        }
    }
}
