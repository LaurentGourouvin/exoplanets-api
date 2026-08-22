package com.example.exoplanetes.repositories;

import com.example.exoplanetes.entities.Exoplanete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExoplaneteRepository extends JpaRepository<Exoplanete, Long> {
    // https://docs.spring.io/spring-data/jpa/reference/repositories/query-keywords-reference.html
    Page<Exoplanete> findByObservatoireId(Long observatoireId, Pageable pageable);

    boolean existsByDesignationAndIdNot(String designation, Long id);
}
