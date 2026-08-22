package com.example.exoplanetes.entities;

import com.example.exoplanetes.enums.Statut;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="exoplanete")
public class Exoplanete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="designation", length = 80, nullable = false, unique = true)
    private String designation;

    @Column(name="masse_terre", nullable = false, precision = 10, scale = 3)
    private BigDecimal masseTerre;

    @Column(name="distance_al", nullable = false, precision = 10, scale = 2)
    private BigDecimal distanceAl;

    @Column(name="statut", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @JoinColumn(name = "observatoire_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Observatoire observatoire;

    @Version
    private Long version;

    public Exoplanete() {
        //Empty constructor for JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getMasseTerre() {
        return masseTerre;
    }

    public void setMasseTerre(BigDecimal masseTerre) {
        this.masseTerre = masseTerre;
    }

    public BigDecimal getDistanceAl() {
        return distanceAl;
    }

    public void setDistanceAl(BigDecimal distanceAl) {
        this.distanceAl = distanceAl;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Observatoire getObservatoire() {
        return observatoire;
    }

    public void setObservatoire(Observatoire observatoire) {
        this.observatoire = observatoire;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
