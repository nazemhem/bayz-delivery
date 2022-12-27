package com.bayzdelivery.repositories;

import com.bayzdelivery.model.Delivery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.Instant;
import java.util.List;

@RestResource(exported = false)
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    @Query("SELECT d FROM Delivery d WHERE d.startTime >= :st AND d.endTime <= :et")
    List<Delivery> findBetweenPeriod(Instant st, Instant et);

    @Query(value = "SELECT * from delivery WHERE start_time  + (45 * interval '1 minute') < current_timestamp AND status = 'PICKED'", nativeQuery = true)
    List<Delivery> findDelayed();
}
