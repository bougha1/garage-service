package com.renault.garage.kafka;

import com.renault.garage.kafka.event.VehicleCreatedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Profile("test")
public class TestVehicleConsumer {

    private CountDownLatch latch = new CountDownLatch(1);
    private VehicleCreatedEvent receivedEvent;

    @KafkaListener(topics = "vehicles-topic", groupId = "test-group")
    public void consume(VehicleCreatedEvent event) {
        this.receivedEvent = event;
        latch.countDown();
        System.out.println("Message reçu : " + event);
    }

    public CountDownLatch getLatch() { return latch; }
    public VehicleCreatedEvent getReceivedEvent() { return receivedEvent; }
}
