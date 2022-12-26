package com.bayzdelivery.controller;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourierController {
    private final CourierService courierService;

    @Autowired
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @PostMapping("/api/courier")
    public ResponseEntity<Courier> register(Courier courier) {
        return ResponseEntity.ok(courierService.save(courier));
    }

    @GetMapping("/api/courier")
    public ResponseEntity<List<Courier>> getAll() {
        return ResponseEntity.ok(courierService.getAll());
    }

    @GetMapping("/api/courier/{courier_id}")
    public ResponseEntity<Courier> getCourierById(@PathVariable("courier_id") Long courierId) {
        Courier courier = courierService.findById(courierId);
        if (courier != null) return ResponseEntity.ok(courier);
        else return ResponseEntity.notFound().build();
    }
}
