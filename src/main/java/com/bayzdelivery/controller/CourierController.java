package com.bayzdelivery.controller;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourierController {
    private final CourierService courierService;

    @Autowired
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @PostMapping("/courier")
    public ResponseEntity<Courier> register(@RequestBody Courier courier) {
        return ResponseEntity.ok(courierService.save(courier));
    }

    @GetMapping("/courier")
    public ResponseEntity<List<Courier>> getAll() {
        return ResponseEntity.ok(courierService.getAll());
    }

    @GetMapping("/courier/{courier_id}")
    public ResponseEntity<Courier> getCourierById(@PathVariable("courier_id") Long courierId) {
        Courier courier = courierService.findById(courierId);
        if (courier != null) return ResponseEntity.ok(courier);
        else return ResponseEntity.notFound().build();
    }
}
