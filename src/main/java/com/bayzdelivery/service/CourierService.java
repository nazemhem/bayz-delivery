package com.bayzdelivery.service;

import com.bayzdelivery.model.Courier;

import java.util.List;


public interface CourierService {
    public List<Courier> getAll();

    public Courier save(Courier c);

    public Courier findById(Long courierId);
}
