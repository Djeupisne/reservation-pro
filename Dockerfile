# Image Java officielle
FROM openjdk:17-jdk-slim

# Répertoire de travail
WORKDIR /app

# Copie du JAR généré
COPY target/*.jar app.jar

# Expose le port
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]