# Exoplanètes API — exercice REST (Java / Spring Boot / JPA-Hibernate)

Exercice d'entraînement : construire **from scratch** une API REST en couches
`Controller → Service → Repository`, accès aux données via **JPA/Hibernate**
(pas de JdbcTemplate), schéma géré par **Flyway**.

> Ce dépôt ne contient **que l'infrastructure** : Docker, `pom.xml`, migrations
> Flyway, squelette Spring Boot, énoncé. Tout le code Java métier (entités,
> repositories, services, controllers, DTO, exceptions, gestion d'erreur) est
> **à écrire par toi**. L'énoncé détaillé est dans [`ENONCE.md`](./ENONCE.md).

## Domaine

Un **observatoire** (`Observatoire`) découvre plusieurs **exoplanètes**
(`Exoplanete`). Relation **N–1** : une exoplanète appartient à un observatoire.

| Table         | Champs clés |
|---------------|-------------|
| `observatoire` | `id`, `nom` (unique), `pays`, `altitude_m` |
| `exoplanete`   | `id`, `designation` (unique), `masse_terre`, `distance_al`, `statut` (`CANDIDATE`/`CONFIRMEE`/`REJETEE`), `observatoire_id` (FK), `version` (verrou optimiste) |

## Pré-requis

- Java 21+ (le wrapper `./mvnw` télécharge Maven tout seul au 1er lancement)
- Docker + Docker Compose
- Un daemon Docker qui tourne (aussi utilisé par Testcontainers pour les tests)

## 1. Lancer la base PostgreSQL (Docker)

```bash
docker compose up -d
# vérifier l'état (attendre "healthy")
docker compose ps
```

La base écoute sur `localhost:5432` (`exoplanetes` / `exo` / `exo`).

Pour tout remettre à zéro (supprime le volume et donc les données) :

```bash
docker compose down -v
```

## 2. Migrations Flyway

Tu n'as **rien à lancer manuellement** : Flyway joue automatiquement
`src/main/resources/db/migration/V1__init_schema.sql` puis `V2__seed_data.sql`
au démarrage de l'application. Le schéma et un jeu de données de test sont donc
créés au premier `./mvnw spring-boot:run`.

Vérifier après coup :

```bash
docker exec -it exoplanetes-db psql -U exo -d exoplanetes -c "\dt"
docker exec -it exoplanetes-db psql -U exo -d exoplanetes -c "SELECT * FROM exoplanete;"
```

## 3. Lancer l'application

```bash
./mvnw spring-boot:run
```

> Tant que tu n'as écrit aucun `@RestController`, l'appli démarre mais n'expose
> aucun endpoint métier (les `curl` ci-dessous renverront 404 tant que tu n'as
> pas codé les controllers — c'est normal, c'est le but de l'exercice).

## 4. Lancer les tests

```bash
./mvnw test
```

Le test `contextLoads` démarre un PostgreSQL éphémère via **Testcontainers**
(daemon Docker requis, mais `docker compose up` **pas** nécessaire), joue les
migrations, puis Hibernate **valide** tes entités contre le schéma.

## 5. Exemples curl (cibles à faire passer)

Ces commandes décrivent le comportement **attendu** une fois l'API codée.

### Observatoires
```bash
# Lister
curl -s http://localhost:8080/api/observatoires | jq

# Consulter par id
curl -s http://localhost:8080/api/observatoires/1 | jq

# Créer -> 201 + header Location
curl -i -X POST http://localhost:8080/api/observatoires \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Observatoire de Nice","pays":"France","altitudeM":372}'
```

### Exoplanètes
```bash
# Lister (toutes)
curl -s http://localhost:8080/api/exoplanetes | jq

# Filtrer par observatoire (clé étrangère)
curl -s 'http://localhost:8080/api/exoplanetes?observatoireId=2' | jq

# Pagination
curl -s 'http://localhost:8080/api/exoplanetes?page=0&size=3' | jq

# Consulter par id (404 si absent)
curl -i http://localhost:8080/api/exoplanetes/1
curl -i http://localhost:8080/api/exoplanetes/99999   # -> 404 ProblemDetail

# Créer -> 201 + Location
curl -i -X POST http://localhost:8080/api/exoplanetes \
  -H 'Content-Type: application/json' \
  -d '{"designation":"HD 189733 b","masseTerre":363.0,"distanceAl":64.5,"observatoireId":4}'

# Validation KO -> 400
curl -i -X POST http://localhost:8080/api/exoplanetes \
  -H 'Content-Type: application/json' \
  -d '{"designation":"","masseTerre":-1,"distanceAl":0,"observatoireId":4}'

# Observatoire inexistant -> 404
curl -i -X POST http://localhost:8080/api/exoplanetes \
  -H 'Content-Type: application/json' \
  -d '{"designation":"WASP-12b","masseTerre":439.0,"distanceAl":1410.0,"observatoireId":9999}'

# Désignation déjà prise -> 409
curl -i -X POST http://localhost:8080/api/exoplanetes \
  -H 'Content-Type: application/json' \
  -d '{"designation":"TRAPPIST-1e","masseTerre":0.7,"distanceAl":40.7,"observatoireId":2}'

# Mise à jour partielle PATCH
curl -i -X PATCH http://localhost:8080/api/exoplanetes/1 \
  -H 'Content-Type: application/json' \
  -d '{"distanceAl":4.25}'

# Endpoint métier : confirmer une candidate (CANDIDATE -> CONFIRMEE)
curl -i -X POST http://localhost:8080/api/exoplanetes/7/confirmer

# Supprimer -> 204 (404 si absent)
curl -i -X DELETE http://localhost:8080/api/exoplanetes/8
```

## Arborescence

```
exoplanetes-api/
├── docker-compose.yml
├── pom.xml
├── mvnw / mvnw.cmd / .mvn/wrapper/
├── README.md
├── ENONCE.md
└── src/
    ├── main/
    │   ├── java/com/example/exoplanetes/
    │   │   └── ExoplanetesApplication.java   <- fourni
    │   │   (à toi : entity/ repository/ service/ web/ dto/ exception/ ...)
    │   └── resources/
    │       ├── application.properties
    │       └── db/migration/
    │           ├── V1__init_schema.sql
    │           └── V2__seed_data.sql
    └── test/
        └── java/com/example/exoplanetes/
            └── ExoplanetesApplicationTests.java   <- contextLoads (fourni)
```
