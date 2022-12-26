package com.bayzdelivery.service;

import java.util.List;

import com.bayzdelivery.model.Customer;

public interface CustomerService {
  public List<Customer> getAll();

  public Customer save(Customer c);

  public Customer findById(Long customerId);

}
