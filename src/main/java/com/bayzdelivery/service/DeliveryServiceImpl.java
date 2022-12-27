package com.bayzdelivery.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.model.enums.DeliveryStatus;
import com.bayzdelivery.repositories.DeliveryRepository;
import com.bayzdelivery.model.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }


    public List<Delivery> getAll() {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        deliveryRepository.findAll().forEach(deliveries::add);
        return deliveries;
    }

    public Delivery createNewDelivery(Delivery delivery) {
        delivery.setStatus(DeliveryStatus.CREATED);
        return deliveryRepository.save(delivery);
    }

    public Delivery findById(Long deliveryId) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        return optionalDelivery.orElse(null);
    }

    public Delivery updateDelivery(Delivery delivery, Courier courier, DeliveryStatus deliveryStatus, Long distance) {
        delivery.setCourier(courier);
        delivery.setStatus(deliveryStatus);
        if (deliveryStatus == DeliveryStatus.PICKED) {
            delivery.setStartTime(Instant.now());
        } else if (deliveryStatus == DeliveryStatus.DELIVERED) {
            //calculate commission, set end time and distance
            delivery.setEndTime(Instant.now());
            delivery.setDistance(distance);
            delivery.setCommission(delivery.getPrice() * 0.05 + (distance * 0.5));
        }
        return deliveryRepository.save(delivery);
    }
}
