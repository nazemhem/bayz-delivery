package com.bayzdelivery.service;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.model.Delivery;
import com.bayzdelivery.model.enums.DeliveryStatus;

import java.util.List;

public interface DeliveryService {

    Delivery createNewDelivery(Delivery delivery);

    Delivery findById(Long deliveryId);

    List<Delivery> getAll();

    Delivery updateDelivery(Delivery delivery, Courier courier, DeliveryStatus status, Long distance);
}
