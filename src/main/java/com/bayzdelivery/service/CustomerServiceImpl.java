package com.bayzdelivery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bayzdelivery.model.Customer;
import com.bayzdelivery.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customerList = new ArrayList<>();
        customerRepository.findAll().forEach(customerList::add);
        return customerList;
    }

    public Customer save(Customer c) {
        return customerRepository.save(c);
    }

    @Override
    public Customer findById(Long customerId) {
        Optional<Customer> dbCustomer = customerRepository.findById(customerId);
        return dbCustomer.orElse(null);
    }


}
