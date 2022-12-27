package com.bayzdelivery.service;

import com.bayzdelivery.model.Courier;
import com.bayzdelivery.repositories.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourierServiceImpl implements CourierService {
    private final CourierRepository courierRepository;

    @Autowired
    public CourierServiceImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public List<Courier> getAll() {
        List<Courier> courierList = new ArrayList<>();
        courierRepository.findAll().forEach(courierList::add);
        return courierList;
    }

    @Override
    public List<Courier> getAll(String stat, Instant st, Instant et, Integer count) {
        if (stat != null) {
            if (count == null) count = 3;
            if (stat.equals("most_commission")) {
                return courierRepository.findByMostCommission(st, et).stream().limit(count).collect(Collectors.toList());
            } //can add other stats later on
        }
        return getAll();
    }

    @Override
    public Courier save(Courier c) {
        return courierRepository.save(c);
    }

    @Override
    public Courier findById(Long courierId) {
        Optional<Courier> dbCourier = courierRepository.findById(courierId);
        return dbCourier.orElse(null);
    }
}
