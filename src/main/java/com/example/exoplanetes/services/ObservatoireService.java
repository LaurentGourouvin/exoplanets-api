package com.example.exoplanetes.services;

import com.example.exoplanetes.dto.CreateObservatoireRequest;
import com.example.exoplanetes.dto.ObservatoireResponse;
import com.example.exoplanetes.entities.Observatoire;
import com.example.exoplanetes.exceptions.ObservatoireNotFound;
import com.example.exoplanetes.repositories.ObservatoireRepository;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class ObservatoireService {

    private final ObservatoireRepository observatoireRepository;

    public ObservatoireService(ObservatoireRepository observatoireRepository) {
        this.observatoireRepository = observatoireRepository;
    }

    private ObservatoireResponse toResponse(Observatoire o) {
        return new ObservatoireResponse(o.getId(), o.getNom(), o.getPays(), o.getAltitudeM());
    }

    private Observatoire fromCreateObservatoireRequestToEntity(CreateObservatoireRequest data) {
        Observatoire observatoire = new Observatoire();
        observatoire.setNom(data.nom());
        observatoire.setPays(data.pays());
        observatoire.setAltitudeM(data.altitudeM());

        return observatoire;
    }

    public List<ObservatoireResponse> list() {
        return this.observatoireRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ObservatoireResponse getById(Long id) {
        Observatoire observatoire = this.observatoireRepository.findById(id).orElseThrow(() -> new ObservatoireNotFound("Observatoire not found"));
        return toResponse(observatoire);
    }

    public ObservatoireResponse create(CreateObservatoireRequest request) {
        Observatoire observatoire = this.observatoireRepository.save(fromCreateObservatoireRequestToEntity(request));
        return toResponse(observatoire);
    }
}
