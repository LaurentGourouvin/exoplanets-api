INSERT INTO observatoire (nom, pays, altitude_m)
VALUES ('La Silla Observatory', 'Chili', 2400),
       ('W. M. Keck Observatory', 'Etats-Unis', 4145),
       ('Gemini South Observatory', 'Chili', 2722),
       ('Kitt Peak National Observatory', 'Etats-Unis', 2096),
       ('Palomar Observatory', 'Etats-Unis', 1712),
       ('Siding Spring Observatory', 'Australie', 1164),
       ('Calar Alto Observatory', 'Espagne', 2168),
       ('Las Campanas Observatory', 'Chili', 2380),
       ('South African Astronomical Observatory', 'Afrique du Sud', 1798),
       ('Observatoire du Teide', 'Espagne', 2390),
       ('Lick Observatory', 'Etats-Unis', 1283);

-- ---------------------------------------------------------------------
-- 50 exoplanetes reelles
-- masse_terre : masses terrestres | distance_al : annees-lumiere
-- ---------------------------------------------------------------------
INSERT INTO exoplanete (designation, masse_terre, distance_al, statut, observatoire_id, version)
VALUES
    -- Systeme TRAPPIST-1 (TRAPPIST-1e est deja dans V2)
    ('TRAPPIST-1 b', 1.374, 40.70, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('TRAPPIST-1 c', 1.308, 40.70, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('TRAPPIST-1 d', 0.388, 40.70, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('TRAPPIST-1 f', 0.934, 40.70, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('TRAPPIST-1 g', 1.148, 40.70, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('TRAPPIST-1 h', 0.331, 40.70, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),

    -- Etoiles proches (radial velocity, imagerie)
    ('55 Cancri e', 7.990, 41.00, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'Observatoire du Cerro Paranal'), 0),
    ('HD 189733 b', 363.000, 64.50, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'Observatoire de Haute-Provence'), 0),
    ('HD 40307 g', 7.100, 42.00, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('HD 85512 b', 3.600, 36.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('Gliese 667 Cc', 3.800, 23.60, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('Gliese 876 b', 715.000, 15.20, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Lick Observatory'), 0),
    ('Gliese 436 b', 22.100, 31.80, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Observatoire du Teide'), 0),
    ('Gliese 1214 b', 6.550, 47.50, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Calar Alto Observatory'), 0),
    ('Gliese 163 c', 6.800, 49.00, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('Wolf 1061 c', 3.410, 13.80, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'Observatoire du Cerro Paranal'), 0),
    ('Ross 128 b', 1.350, 11.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('LHS 1140 b', 6.380, 48.90, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Las Campanas Observatory'), 0),
    ('GJ 357 d', 6.100, 31.00, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'Las Campanas Observatory'), 0),
    ('Teegarden b', 1.050, 12.50, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Calar Alto Observatory'), 0),
    ('Teegarden c', 1.110, 12.50, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'Calar Alto Observatory'), 0),
    ('Barnard b', 3.230, 5.96, 'REJETEE', (SELECT id FROM observatoire WHERE nom = 'Observatoire du Cerro Paranal'), 0),

    -- Jupiters chauds (transits)
    ('WASP-12b', 465.000, 1410.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Palomar Observatory'), 0),
    ('WASP-17b', 156.000, 1300.00, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'South African Astronomical Observatory'), 0),
    ('WASP-121b', 371.000, 858.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Siding Spring Observatory'),
     0),
    ('WASP-39b', 89.000, 700.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Mauna Kea Observatory'), 0),
    ('WASP-96b', 152.000, 1150.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Siding Spring Observatory'),
     0),
    ('HAT-P-11b', 25.800, 123.00, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'Kitt Peak National Observatory'), 0),
    ('HAT-P-7b', 570.000, 1040.00, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'Kitt Peak National Observatory'), 0),
    ('CoRoT-7b', 5.740, 489.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Roque de los Muchachos'), 0),
    ('XO-1b', 286.000, 536.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Kitt Peak National Observatory'),
     0),
    ('TrES-2b', 379.000, 704.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Palomar Observatory'), 0),

    -- Systemes multiples proches
    ('Tau Ceti e', 3.930, 11.90, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'Observatoire du Cerro Paranal'),
     0),
    ('Tau Ceti f', 3.930, 11.90, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'Observatoire du Cerro Paranal'),
     0),
    ('Upsilon Andromedae b', 218.000, 44.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Lick Observatory'),
     0),

    -- Imagerie directe (geantes gazeuses massives)
    ('Fomalhaut b', 953.000, 25.10, 'REJETEE', (SELECT id FROM observatoire WHERE nom = 'W. M. Keck Observatory'), 0),
    ('Beta Pictoris b', 3560.000, 63.00, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'Gemini South Observatory'), 0),
    ('47 Ursae Majoris b', 816.000, 45.90, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Lick Observatory'),
     0),
    ('HR 8799 b', 2225.000, 133.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'W. M. Keck Observatory'), 0),
    ('HR 8799 c', 3180.000, 133.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'W. M. Keck Observatory'), 0),

    -- Divers
    ('PSR B1257+12 b', 0.020, 2300.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'W. M. Keck Observatory'),
     0),
    ('K2-3d', 2.800, 143.00, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'Roque de los Muchachos'), 0),
    ('GJ 1132 b', 1.660, 41.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'La Silla Observatory'), 0),
    ('Proxima Centauri c', 7.000, 4.24, 'CANDIDATE',
     (SELECT id FROM observatoire WHERE nom = 'Observatoire du Cerro Paranal'), 0),
    ('Proxima Centauri d', 0.260, 4.24, 'CANDIDATE',
     (SELECT id FROM observatoire WHERE nom = 'Observatoire du Cerro Paranal'), 0),
    ('KELT-9b', 880.000, 670.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Siding Spring Observatory'), 0),
    ('TOI-849 b', 39.000, 730.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Mauna Kea Observatory'), 0),
    ('Kepler-186f', 1.400, 580.00, 'CONFIRMEE', (SELECT id FROM observatoire WHERE nom = 'Mauna Kea Observatory'), 0),
    ('Kepler-452b', 5.000, 1400.00, 'CANDIDATE', (SELECT id FROM observatoire WHERE nom = 'Mauna Kea Observatory'), 0),
    ('Kepler-22b', 9.100, 640.00, 'CONFIRMEE',
     (SELECT id FROM observatoire WHERE nom = 'Kitt Peak National Observatory'), 0);