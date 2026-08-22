package com.example.exoplanetes.services;

import com.example.exoplanetes.dto.CreateExoplaneteRequest;
import com.example.exoplanetes.dto.ExoplaneteResponse;
import com.example.exoplanetes.entities.Exoplanete;
import com.example.exoplanetes.entities.Observatoire;
import com.example.exoplanetes.enums.Statut;
import com.example.exoplanetes.exceptions.ObservatoireNotFound;
import com.example.exoplanetes.repositories.ExoplaneteRepository;
import com.example.exoplanetes.repositories.ObservatoireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExoplaneteService {
    private final ExoplaneteRepository exoplaneteRepository;
    private final ObservatoireRepository observatoireRepository;

    public ExoplaneteService(ExoplaneteRepository exoplaneteRepository, ObservatoireRepository observatoireRepository) {
        this.exoplaneteRepository = exoplaneteRepository;
        this.observatoireRepository = observatoireRepository;
    }

    private ExoplaneteResponse toResponse(Exoplanete exoplanete) {
        return new ExoplaneteResponse(exoplanete.getId(), exoplanete.getDesignation(),
                exoplanete.getMasseTerre(), exoplanete.getDistanceAl(),
                exoplanete.getStatut().name(), exoplanete.getObservatoire().getId());
    }

    private Exoplanete fromCreateExoplaneteRequestToEntity(CreateExoplaneteRequest data) {
        Observatoire observatoire = this.observatoireRepository.findById(data.observatoireId())
                .orElseThrow(() -> new ObservatoireNotFound("Observatoire " + data.observatoireId() + " not found"));

        Exoplanete exoplanete = new Exoplanete();
        exoplanete.setDesignation(data.designation());
        exoplanete.setDistanceAl(data.distanceAl());
        exoplanete.setMasseTerre(data.masseTerre());
        exoplanete.setStatut(Statut.CANDIDATE);
        exoplanete.setObservatoire(observatoire);

        return exoplanete;
    }

    @Transactional
    public ExoplaneteResponse create(CreateExoplaneteRequest data) {
        Exoplanete exoplanete = this.exoplaneteRepository.save(fromCreateExoplaneteRequestToEntity(data));
        return toResponse(exoplanete);
    }
}
