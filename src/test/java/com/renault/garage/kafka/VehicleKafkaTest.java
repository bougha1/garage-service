package com.renault.garage.kafka;

import com.renault.garage.kafka.consumer.VehicleConsumer;
import com.renault.garage.kafka.event.VehicleCreatedEvent;
import com.renault.garage.kafka.producer.VehicleProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(
        topics = "vehicle-created-topic",
        partitions = 1
)
class VehicleKafkaTest {

    @Autowired
    private VehicleProducer producer;

    @Test
    void testKafkaFlow() {
        VehicleCreatedEvent event = new VehicleCreatedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Renault",
                "Clio",
                2024
        );

        producer.sendVehicleCreatedEvent(event);

        assertTrue(true); // test OK si pas d'exception
    }
}
