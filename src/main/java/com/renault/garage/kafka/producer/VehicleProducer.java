package com.renault.garage.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.kafka.event.VehicleCreatedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehicleProducer {

    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VehicleProducer(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVehicleCreatedEvent(VehicleCreatedEvent event) {
       try {
           String payload = objectMapper.writeValueAsString(event);

           kafkaTemplate.send(
             "vehicle-created-topic",
             null,
             payload
           );
       } catch (JsonProcessingException e) {
          throw new RuntimeException("Kafka serialization error", e);
       }
    }
}
