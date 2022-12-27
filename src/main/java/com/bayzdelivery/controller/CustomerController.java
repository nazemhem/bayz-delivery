package com.bayzdelivery.controller;

import java.util.List;

import com.bayzdelivery.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bayzdelivery.service.CustomerService;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(path = "/customer")
    public ResponseEntity<Customer> register(@RequestBody Customer p) {
        return ResponseEntity.ok(customerService.save(p));
    }

    @GetMapping(path = "/customer")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping(path = "/customer/{customer_id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(name = "customer_id", required = true) Long customerId) {
        Customer customer = customerService.findById(customerId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

}
