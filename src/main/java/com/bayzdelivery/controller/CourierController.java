package com.bayzdelivery.controller;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public ResponseEntity<?> getAll(@RequestParam(value = "stat", required = false) String stat,
                                    @RequestParam(value = "start_time", required = false) String st,
                                    @RequestParam(value = "end_time", required = false) String et,
                                    @RequestParam(value = "count", required = false) Integer count) {
        Instant startTime = null;
        Instant endTime = null;
        if (stat != null)
            try {
                if (st == null) return ResponseEntity.badRequest().body("You must provide start time");
                startTime = LocalDateTime.parse(st, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.of("Africa/Cairo")).toInstant();
                if (et == null) return ResponseEntity.badRequest().body("You must provide end time");
                endTime = LocalDateTime.parse(et, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(ZoneId.of("Africa/Cairo")).toInstant();
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Invalid time format. Format should be: dd/MM/yyyy HH:mm:ss");
            }

        return ResponseEntity.ok(courierService.getAll(stat, startTime, endTime, count));
    }

    @GetMapping("/courier/{courier_id}")
    public ResponseEntity<Courier> getCourierById(@PathVariable("courier_id") Long courierId) {
        Courier courier = courierService.findById(courierId);
        if (courier != null) return ResponseEntity.ok(courier);
        else return ResponseEntity.notFound().build();
    }


}
