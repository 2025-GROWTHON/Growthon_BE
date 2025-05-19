package com.growthon.domain.produce.repository;

import com.growthon.domain.produce.domain.Produce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
    Optional<Produce> findByProduceId(Long produceId);
}
