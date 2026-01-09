# Image Java officielle
FROM openjdk:17-jdk-slim

# Répertoire de travail
WORKDIR /app

# Copier le JAR généré
COPY target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]