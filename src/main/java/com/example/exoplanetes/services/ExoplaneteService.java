package com.example.exoplanetes.services;

import com.example.exoplanetes.dto.CreateExoplaneteRequest;
import com.example.exoplanetes.dto.ExoplaneteResponse;
import com.example.exoplanetes.dto.UpdateExoplaneteDto;
import com.example.exoplanetes.entities.Exoplanete;
import com.example.exoplanetes.entities.Observatoire;
import com.example.exoplanetes.enums.Statut;
import com.example.exoplanetes.exceptions.DesignationAlreadyExistsException;
import com.example.exoplanetes.exceptions.ExoplaneteNotFound;
import com.example.exoplanetes.exceptions.ObservatoireNotFound;
import com.example.exoplanetes.repositories.ExoplaneteRepository;
import com.example.exoplanetes.repositories.ObservatoireRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    @Transactional(readOnly = true)
    public ExoplaneteResponse getById(Long id) {
        Exoplanete exoplanete = this.exoplaneteRepository.findById(id).orElseThrow(() -> new ExoplaneteNotFound("Exoplanete not found with id " + id));
        return toResponse(exoplanete);
    }

    @Transactional(readOnly = true)
    public Page<ExoplaneteResponse> getList(Long obsId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Exoplanete> find;

        if (Objects.isNull(obsId)) {
            find = this.exoplaneteRepository.findAll(pageable);
        } else {
            find = this.exoplaneteRepository.findByObservatoireId(obsId, pageable);
        }

        return find.map(this::toResponse);
    }

    @Transactional
    public ExoplaneteResponse update(Long id, UpdateExoplaneteDto request) {
        Exoplanete exoplanete = this.exoplaneteRepository.findById(id)
                .orElseThrow(() -> new ExoplaneteNotFound("Exoplanete not found"));

        if (request.designation() != null) {
            boolean designationExists = this.exoplaneteRepository.existsByDesignationAndIdNot(request.designation(), id);
            if (designationExists) {
                throw new DesignationAlreadyExistsException("Designation already exist");
            }
            exoplanete.setDesignation(request.designation());
        }

        if (request.observatoireId() != null) {
            Observatoire observatoire = this.observatoireRepository.findById(request.observatoireId())
                    .orElseThrow(() -> new ObservatoireNotFound("Observatoire not found"));

            exoplanete.setObservatoire(observatoire);
        }

        if (request.masseTerre() != null) {
            exoplanete.setMasseTerre(request.masseTerre());
        }

        if (request.distanceAl() != null) {
            exoplanete.setDistanceAl(request.distanceAl());
        }

        return toResponse(exoplanete);
    }
}
