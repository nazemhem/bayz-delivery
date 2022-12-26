package com.bayzdelivery.repositories;

import com.bayzdelivery.model.Courier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface CourierRepository extends PagingAndSortingRepository<Courier, Long> {
}
