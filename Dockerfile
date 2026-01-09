# Image Java officielle Eclipse Temurin
FROM eclipse-temurin:17-jdk-jammy

# Répertoire de travail
WORKDIR /app

# Copier le JAR généré
COPY target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]