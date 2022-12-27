package com.bayzdelivery.service;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.model.Delivery;
import com.bayzdelivery.model.enums.DeliveryStatus;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.List;

public interface DeliveryService {

    Delivery createNewDelivery(Delivery delivery);

    Delivery findById(Long deliveryId);

    List<Delivery> getAll();

    List<Delivery> getAllBetweenPeriod(Instant st, Instant et);

    AbstractMap.SimpleEntry<String, String> getStat(String statName, Instant st, Instant et);

    Delivery updateDelivery(Delivery delivery, Courier courier, DeliveryStatus status, Long distance);
}
