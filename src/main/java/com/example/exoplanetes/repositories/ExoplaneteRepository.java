package com.example.exoplanetes.repositories;

import com.example.exoplanetes.entities.Exoplanete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExoplaneteRepository extends JpaRepository<Exoplanete, Long> {
}
