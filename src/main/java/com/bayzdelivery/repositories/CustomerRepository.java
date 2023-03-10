package com.bayzdelivery.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import com.bayzdelivery.model.Customer;

@RestResource(exported=false)
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}
