package com.renault.garage.repository;

import com.renault.garage.entity.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OpeningTimeRepository extends JpaRepository<OpeningTime, UUID> {
}
