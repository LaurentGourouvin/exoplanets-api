# ENONCE — API Exoplanètes (Java / Spring Boot / JPA-Hibernate)

Projet fictif **EXO**. Tu es dev back-end : tu dois livrer une API REST de
gestion d'**observatoires** et d'**exoplanètes**. Le schéma et les données
existent déjà (Flyway). À toi d'écrire **tout** le code Java.

> Rappel des règles du jeu : tu codes, je relis. Je ne te donne pas la solution
> tant que tu n'as pas tenté. À chaque ticket, note les **points JPA** signalés.

---

## Contexte & modèle

Un **observatoire** découvre plusieurs **exoplanètes**. Relation **N–1** :
`exoplanete → observatoire` (l'analogue de `livre → auteur`).

```
Observatoire 1 ────< N Exoplanete
```

**Table `observatoire`** : `id`, `nom` (UNIQUE), `pays`, `altitude_m`.
**Table `exoplanete`** : `id`, `designation` (UNIQUE), `masse_terre` (> 0),
`distance_al` (> 0), `statut` ∈ {`CANDIDATE`,`CONFIRMEE`,`REJETEE`},
`observatoire_id` (FK NOT NULL), `version` (verrou optimiste).

### Architecture imposée
- Couches strictes : **Controller → Service → Repository**. Le controller ne
  parle jamais au repository directement ; la logique vit dans le service.
- **Injection par constructeur** partout. Aucun `new` manuel d'un bean, aucun
  `@Autowired` sur champ.
- **Entités = classes** annotées `@Entity` (constructeur sans-arg requis par
  Hibernate, champs mutables). **DTO = records** immuables. On n'expose jamais
  une entité JPA directement en JSON (ni en entrée ni en sortie).
- `spring.jpa.hibernate.ddl-auto=validate` : Flyway est la seule source de
  vérité du schéma ; tes entités doivent **coller** au schéma, sinon le
  démarrage échoue.

### Conventions d'URL
- Préfixe `/api`. Ressources au pluriel : `/api/observatoires`,
  `/api/exoplanetes`.
- JSON en `camelCase` (ex. `observatoireId`, `masseTerre`, `distanceAl`).

### Format d'erreur (transverse à tous les tickets)
Toutes les erreurs renvoient un **`ProblemDetail`** (RFC 7807,
`Content-Type: application/problem+json`) — voir **EXO-6**. Aucune stack trace
ne doit fuiter.

---

## EPIC EXO — CRUD Exoplanètes + endpoint métier

### EXO-1 — Lister les exoplanètes (filtre + pagination)
**En tant que** client, **je veux** lister les exoplanètes, filtrables par
observatoire et paginées.

`GET /api/exoplanetes`
- `?observatoireId=2` (optionnel) : ne renvoie que les exoplanètes de cet
  observatoire.
- `?page=0&size=10` (optionnel) : pagination.

**Critères d'acceptation**
- [ ] `200 OK` avec la liste (DTO de sortie, jamais l'entité).
- [ ] Sans `observatoireId` → toutes les exoplanètes.
- [ ] Avec `observatoireId` connu → sous-ensemble filtré.
- [ ] Avec `observatoireId` **inconnu** → `200` + liste vide (pas 404 : une
      liste vide est une réponse valide).
- [ ] La pagination renvoie les métadonnées (total, page, taille).

**Points JPA à travailler**
- `JpaRepository<Exoplanete, Long>` te donne `findAll()` /
  `findAll(Pageable)` gratuitement. Pour le filtre, une **méthode dérivée**
  `findByObservatoireId(Long id, Pageable p)` suffit — Hibernate génère le SQL
  à partir du nom. (Compare : en JDBC tu écrivais le `SELECT ... WHERE` à la
  main.)
- ⚠️ **N+1** : si ton DTO de sortie contient le nom de l'observatoire, mapper
  N exoplanètes déclenche N `SELECT observatoire`. Active `show-sql` et
  regarde. Solutions à connaître : `@EntityGraph` sur la méthode repo, ou un
  `JOIN FETCH` (JPQL). Sache **expliquer** le problème en entretien.
- ⚠️ Pagination **+** `JOIN FETCH` sur une collection = piège (pagination en
  mémoire). Ici la relation paginée est `@ManyToOne` (côté "un"), donc
  `@EntityGraph` reste sûr — mais sache pourquoi la collection poserait
  problème.

---

### EXO-2 — Consulter une exoplanète par id
`GET /api/exoplanetes/{id}`

**Critères d'acceptation**
- [ ] `200 OK` + DTO si l'id existe.
- [ ] `404 Not Found` (ProblemDetail) si l'id n'existe pas.

**Points JPA**
- `findById` renvoie un `Optional`. Le `.orElseThrow(...)` d'une exception
  métier (→ 404) se fait **dans le service**, pas le controller.
- Différence `getReferenceById` (proxy paresseux, ex-`getOne`) vs `findById`
  (charge réellement). Sache quand chacun est pertinent.

---

### EXO-3 — Créer une exoplanète
`POST /api/exoplanetes` — corps :
```json
{ "designation": "HD 189733 b", "masseTerre": 363.0, "distanceAl": 64.5, "observatoireId": 4 }
```

**Critères d'acceptation**
- [ ] `201 Created` + header **`Location: /api/exoplanetes/{id}`** + DTO créé.
- [ ] Validation `@Valid` KO (designation vide, masse ≤ 0, distance ≤ 0,
      observatoireId nul) → `400 Bad Request` avec le détail des champs.
- [ ] `observatoireId` **inexistant** → `404 Not Found`.
- [ ] `designation` **déjà prise** → `409 Conflict`.
- [ ] `statut` par défaut à la création = `CANDIDATE` (non fourni par le client).

**Points JPA**
- Validation : annotations `jakarta.validation` (`@NotBlank`, `@Positive`,
  `@NotNull`) sur le **DTO d'entrée** (record), `@Valid` sur le paramètre du
  controller.
- Rattacher l'observatoire : charge-le via le repo (→ 404 si absent) puis
  `exoplanete.setObservatoire(obs)`. **Ne fabrique pas** une entité
  observatoire "coquille" juste avec l'id sans réfléchir — discutons du pour/
  contre de `getReferenceById` ici (évite un SELECT, mais masque le 404).
- Le 409 : deux écoles. (a) Vérifier l'unicité **avant** insert
  (`existsByDesignation`) — lisible mais TOCTOU en concurrence. (b) Laisser la
  contrainte DB parler et **traduire** `DataIntegrityViolationException` en 409
  dans le `@RestControllerAdvice`. Sache défendre les deux.
- ⚠️ `save()` sur une entité **transient** (id nul) fait un `INSERT`. Sur une
  entité **détachée** (id non nul) il peut faire un `merge`/`UPDATE`. Comprends
  transient / managed / detached / removed.

---

### EXO-4 — Mise à jour partielle (PATCH) ★ le morceau intéressant
`PATCH /api/exoplanetes/{id}` — seuls les champs présents sont modifiés :
```json
{ "distanceAl": 4.25 }
```

**Critères d'acceptation**
- [ ] `200 OK` + DTO à jour.
- [ ] Champs absents du corps = **inchangés** (vrai PATCH, pas un PUT déguisé).
- [ ] `404` si l'id n'existe pas.
- [ ] Validation des champs **présents** (ex. `masseTerre` fournie mais ≤ 0)
      → `400`.
- [ ] Si `designation` fournie et déjà prise par une autre → `409`.
- [ ] (Choix de conception à défendre) changer `observatoireId` : autorisé ou
      non ? Si oui, `observatoireId` inconnu → `404`.

**Points JPA — LE contraste avec JDBC**
- En JDBC tu écrivais un `UPDATE exoplanete SET ... WHERE id=?` avec des
  colonnes conditionnelles : pénible.
- En JPA, tu **charges l'entité managée** (`findById`) dans une méthode
  `@Transactional`, tu appelles les setters **uniquement pour les champs
  présents**, et… c'est tout. Le **dirty checking** d'Hibernate détecte les
  champs modifiés et émet l'`UPDATE` au flush/commit. Pas de SQL manuel.
- ⚠️ Distinguer "champ absent" de "champ présent à `null`". Avec un record aux
  champs `null` par défaut, `{"distanceAl": 4.25}` et un corps sans `distanceAl`
  sont **indiscernables** si tu regardes juste `!= null`. Réfléchis :
  `JsonNullable`/`Optional`, ou une convention documentée. Discutons-en.
- ⚠️ La méthode **doit** être `@Transactional` (sur le service). Hors
  transaction, l'entité est détachée → pas de dirty checking, ton PATCH ne
  persiste rien silencieusement. Piège classique.

---

### EXO-5 — Supprimer une exoplanète
`DELETE /api/exoplanetes/{id}`

**Critères d'acceptation**
- [ ] `204 No Content` si supprimée.
- [ ] `404` si l'id n'existe pas (ne pas renvoyer 204 pour un id inconnu).

**Points JPA**
- `deleteById` ne lève pas si l'id est absent → vérifie l'existence pour
  décider du 404.
- ⚠️ `@ManyToOne` / cascade : ici pas de cascade delete vers l'observatoire
  (surtout pas !). Vérifie que supprimer une exoplanète ne touche jamais son
  observatoire. Sache ce que font `CascadeType.*` et `orphanRemoval`, et
  pourquoi on les met **du bon côté** (jamais du `@ManyToOne`).

---

### EXO-6 — Gestion d'erreur centralisée (RFC 7807)
Un `@RestControllerAdvice` unique traduit les exceptions en `ProblemDetail`.

**Critères d'acceptation**
- [ ] `404` : ressource introuvable → `type/title/status/detail` cohérents.
- [ ] `409` : conflit d'unicité.
- [ ] `400` : échec de validation → lister les champs fautifs + message.
- [ ] `500` : toute exception non prévue, **message générique**, aucune stack
      trace, rien de sensible.
- [ ] `Content-Type: application/problem+json`.

**Points**
- Utilise `ProblemDetail` (Spring 6) ; possible d'étendre
  `ResponseEntityExceptionHandler` pour récupérer proprement les erreurs de
  validation (`MethodArgumentNotValidException`).
- Mappe `DataIntegrityViolationException` → 409 (cf. EXO-3), tes exceptions
  métier → 404/409/400. Un `@ExceptionHandler(Exception.class)` filet → 500.

---

## Bonus (à faire après le CRUD)

### EXO-7 — Endpoint métier : confirmer / rejeter (avec concurrence)
`POST /api/exoplanetes/{id}/confirmer` et `POST /api/exoplanetes/{id}/rejeter`.

**Règles métier**
- `confirmer` : `CANDIDATE → CONFIRMEE`. Depuis un autre statut → refus.
- `rejeter`  : `CANDIDATE → REJETEE`. Depuis un autre statut → refus.

**Critères d'acceptation**
- [ ] `200 OK` + DTO avec le nouveau statut si la transition est légale.
- [ ] Transition illégale (ex. confirmer une `REJETEE`) → `409 Conflict`
      (ou `422`, à défendre).
- [ ] `404` si id inconnu.
- [ ] En cas de modification concurrente → conflit optimiste géré (voir ci-dessous).

**Points JPA**
- La colonne `version` + `@Version` = **verrouillage optimiste**. Si deux
  requêtes modifient la même exoplanète, la seconde lève une
  `OptimisticLockingFailureException` au commit. Traduis-la en `409`.
- Transition = charger l'entité managée, vérifier la règle métier dans le
  service, changer le statut → dirty checking. Pas d'`UPDATE` SQL.
- Sache expliquer optimiste vs pessimiste (`@Lock(PESSIMISTIC_WRITE)`), et
  pourquoi l'optimiste convient à une API REST stateless.

### EXO-8 — CRUD Observatoires (support)
Au minimum : `GET /api/observatoires`, `GET /api/observatoires/{id}` (404),
`POST /api/observatoires` (201 + Location, `nom` unique → 409, validation → 400).
Cela te permet de créer des parents pour rattacher des exoplanètes.

**Points JPA**
- Côté `Observatoire`, un `@OneToMany(mappedBy = "observatoire")` est
  **optionnel**. Si tu l'ajoutes : `fetch = LAZY` (défaut), et **n'expose
  jamais** la collection en JSON directement (LazyInit + N+1 + récursion).
  Discutons de `mappedBy` et du côté propriétaire de la relation.

### EXO-9 — Test d'intégration Testcontainers
Le squelette câble déjà un PostgreSQL Testcontainers (`contextLoads`). Étends-le
en **vrais tests d'API** : au moins un test par verbe critique
(`POST` 201/400/404/409, `GET` 200/404, `PATCH` partiel, `DELETE` 204/404).

**Points**
- `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate`, ou
  `MockMvc`. Réutilise le conteneur (`@ServiceConnection`) — idéalement une
  classe de base abstraite pour ne démarrer le conteneur qu'une fois.
- Vérifie l'**état en base** après un PATCH, pas seulement la réponse HTTP :
  c'est là qu'on prouve que le dirty checking a bien persisté.

---

## Checklist "pièges JPA" (à relire avant l'entretien)
- [ ] **N+1** : sais-je le détecter (show-sql) et le corriger (`@EntityGraph` /
      `JOIN FETCH`) ?
- [ ] **LazyInitializationException** : pourquoi `open-in-view=false` la
      provoque, et pourquoi c'est une bonne chose ?
- [ ] **`@Transactional`** : sur le service, pas le controller ; le dirty
      checking n'opère que dans la transaction.
- [ ] **Entité vs record** : pourquoi Hibernate exige une classe mutable avec
      constructeur sans-arg ; pourquoi les DTO sont des records.
- [ ] **Ne jamais sérialiser une entité** : DTO en entrée/sortie.
- [ ] **`fetch = LAZY`** sur `@ManyToOne` (le défaut est EAGER !) — sais-je
      pourquoi je le force en LAZY ?
- [ ] **Cascade / orphanRemoval** : du bon côté, jamais par accident.
- [ ] **`@Version`** : verrou optimiste, traduction en 409.
- [ ] **`equals`/`hashCode`** sur entités JPA : les pièges (id généré, proxies).

Bonne chance. Commence par EXO-8 (créer un observatoire) ou EXO-3, puis
remonte. Montre-moi ton code ticket par ticket.
