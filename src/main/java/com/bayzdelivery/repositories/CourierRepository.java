package com.bayzdelivery.repositories;

import com.bayzdelivery.model.Courier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.Instant;
import java.util.List;

@RestResource(exported = false)
public interface CourierRepository extends PagingAndSortingRepository<Courier, Long> {
    @Query("SELECT c FROM Delivery d JOIN d.courier c WHERE d.startTime >= :st AND d.endTime <= :et GROUP BY c ORDER BY SUM(commission) DESC")
    List<Courier> findByMostCommission(Instant st, Instant et);
}
