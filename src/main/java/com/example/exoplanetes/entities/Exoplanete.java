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


}
