# Étape 1 : build de l'application
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests


# Étape 2 : image finale
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]