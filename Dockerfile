# Stage 1: Build de l'application
FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .

# Télécharger les dépendances (mis en cache si pom.xml ne change pas)
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler l'application
RUN mvn clean package -DskipTests

# Stage 2: Image de production légère
FROM amazoncorretto:17-alpine
WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]