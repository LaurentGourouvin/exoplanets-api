package com.example.exoplanetes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Test de fumee : verifie que le contexte Spring demarre.
 *
 * Un conteneur PostgreSQL ephemere est demarre par Testcontainers, et
 * @ServiceConnection cable automatiquement la datasource dessus (pas besoin
 * du docker-compose ni de reecrire spring.datasource.* ici). Flyway joue les
 * migrations, puis Hibernate VALIDE tes entites contre le schema.
 *
 * => Tant que tu n'as pas d'entite, ce test passe. Des que tu ajoutes une
 *    @Entity qui ne colle pas au schema Flyway, il ECHOUE au demarrage :
 *    c'est ton filet de securite "entites <-> schema".
 *
 * Pre-requis : un daemon Docker doit tourner sur ta machine (Testcontainers
 * l'utilise). Inutile en revanche d'avoir lance `docker compose up`.
 */
@SpringBootTest
@Testcontainers
class ExoplanetesApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void contextLoads() {
    }
}
