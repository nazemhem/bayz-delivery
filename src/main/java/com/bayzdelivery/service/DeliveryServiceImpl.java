package com.bayzdelivery.service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.bayzdelivery.jobs.DelayedDeliveryNotifier;
import com.bayzdelivery.model.Courier;
import com.bayzdelivery.model.enums.DeliveryStatus;
import com.bayzdelivery.repositories.DeliveryRepository;
import com.bayzdelivery.model.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DelayedDeliveryNotifier notifier;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, DelayedDeliveryNotifier notifier) {
        this.deliveryRepository = deliveryRepository;
        this.notifier = notifier;
    }


    public List<Delivery> getAll() {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        deliveryRepository.findAll().forEach(deliveries::add);
        return deliveries;
    }

    @Override
    @Scheduled(fixedDelay = 30000)
    public List<Delivery> checkDelayed() {
        List<Delivery> delayed = deliveryRepository.findDelayed();
        if (delayed != null && delayed.size() > 0) {
            System.out.println("Deliveries with ids: " +
                    delayed.stream().mapToLong(Delivery::getId).boxed().collect(Collectors.toList()) + " are delayed. " + Thread.currentThread().getName());
            notifier.notifyCustomerSupport();
        }
        return delayed;
    }

    @Override
    public List<Delivery> getBetweenPeriod(Instant st, Instant et) {
        return deliveryRepository.findBetweenPeriod(st, et);
    }

    @Override
    public AbstractMap.SimpleEntry<String, String> getStat(String statName, Instant st, Instant et) {
        List<Delivery> list;
        if (st == null || et == null)
            list = getAll();
        else
            list = getBetweenPeriod(st, et);
        if (statName.equals("commission_avg")) {
            return new AbstractMap.SimpleEntry<>("Average Commission", "" + list.stream().mapToDouble(Delivery::getCommission).average().orElse(-1));
        }
        return null;
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
