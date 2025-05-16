package com.growthon.domain.produce.repository;

import com.growthon.domain.produce.domain.Produce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
    Produce findByProduceId(long produceId);
}
