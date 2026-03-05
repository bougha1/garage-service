package com.renault.garage.repository;

import com.renault.garage.entity.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long> {
}
