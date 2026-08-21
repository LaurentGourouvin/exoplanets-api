package com.example.exoplanetes.entities;

import jakarta.persistence.*;

@Entity
@Table(name="observatoire")
public class Observatoire {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="nom", length = 120, nullable = false, unique = true)
    private String nom;

    @Column(name="pays", length = 80, nullable = false)
    private String pays;

    @Column(name="altitude_m", nullable = false)
    private Integer altitudeM;

    public Observatoire() {}

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setAltitudeM(Integer altitudeM) {
        this.altitudeM = altitudeM;
    }

    public String getPays() {
        return pays;
    }

    public Integer getAltitudeM() {
        return altitudeM;
    }
}
