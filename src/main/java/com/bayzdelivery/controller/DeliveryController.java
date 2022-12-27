package com.bayzdelivery.controller;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.model.Delivery;
import com.bayzdelivery.model.enums.DeliveryStatus;
import com.bayzdelivery.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bayzdelivery.service.DeliveryService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;

@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    private final CourierService courierService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService, CourierService courierService) {
        this.deliveryService = deliveryService;
        this.courierService = courierService;
    }

    @GetMapping(path = "/delivery")
    public ResponseEntity<List<Delivery>> getAll() {
        return ResponseEntity.ok(deliveryService.getAll());
    }

    @PostMapping(path = "/delivery")
    public ResponseEntity<Delivery> createNewDelivery(@RequestBody Delivery delivery) {
        return ResponseEntity.ok(deliveryService.createNewDelivery(delivery));
    }

    @GetMapping(path = "/delivery/{delivery-id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable(name = "delivery-id", required = true) Long deliveryId) {
        Delivery delivery = deliveryService.findById(deliveryId);
        if (delivery != null) return ResponseEntity.ok(delivery);
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/delivery/{delivery-id}")
    public ResponseEntity<?> updateDelivery(@PathVariable(name = "delivery-id", required = true) Long deliveryId, @RequestParam("courier_id") Long courierId, @RequestParam("status") String status, @RequestParam(value = "distance", required = false) Long distance) {
        //check if delivery is valid
        Delivery deliveryDb = deliveryService.findById(deliveryId);
        if (deliveryDb == null) return ResponseEntity.badRequest().body("Delivery not found");
        //check if courier is valid
        Courier courierDb = courierService.findById(courierId);
        if (courierDb == null) return ResponseEntity.badRequest().body("Courier not found");
        //check if status is valid
        DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase());
        if (Arrays.stream(DeliveryStatus.values()).noneMatch(ds -> ds == deliveryStatus))
            return ResponseEntity.badRequest().body("Status not found.");
        //check if distance is valid
        if (deliveryStatus == DeliveryStatus.DELIVERED && (distance == null || distance <= 0))
            return ResponseEntity.badRequest().body("Distance cannot be less than 0");
        //check if courier has any other deliveries
        if (deliveryStatus == DeliveryStatus.PICKED) {
            if (deliveryService.getAll().stream().anyMatch(delivery -> delivery.getStatus() == DeliveryStatus.PICKED && delivery.getCourier().getId().equals(courierId)))
                return ResponseEntity.badRequest().body("A courier is not allowed to deliver multiple orders at the same time");
        }

        Delivery delivery = deliveryService.updateDelivery(deliveryDb, courierDb, deliveryStatus, distance);
        if (delivery == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error has occurred");
        return ResponseEntity.ok(delivery);
    }


    @GetMapping(path = "/delivery/stats")
    public ResponseEntity<?> getDeliveryStat(@RequestParam("stat") String statName,
                                             @RequestParam(value = "start_time", required = false) String st,
                                             @RequestParam(value = "end_time", required = false) String et) {
        Instant startTime = null;
        Instant endTime = null;
        try {
            if (st != null)
                startTime = LocalDateTime.parse(st, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.of("Africa/Cairo")).toInstant();
            if (et != null)
                endTime = LocalDateTime.parse(et, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.of("Africa/Cairo")).toInstant();
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid time format. Format should be: dd/MM/yyyy HH:mm:ss");
        }
        AbstractMap.SimpleEntry<String, String> d = deliveryService.getStat(statName, startTime, endTime);
        if (d == null) return ResponseEntity.badRequest().body("This statName is not implemented");
        return ResponseEntity.ok(d);
    }
}
