package com.example.exoplanetes.repositories;

import com.example.exoplanetes.entities.Observatoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservatoireRepository extends JpaRepository<Observatoire, Long> {
}
