package com.bayzdelivery.service;

import com.bayzdelivery.model.Courier;

import java.time.Instant;
import java.util.List;


public interface CourierService {
    public List<Courier> getAll();

    public List<Courier> getAll(String stat, Instant st, Instant et, Integer count);

    public Courier save(Courier c);

    public Courier findById(Long courierId);
}
